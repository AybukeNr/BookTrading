package org.example.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.request.CreatePaymentRequest;
import org.example.dto.request.TakePaymentRequest;
import org.example.dto.request.TransactionRequest;
import org.example.dto.response.CardResponse;
import org.example.dto.response.PaymentResponse;
import org.example.dto.response.enums.ListType;
import org.example.entity.Cards;
import org.example.entity.Payment;
import org.example.entity.enums.PaymentStatus;
import org.example.exception.ErrorType;
import org.example.exception.TransactionException;
import org.example.mapper.PaymentMapper;
import org.example.repository.CardsRepository;
import org.example.repository.PaymentRepository;
import org.example.service.IPaymentService;
import org.example.service.ITransactionService;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements IPaymentService {

    private final PaymentRepository paymentRepository;
    private final CardsRepository cardsRepository;
    private final PaymentMapper paymentMapper;
    private final ITransactionService transactionService;

    @Override
    public Boolean checkCardInfos(CreatePaymentRequest createPaymentRequest, Cards cards) {
        // Kart numarası ve CVV kontrolü
        boolean isCardNumberValid = createPaymentRequest.getCardNumber().equals(cards.getCardNumber());
        boolean isCvvValid = createPaymentRequest.getCvv().equals(cards.getCvv());
        boolean isFullnameValid = createPaymentRequest.getFullName().equals(cards.getFullName());

        // Kartın son kullanma tarihini kontrol et
        String[] expiryParts = cards.getExpiryDate().split("/");
        String expiryMonth = expiryParts[0];
        String expiryYear = expiryParts[1];
        boolean isExpiryValid = checkExpiryDate(expiryMonth, expiryYear);


        // Tüm koşullar sağlanıyorsa true döndür
        return isCardNumberValid && isCvvValid && isExpiryValid;
    }

      public boolean checkExpiryDate(String expiryMonth, String expiryYear) {
        try {
            int month = Integer.parseInt(expiryMonth);
            int year = Integer.parseInt(expiryYear);

            if (month < 1 || month > 12) {
                return false;
            }

            YearMonth expiryDate = YearMonth.of(year, month);
            YearMonth currentDate = YearMonth.now();
            return !expiryDate.isBefore(currentDate); // Geçmişte değilse geçerli
        } catch (NumberFormatException e) {
            log.error("Invalid expiry date format: {}/{}", expiryMonth, expiryYear);
            return false;
        }
    }

    /**
     * Ödeme başarılı ise transactiona aktarılır
     * @param createPaymentRequest
     * @return
     */
    /**
     * Ödeme başarılı ise transactiona aktarılır.
     *
     * @param createPaymentRequest Ödeme talebi.
     * @return Ödeme yanıtı.
     */
    @Override
    @Transactional
    public PaymentResponse createPayment(CreatePaymentRequest createPaymentRequest) {
        log.info("create payment request {}",createPaymentRequest);
        log.info("Creating payment for list: {}", createPaymentRequest.getListId());

        // Kart bilgilerini veri tabanından getir
        Cards cards = cardsRepository.findByCardNumber(createPaymentRequest.getCardNumber())
                .orElseThrow(() -> new TransactionException(ErrorType.CARD_NOT_FOUND));

        // Kart bilgilerini doğrula
        if ((checkCardInfos(createPaymentRequest, cards))) {
            log.error("Card information is invalid for card: {}", cards.getCardNumber());
            throw new TransactionException(ErrorType.INVALID_CARD_INFO);
        }

        // Bakiye kontrolü
        if (cards.getBalance() < createPaymentRequest.getAmount()) {
            log.warn("Insufficient balance for card: {}", cards.getCardNumber());

            // Başarısız ödeme kaydı
            Payment failedPayment = paymentMapper.requestToPayment(createPaymentRequest);
            failedPayment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(failedPayment);

            throw new TransactionException(ErrorType.INSUFFICIENT_BALANCE);
        }

        // Bakiyeyi güncelle
        cards.setBalance(cards.getBalance() - createPaymentRequest.getAmount());
        cardsRepository.save(cards);

        // Başarılı ödeme kaydı
        Payment successfulPayment = paymentMapper.requestToPayment(createPaymentRequest);
        successfulPayment.setStatus(PaymentStatus.SUCCESS);
        if(createPaymentRequest.getListType().equals(ListType.SALE)){


        }

        // Ödemeyi transaction servisine aktar
        TakePaymentRequest takePaymentRequest = TakePaymentRequest.builder()
                .amount(createPaymentRequest.getAmount())
                .listId(createPaymentRequest.getListId())
                .userId(createPaymentRequest.getUserId())
                .build();

        transactionService.takePayment(takePaymentRequest);

        // Ödemeyi kaydet ve yanıt döndür
        Payment savedPayment = paymentRepository.save(successfulPayment);
        return paymentMapper.paymentToResponse(savedPayment);
    }
    @Override
    public List<PaymentResponse> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        if (payments.isEmpty()) {
            throw new TransactionException(ErrorType.PAYMENT_NOT_FOUND);
        }
        return payments.stream().map(paymentMapper::paymentToResponse).toList();
    }

    @Override
    public PaymentResponse getPayment(String paymentId) {
        return paymentMapper.paymentToResponse(paymentRepository.findByPaymentId(paymentId).orElseThrow(() -> new TransactionException(ErrorType.PAYMENT_NOT_FOUND)));
    }

    @Override
    public List<PaymentResponse> getUsersPayment(String userId) {
        List<Payment> payments = paymentRepository.findAllByUserId(userId).orElseThrow(() -> new TransactionException(ErrorType.PAYMENT_NOT_FOUND));
        return payments.stream().map(paymentMapper::paymentToResponse).toList();
    }

}