package org.example.external;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.example.dto.request.TakePaymentRequest;
import org.example.dto.request.TransactionRequest;
import org.example.dto.response.TransactionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.example.constants.RestApiList.CREATE_TRANSACTION;
import static org.example.constants.RestApiList.TAKE_PAYMENT;

@FeignClient(url = "http://localhost:9093/api/v1/transactions",name = "transactionManager")
public interface TransactionManager {

   //feing clinet patch desteklemiyor ?
    @PatchMapping(TAKE_PAYMENT)
    public ResponseEntity<TransactionResponse> takePayment(
            @RequestBody TakePaymentRequest takePaymentRequest);


    @PostMapping(CREATE_TRANSACTION)
    public ResponseEntity<TransactionResponse> createTransaction(
            @RequestBody TransactionRequest transactionRequest);

}
