package org.example.external;


import org.example.dto.request.ExchangeComplatedRequest;
import org.example.dto.request.TransferAllReq;
import org.example.dto.response.TransactionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;

import static org.example.constant.RestApiList.*;

@FeignClient(url = "http://localhost:9092/api/v1/transactions",name = "transactionManager")
public interface TransactionsManager {

    @PatchMapping(FINALIZE_PAYMENT)
    public ResponseEntity<TransactionResponse> finalizePayment(ExchangeComplatedRequest exchangeComplatedRequest);

    @PatchMapping(REFUND_DEPOSITS)
    public ResponseEntity<TransactionResponse> refundBothParties(ExchangeComplatedRequest exchangeComplatedRequest);

    @PatchMapping(TRANSFER_ALL)
    public ResponseEntity<TransactionResponse> transferAll(TransferAllReq transferAllReq);
}
