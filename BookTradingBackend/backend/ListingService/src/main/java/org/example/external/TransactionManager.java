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

@FeignClient(url = "http://localhost:9092/api/v1/transactions",name = "transactionManager")
public interface TransactionManager {

    @Operation(summary = "Take payment", description = "Processes payment collection for a transaction.")
    @PatchMapping(TAKE_PAYMENT)
    public ResponseEntity<TransactionResponse> takePayment(
            @Parameter(description = "Request body containing payment collection details", required = true)
            @RequestBody TakePaymentRequest takePaymentRequest);

    @Operation(summary = "Create a new transaction", description = "Creates a new transaction and returns its details.")
    @PostMapping(CREATE_TRANSACTION)
    public ResponseEntity<TransactionResponse> createTransaction(
            @Parameter(description = "Request body containing transaction details", required = true)
            @RequestBody TransactionRequest transactionRequest);

}