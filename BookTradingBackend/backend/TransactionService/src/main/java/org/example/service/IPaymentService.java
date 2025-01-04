package org.example.service;

import org.example.dto.request.CreatePaymentRequest;
import org.example.dto.request.ExchangeComplatedRequest;
import org.example.dto.request.TakePaymentRequest;
import org.example.dto.response.PaymentResponse;
import org.example.dto.response.TransactionResponse;
import org.example.entity.Cards;

import java.util.List;

// Payment Service Interface
public interface IPaymentService {

    PaymentResponse createPayment(CreatePaymentRequest createPaymentRequest);

    List<PaymentResponse> getAllPayments();

    PaymentResponse getPayment(String paymentId);

    List<PaymentResponse> getUsersPayment(String userId);

    public Boolean checkCardInfos(CreatePaymentRequest createPaymentRequest, Cards cards);




}

