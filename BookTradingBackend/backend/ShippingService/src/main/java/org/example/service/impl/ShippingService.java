package org.example.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.request.CreateShippingRequest;
import org.example.dto.response.ExchangeResponse;
import org.example.dto.response.ShippingResponse;
import org.example.entity.ExchangeManager;
import org.example.entity.Shippings;
import org.example.entity.enums.ExchangeStatus;
import org.example.exception.ErrorType;
import org.example.exception.ShippingException;
import org.example.mapper.ShippingMapper;
import org.example.repository.ExchangeRepository;
import org.example.repository.ShippingRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShippingService {
    private final ShippingRepository shippingRepository;
    private final ShippingMapper shippingMapper;
    private final ExchangeRepository exchangeRepository;


    @Transactional
    public List<ShippingResponse> createShippingforExchange(CreateShippingRequest createShippingRequest) {
        log.info("Create shipping request: {}", createShippingRequest);
         Shippings ownerShipping = shippingRepository.save(shippingMapper.RequestToSenderShipping(createShippingRequest));
         Shippings offererShipping = shippingRepository.save(shippingMapper.RequestToReceiverShipping(createShippingRequest));
         List<ShippingResponse> shippings = new ArrayList<>();
         shippings.add(shippingMapper.ShippingToSenderResponse(ownerShipping));
         shippings.add(shippingMapper.ShippingToReceiverResponse(offererShipping));
         log.info("Create shipping response: {}", shippings);
         createExchange(createShippingRequest.getListID(),ownerShipping.getShippingSerialNumber(),offererShipping.getShippingSerialNumber());
         log.info("Exchange creating: {}", createShippingRequest.getListID());
         return shippings;
    }

    @Transactional
    public ExchangeResponse createExchange(String listId,String ownerSerialNumber,String offererSerialNumber){
        log.info("Creating exchange for list: {}", listId);
        ExchangeManager exchange = exchangeRepository.save(ExchangeManager.builder()
                .ownerShippingSerialNumber(ownerSerialNumber)
                .offererShippingSerialNumber(offererSerialNumber)
                .listId(listId)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .status(ExchangeStatus.BEKLEMEDE)
                .deadline(LocalDateTime.now().plusMinutes(1)).build());
        return shippingMapper.exchangeToResponse(exchange);
    }

    @Scheduled(cron = "*/5 * * * *")
    public void isExchangeCompleted() {//deadline geçmiş olanlar kontrol edilir
        List<ExchangeManager> exchange = exchangeRepository.findAll();
        for(ExchangeManager manager : exchange) {
            // İki tarafın kargo takip numaralarının girilip girilmediğini kontrol ediyoruz
            boolean isOffererTrackingNumberSet = manager.getOffererTrackingNumber() != null;
            boolean isOwnerTrackingNumberSet = manager.getOwnerTrackingNumber() != null;

            // Her iki takip numarası da giril
            if (isOffererTrackingNumberSet && isOwnerTrackingNumberSet) {

            }
        }

    }



}
