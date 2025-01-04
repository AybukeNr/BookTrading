package org.example.mapper;

import org.example.dto.request.CreatePaymentRequest;
import org.example.dto.response.PaymentResponse;
import org.example.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {
    public PaymentResponse paymentToResponse(Payment payment){
        return PaymentResponse.builder()
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .senderCardNumber(payment.getSenderCardNumber()).build();
    }
    public Payment requestToPayment(CreatePaymentRequest createPaymentRequest){
        return Payment.builder()
                .listId(createPaymentRequest.getListId())
                .senderCardNumber(createPaymentRequest.getCardNumber())
                .amount(createPaymentRequest.getAmount()).build();
    }


}
