package org.example.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.request.*;
import org.example.dto.request.mail.ShippingMailReq;
import org.example.dto.response.ExchangeInfos;
import org.example.dto.response.ExchangeResponse;
import org.example.dto.response.ShippingResponse;
import org.example.entity.Exchange;
import org.example.entity.Shippings;
import org.example.entity.enums.ExchangeStatus;
import org.example.entity.enums.ExchangeType;
import org.example.entity.enums.ShippingStatus;
import org.example.exception.ErrorType;
import org.example.exception.ShippingException;
import org.example.external.MailManager;
import org.example.external.TransactionsManager;
import org.example.mapper.ShippingMapper;
import org.example.repository.ExchangeRepository;
import org.example.repository.ShippingRepository;

import org.example.service.IShippingService;
import org.example.util.TrackingNumberUtil;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class ShippingService implements IShippingService {
    private final ShippingRepository shippingRepository;
    private final ShippingMapper shippingMapper;
    private final ExchangeRepository exchangeRepository;
    private final TransactionsManager transactionsManager;
    private final MailManager mailManager;

    @Override
    @Transactional
    public Boolean createShipping(CreateShippingRequest createShippingRequest) {
        log.info("Creating shipping for list ID: {}", createShippingRequest.getListId());

        // Owner Shipping
        Shippings ownerShipping = shippingMapper.RequestToOwnerShipping(createShippingRequest);
        ownerShipping.setSenderId(createShippingRequest.getOwnerId());
        ownerShipping.setRecieverId(createShippingRequest.getOffererId());
        ownerShipping.setRecieverAddress(createShippingRequest.getOffererAddress());
        ownerShipping.setListId(createShippingRequest.getListId());
        ownerShipping.setCreatedDate(LocalDateTime.now());
        ownerShipping.setUpdatedDate(LocalDateTime.now());
        ownerShipping.setTrackingNumber(TrackingNumberUtil.generateTrackingNumber());
        ownerShipping.setDeadline(LocalDateTime.now().plusMinutes(5));
        ownerShipping.setStatus(ShippingStatus.BEKLEMEDE);

        shippingRepository.save(ownerShipping);
        log.info("Created owner shipping: {}", ownerShipping);

        // Offerer Shipping
        CreateExchangeRequest createExchangeRequest = new CreateExchangeRequest();
        if (createShippingRequest.getOwnerAddress() != null) {
            Shippings offererShipping = shippingMapper.RequestToOffererShipping(createShippingRequest);
            offererShipping.setSenderId(createShippingRequest.getOffererId());
            offererShipping.setRecieverId(createShippingRequest.getOwnerId());
            offererShipping.setRecieverAddress(createShippingRequest.getOwnerAddress());
            offererShipping.setListId(createShippingRequest.getListId());
            offererShipping.setCreatedDate(LocalDateTime.now());
            offererShipping.setUpdatedDate(LocalDateTime.now());
            offererShipping.setTrackingNumber(TrackingNumberUtil.generateTrackingNumber());
            offererShipping.setDeadline(LocalDateTime.now().plusMinutes(5));
            offererShipping.setStatus(ShippingStatus.BEKLEMEDE);

            shippingRepository.save(offererShipping);
            createExchangeRequest.setOffererShippingSerialNumber(offererShipping.getShippingSerialNumber());
            log.info("Created offerer shipping: {}", offererShipping);
        }

        // Exchange Request
        createExchangeRequest.setListType(createShippingRequest.getListType());
        createExchangeRequest.setOwnerShippingSerialNumber(ownerShipping.getShippingSerialNumber());
        createExchangeRequest.setListId(createShippingRequest.getListId());
        createExchangeRequest.setOwnerId(createShippingRequest.getOwnerId());
        createExchangeRequest.setOffererId(createShippingRequest.getOffererId());
        createExchangeRequest.setTransactionId(createShippingRequest.getTransactionId());

        boolean exchangeResult = createExchange(createExchangeRequest);
        if (!exchangeResult) {
            log.error("Exchange request failed for request: {}", createExchangeRequest);
            return false;
        }

        log.info("Exchange request successfully sent: {}", createExchangeRequest);
        return true;
    }

    @Override
    @Transactional
    public Boolean createExchange(CreateExchangeRequest createExchangeRequest) {
        log.info("Processing exchange request: {}", createExchangeRequest);
        Exchange exchange = new Exchange();
        exchange.setListId(createExchangeRequest.getListId());
        exchange.setOwnerShippingSerialNumber(createExchangeRequest.getOwnerShippingSerialNumber());
        if(createExchangeRequest.getOffererShippingSerialNumber() != null) {
            exchange.setOffererShippingSerialNumber(createExchangeRequest.getOffererShippingSerialNumber());
            exchange.setExchangeType(ExchangeType.EXCHANGE);
        }
        else{
            exchange.setExchangeType(ExchangeType.SALE);
        }
        exchange.setTransactionId(createExchangeRequest.getTransactionId());
        exchange.setStatus(ExchangeStatus.KARGO_BEKLENIYOR);
        exchange.setOwnerId(createExchangeRequest.getOwnerId());
        exchange.setOffererId(createExchangeRequest.getOffererId());
        exchange.setCreatedDate(LocalDateTime.now());
        exchange.setUpdatedDate(LocalDateTime.now());
        exchange.setDeadline(LocalDateTime.now().plusMinutes(5));
        exchangeRepository.save(exchange);
        log.info("Exchange created successfully: {}", exchange);
        return true;
    }


    //kargo takip no girme
    @Override
    @Transactional
    public ShippingResponse updateShippingStatus(UpdateShippingRequest updateShippingRequest) {
        Shippings shippings = shippingRepository.findByShippingSerialNumber(updateShippingRequest.getShippingSerialNumber()).orElseThrow(() -> new ShippingException(ErrorType.SHIPPING_NOT_FOUND));
        Exchange exchange = exchangeRepository.findByListId(shippings.getListId()).orElseThrow(() -> new ShippingException(ErrorType.EXCHANGE_NOT_FOUND));
        shippings.setUpdatedDate(LocalDateTime.now());
        if(updateShippingRequest.getTrackingNumber().equals(shippings.getTrackingNumber())) {
            shippings.setStatus(ShippingStatus.KARGOLANDI);
            shippingRepository.save(shippings);
            log.info("Shipping Updated : {}" ,shippings);

            if (updateShippingRequest.getShippingSerialNumber().equals(exchange.getOffererShippingSerialNumber())) {
                exchange.setOffererShippingSerialNumber(exchange.getOffererShippingSerialNumber());
            } else {
                exchange.setOwnerShippingSerialNumber(exchange.getOwnerShippingSerialNumber());
            }
            if(exchange.getOffererId().equals(updateShippingRequest.getUserId())){
                exchange.setOffererTrackingNumber(updateShippingRequest.getTrackingNumber());
            }
            else {
                exchange.setOwnerTrackingNumber(updateShippingRequest.getTrackingNumber());
            }
            exchange.setStatus(ExchangeStatus.KARGO_BEKLENIYOR);
            exchangeRepository.save(exchange);
            log.info("Exchange : {}", exchange);
            ShippingMailReq mailReq = new ShippingMailReq();
            mailReq.setSenderId(shippings.getSenderId());
            mailReq.setRecipientId(shippings.getRecieverId());
            mailReq.setTrackingNumber(shippings.getTrackingNumber());
            mailReq.setListId(exchange.getListId());
            mailManager.testShippingMail(mailReq);
        }
        return shippingMapper.ShippingToResponse(shippings);
    }



    @Override
    public List<ShippingResponse> getUsersShippings(String userId) {
        List<Shippings> shippings = shippingRepository.findAllBySenderIdOrRecieverId(userId,userId);

        return shippings.stream()
                .map(shippingMapper::ShippingToResponse)
                .toList();
    }

    /**
     * ExchangeType.SALE olanlarda sadece owner'a ait kargo durumunu kontrol eder.
     * Sale durumunda kargo takip numarası deadline sonrası yoksa, ödeme iptali için TransactionManager'a istek atar.
     * ExchangeType.EXCHANGE olanlarda her iki tarafın kargo durumunu kontrol eder.
     * Deadline geçmişse ve kargo takip numaralarından biri eksikse, tüm bedeli karşı tarafa geçirmek için TransactionManager'a istek atar.
     * Deadline geçmiş ve kargo durumları TESLIM_EDILDI değilse, karşılıklı iade için TransactionManager'a istek atar.
     * Deadline öncesinde teslim edilen tüm kargolar için her durumda karşılıklı iade işlemi için TransactionManager'a istek atar.
     *
     *
     */
    @Override
    @Scheduled(cron = "*/30 * * * * *")
    public void checkExchangeStatus() {
        log.info("Checking exchange status started");
        // Aktif takasları getir
        List<Exchange> activeExchanges = exchangeRepository.findAllByStatus(ExchangeStatus.KARGO_BEKLENIYOR);

        if(!activeExchanges.isEmpty()) {

            for (Exchange exchange : activeExchanges) {
                LocalDateTime deadline = exchange.getDeadline();
                boolean isDeadlinePassed = LocalDateTime.now().isAfter(deadline);

                try {
                    if (exchange.getExchangeType() == ExchangeType.SALE) {
                        // Sale türündeki işlemleri kontrol et
                        processSaleExchange(exchange, isDeadlinePassed);
                    } else if (exchange.getExchangeType() == ExchangeType.EXCHANGE) {
                        // Exchange türündeki işlemleri kontrol et
                        processExchangeExchange(exchange, isDeadlinePassed);
                    }
                } catch (Exception e) {
                    log.error("Error while processing exchange with transaction ID: {}", exchange.getTransactionId(), e);

                }
            }
        }
        else {
            log.info("No exchange to check found.");
        }


    }

    /**
     * Sale türündeki exchange işlemlerini kontrol eder.
     * @param exchange Exchange nesnesi
     * @param isDeadlinePassed Deadline'ın geçip geçmediği bilgisi
     */
    private void processSaleExchange(Exchange exchange, boolean isDeadlinePassed) {
        String ownerTrackingNumber = exchange.getOwnerTrackingNumber();
        Shippings shippings = shippingRepository.findByShippingSerialNumber(exchange.getOwnerShippingSerialNumber()).orElseThrow(() -> new ShippingException(ErrorType.SHIPPING_NOT_FOUND));

        if (isDeadlinePassed && (ownerTrackingNumber == null || ownerTrackingNumber.isEmpty())) {
            // Deadline geçmiş ve tracking number yoksa, ödeme iptali işlemi
            shippings.setStatus(ShippingStatus.İPTAL_EDİLDİ);
            shippingRepository.save(shippings);
            exchange.setStatus(ExchangeStatus.İPTAL_EDİLDİ);
            exchangeRepository.save(exchange);
            TransferAllReq transferAllReq = TransferAllReq.builder()
                            .transferUserId(exchange.getOffererId())
                                    .transactionId(exchange.getTransactionId()).build();
            transactionsManager.transferAll(transferAllReq);

        } else if (isDeadlinePassed && !isShippingDelivered(exchange.getOwnerShippingSerialNumber())&& !(ownerTrackingNumber == null || ownerTrackingNumber.isEmpty())) {
            // Deadline geçmiş ama kargo takip no var ve teslim edilmemişse, karşılıklı iade işlemi
            exchange.setStatus(ExchangeStatus.TAMAMLANDI);
            shippings.setStatus(ShippingStatus.TESLIM_EDILDI);
            shippingRepository.save(shippings);
            exchangeRepository.save(exchange);
            ExchangeComplatedRequest req = new ExchangeComplatedRequest();
            req.setOffererId(exchange.getOffererId());
            req.setOwnerId(exchange.getOwnerId());
            req.setTransactionId(exchange.getTransactionId());
            transactionsManager.finalizePayment(req);
        }
    }

    /**
     * Exchange türündeki exchange işlemlerini kontrol eder.
     * Eğer kargo numaralarından biri eksikse, bütün depozit kargo numarasını giren kullanıcıya geçer.
     * @param exchange Exchange nesnesi
     * @param isDeadlinePassed Deadline'ın geçip geçmediği bilgisi
     */
    private void processExchangeExchange(Exchange exchange, boolean isDeadlinePassed) {
        String ownerTrackingNumber = exchange.getOwnerTrackingNumber();
        String offererTrackingNumber = exchange.getOffererTrackingNumber();

        if (isDeadlinePassed) {
            // Deadline geçmiş ve kargo takip numaralarından biri eksikse
            if ((ownerTrackingNumber == null || ownerTrackingNumber.isEmpty()) ||
                    (offererTrackingNumber == null || offererTrackingNumber.isEmpty())) {
                // Kargo numarasını giren kullanıcıya tüm depoziti geçir
                String recipient = (ownerTrackingNumber != null && !ownerTrackingNumber.isEmpty())
                        ? exchange.getOwnerId()
                        : exchange.getOffererId();
                TransferAllReq transferAllReq = new TransferAllReq();
                transferAllReq.setTransactionId(exchange.getTransactionId());
                transferAllReq.setTransferUserId(recipient);

               transactionsManager.transferAll(transferAllReq);
               exchange.setStatus(ExchangeStatus.İPTAL_EDİLDİ);
               exchangeRepository.save(exchange);
               log.info("TransferAllReq: {}", transferAllReq);

            }
            else{
                ExchangeComplatedRequest req = new ExchangeComplatedRequest();
                req.setOffererId(exchange.getOffererId());
                req.setOwnerId(exchange.getOwnerId());
                req.setTransactionId(exchange.getTransactionId());
                exchange.setStatus(ExchangeStatus.TAMAMLANDI);
                exchangeRepository.save(exchange);
                transactionsManager.refundBothParties(req);
                log.info("Exchange successfully finalized for transaction ID: {}", exchange.getTransactionId());
            }
        } else if ((!isDeadlinePassed &&
                isShippingDelivered(exchange.getOwnerShippingSerialNumber()) &&
                isShippingDelivered(exchange.getOffererShippingSerialNumber())) ) {
            // Deadline öncesinde her iki kargo teslim edilmişse, karşılıklı iade işlemi başlat
            ExchangeComplatedRequest req = new ExchangeComplatedRequest();
            req.setOffererId(exchange.getOffererId());
            req.setOwnerId(exchange.getOwnerId());
            req.setTransactionId(exchange.getTransactionId());
            exchange.setStatus(ExchangeStatus.TAMAMLANDI);
            exchangeRepository.save(exchange);
            transactionsManager.refundBothParties(req);
            log.info("Exchange successfully finalized for transaction ID: {}", exchange.getTransactionId());
        }

    }

    /**
     * Kargonun TESLIM_EDILDI durumunda olup olmadığını kontrol eder.
     * @param shippingSerialNumber Kargo seri numarası
     * @return Boolean
     */
    private boolean isShippingDelivered(String shippingSerialNumber) {
        Optional<Shippings> shippingOpt = shippingRepository.findByShippingSerialNumber(shippingSerialNumber);
        if (shippingOpt.isEmpty()) {
            log.error("Shipping not found for serial number: {}", shippingSerialNumber);
            return false;
        }

        return shippingOpt.get().getStatus() == ShippingStatus.TESLIM_EDILDI;
    }

    @Override
    public ExchangeResponse findByTransactionId(String transactionId) {
        return null;
    }

    /**
     * Eğer kargo takip numaralarından ikisi de boşsa exchange iptal edilebilir, aksi halde hata fırlatır.
     *
     * @param transactionId Takas işleminin ID'si
     * @return Güncellenmiş ExchangeResponse
     */
    //todo -> transaction iptali eklendi,test edilmedi
    @Override
    @Transactional
    public ExchangeResponse cancelExchangeStatus(String transactionId) {

        Exchange exchange = exchangeRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ShippingException(ErrorType.EXCHANGE_NOT_FOUND));

        boolean ownerHasTrackingNumber = exchange.getOwnerTrackingNumber() != null && !exchange.getOwnerTrackingNumber().isBlank();
        boolean offererHasTrackingNumber = exchange.getOffererTrackingNumber() != null && !exchange.getOffererTrackingNumber().isBlank();

        if (!ownerHasTrackingNumber && !offererHasTrackingNumber) {
            exchange.setStatus(ExchangeStatus.İPTAL_EDİLDİ);
            exchange.setUpdatedDate(LocalDateTime.now());
            exchangeRepository.save(exchange);
            transactionsManager.setStatus(transactionId,"CANCELLED");
            log.info("Exchange cancelled successfully for transaction ID: {}", transactionId);
            return shippingMapper.exchangeToResponse(exchange);
        } else {
            throw new ShippingException(ErrorType.EXCHANGE_CANNOT_BE_CANCELLED);
        }
    }

    /**
     * Kargo teslim edilmiştir
     * @param shippingSerialNumber
     * @return
     */
    @Override
    @Transactional
    public ShippingResponse delivered(String shippingSerialNumber) {
        Shippings shippings = shippingRepository.findByShippingSerialNumber(shippingSerialNumber).orElseThrow(() -> new ShippingException(ErrorType.SHIPPING_NOT_FOUND));
        shippings.setStatus(ShippingStatus.TESLIM_EDILDI);
        shippingRepository.save(shippings);
        return shippingMapper.ShippingToResponse(shippings);
    }

    @Override
    public List<ShippingResponse> getAllShippings() {
        List<Shippings> shippings = shippingRepository.findAll();

        return shippings.stream().map(shippingMapper::ShippingToResponse).toList();
    }

    @Override
    public ShippingResponse getShippingBySn(String serialNumber) {
        Shippings shippings = shippingRepository.findByShippingSerialNumber(serialNumber).orElseThrow(() -> new ShippingException(ErrorType.SHIPPING_NOT_FOUND));
        return shippingMapper.ShippingToResponse(shippings);
    }

    @Override
    public List<ExchangeResponse> getAllExchanges() {
        List<Exchange> exchanges = exchangeRepository.findAll();
        if (exchanges.isEmpty()) {
            log.warn("No exchanges found.");
            return new ArrayList<>();
        }
        return exchanges.stream()
                .map(shippingMapper::exchangeToResponse)
                .toList();
    }

    @Override
    public List<ExchangeResponse> getAllExchangesByStatus(String exchangeStatus) {
        ExchangeStatus status;
        try {
            status = ExchangeStatus.valueOf(exchangeStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("Invalid exchange status: {}", exchangeStatus);
            throw new ShippingException(ErrorType.INVALID_STATUS);
        }

        List<Exchange> exchanges = exchangeRepository.findAllByStatus(ExchangeStatus.valueOf(String.valueOf(status)));
        if (exchanges.isEmpty()) {
            log.warn("No exchanges found for status: {}", exchangeStatus);
            return new ArrayList<>();
        }
        return exchanges.stream()
                .map(shippingMapper::exchangeToResponse)
                .toList();
    }

    @Override
    public ExchangeResponse getExchangeByTID(String transactionId) {
        Exchange exchange = exchangeRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ShippingException(ErrorType.EXCHANGE_NOT_FOUND));
        return shippingMapper.exchangeToResponse(exchange);
    }

    @Override
    public List<ExchangeResponse> getExchangeByUserID(String userId) {
        List<Exchange> userExchanges = exchangeRepository.findAllByOwnerIdOrOffererId(userId,userId);
        if (userExchanges.isEmpty()) {
            log.warn("No exchanges found for user ID: {}", userId);
            return new ArrayList<>();
        }
        return userExchanges.stream()
                .map(shippingMapper::exchangeToResponse)
                .toList();
    }

    private void sendShippingMail(ShippingMailReq mailReq){
        mailManager.testShippingMail(mailReq);
    }

    @Override
    public ExchangeInfos getExchangeInfos(String userId,String listId) {
        Exchange exchange = exchangeRepository.findByListId(listId).
                orElseThrow(() -> new ShippingException(ErrorType.EXCHANGE_NOT_FOUND));
        Shippings shippings = shippingRepository.findBySenderIdAndListId(userId,listId).orElseThrow(() -> new ShippingException(ErrorType.SHIPPING_NOT_FOUND));
        return ExchangeInfos.builder().shippingSerialNumber(shippings.getShippingSerialNumber())
                .userTracking(shippings.getTrackingNumber().equals(exchange.getOffererTrackingNumber()) ? exchange.getOffererTrackingNumber() : exchange.getOwnerTrackingNumber())
                .status(String.valueOf(exchange.getStatus())).build();
    }

}
