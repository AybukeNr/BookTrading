package org.example.external;


import org.example.dto.request.ExchangeComplatedRequest;
import org.example.dto.request.TransferAllReq;
import org.example.dto.response.TransactionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.example.constant.RestApiList.*;
import static org.example.constant.RestApiList.SET_STATUS;

@FeignClient(url = "http://localhost:9093/api/v1/transactions",name = "transactionManager")
public interface TransactionsManager {

    @PutMapping(FINALIZE_PAYMENT)
    public ResponseEntity<TransactionResponse> finalizePayment(ExchangeComplatedRequest exchangeComplatedRequest);

    @PutMapping(REFUND_DEPOSITS)
    public ResponseEntity<TransactionResponse> refundBothParties(ExchangeComplatedRequest exchangeComplatedRequest);

    @PutMapping(TRANSFER_ALL)
    public ResponseEntity<TransactionResponse> transferAll(TransferAllReq transferAllReq);

    @PutMapping(SET_STATUS)
    public ResponseEntity<Boolean> setStatus(@RequestParam String transacId, @RequestParam String status);
}
