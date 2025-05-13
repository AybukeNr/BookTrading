package org.example.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.CreatePaymentRequest;
import org.example.dto.response.PaymentResponse;
import org.example.service.IPaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.constant.RestApiList.*;

@RestController
@RequestMapping(PAYMENT)
@RequiredArgsConstructor
public class PaymentController {

    private final IPaymentService paymentService;

    @Operation(description = "Satın alma ve güvence bedeli ödeme buradan yapılır")
    @GetMapping(GET_PAYMENT_BY_ID)
    public ResponseEntity<PaymentResponse> getPayment(@RequestParam String paymentId) {
        return new ResponseEntity<>(paymentService.getPayment(paymentId), HttpStatus.OK);
    }
    @GetMapping(GET_ALL_PAYMENTS)
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        return new ResponseEntity<>(paymentService.getAllPayments(), HttpStatus.OK);
    }
    @GetMapping(GET_USERS_PAYMENTS)
    public ResponseEntity<List<PaymentResponse>> getUsersPayments(@RequestParam String userId){
        return new ResponseEntity<>(paymentService.getUsersPayment(userId), HttpStatus.OK);
    }
    @PostMapping(CREATE_PAYMENT)
    public ResponseEntity<Void> createPayment(@RequestBody CreatePaymentRequest createPaymentRequest) {
        paymentService.createPayment(createPaymentRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
