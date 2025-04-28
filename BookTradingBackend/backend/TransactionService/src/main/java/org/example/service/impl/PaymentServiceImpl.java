package org.example.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.request.CreatePaymentRequest;
import org.example.dto.request.SalesRequest;
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
import org.example.external.ListManager;
import org.example.mapper.PaymentMapper;
import org.example.repository.CardsRepository;
import org.example.repository.PaymentRepository;
import org.example.service.IPaymentService;
import org.example.service.ITransactionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private final ListManager listManager;

    @Override
    public Boolean checkCardInfos(CreatePaymentRequest createPaymentRequest, Cards cards) {
        // Kart bilgisi kontrolü
        boolean isCardNumberValid = createPaymentRequest.getCardNumber().equals(cards.getCardNumber());
        boolean isCvvValid = createPaymentRequest.getCvv().equals(cards.getCvv());
        boolean isFullnameValid = createPaymentRequest.getFullName().equals(cards.getFullName());
        boolean isExpiryValid = createPaymentRequest.getExpiryDate().equals(cards.getExpiryDate());

        return isCardNumberValid && isCvvValid && isExpiryValid && isFullnameValid;
    }


    /**
     * Ödeme başarılı ise transactiona aktarılır
     */
    @Override
    @Transactional
    public void createPayment(CreatePaymentRequest createPaymentRequest) {
        log.info("create payment request {}",createPaymentRequest);
        log.info("Creating payment for list: {}", createPaymentRequest.getListId());

        Cards cards = cardsRepository.findByCardNumber(createPaymentRequest.getCardNumber())
                .orElseThrow(() -> new TransactionException(ErrorType.CARD_NOT_FOUND));


        if (!(checkCardInfos(createPaymentRequest, cards))) {
            log.error("Card information is invalid for card: {}", cards.getCardNumber());
            throw new TransactionException(ErrorType.INVALID_CARD_INFO);
        }

        if (cards.getBalance() < createPaymentRequest.getAmount()) {
            log.warn("Insufficient balance for card: {}", cards.getCardNumber());

            throw new TransactionException(ErrorType.INSUFFICIENT_BALANCE);
        }

        // Bakiyeyi güncelle
        cards.setBalance(cards.getBalance() - createPaymentRequest.getAmount());
        cardsRepository.save(cards);

        for (String listId : createPaymentRequest.getListId()) {
            Double listPrice = listManager.getListPrice(listId).getBody();

            // Ödemeyi transaction servisine aktar
            TakePaymentRequest takePaymentRequest = TakePaymentRequest.builder()
                    .amount(listPrice)
                    .listId(listId)
                    .userId(createPaymentRequest.getUserId())
                    .build();

            transactionService.takePayment(takePaymentRequest);

            Payment successfulPayment = paymentMapper.requestToPayment(createPaymentRequest);
            successfulPayment.setStatus(PaymentStatus.SUCCESS);
            successfulPayment.setPaymentDate(LocalDateTime.now());
            if (createPaymentRequest.getListType().equals(ListType.SALE)) {
                SalesRequest salesRequest = SalesRequest.builder()
                        .offererId(createPaymentRequest.getUserId())
                        .listId(listId).build();
                listManager.processSales(salesRequest);
                log.info("sale processing requested {}", salesRequest);

            }
            paymentRepository.save(successfulPayment);
            cards.setBalance(cards.getBalance() - createPaymentRequest.getAmount());
            cardsRepository.save(cards);
        }
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
