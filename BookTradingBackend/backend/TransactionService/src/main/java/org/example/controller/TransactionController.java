package org.example.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.request.*;
import org.example.dto.response.CardResponse;
import org.example.dto.response.PaymentResponse;
import org.example.dto.response.TransactionInfo;
import org.example.dto.response.TransactionResponse;
import org.example.service.ICardService;
import org.example.service.IPaymentService;
import org.example.service.ITransactionService;
import org.example.service.impl.TransactionServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.constant.RestApiList.*;


@RestController
@RequestMapping(TRANSACTIONS)
@RequiredArgsConstructor
@Tag(name = "Transaction Operations", description = "APIs for managing transaction operations.")
@Slf4j
@CrossOrigin("*")
public class TransactionController {

    private final ITransactionService transactionService;

    @Operation(summary = "Get all transactions", description = "Retrieves a list of all transactions.")
    @GetMapping(GET_ALL_TRANSACTIONS)
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        List<TransactionResponse> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Get transaction by ID", description = "Retrieves a specific transaction using its transaction ID.")
    @GetMapping(GET_TRANSACTION_BY_ID)
    public ResponseEntity<TransactionResponse> getTransaction(
            @Parameter(description = "ID of the transaction to retrieve", required = true)
            @RequestParam String transactionId) {
        return ResponseEntity.ok(transactionService.getTransaction(transactionId));
    }

    @Operation(summary = "Get transactions by user ID", description = "Retrieves all transactions associated with a specific user ID.")
    @GetMapping(GET_TRANSACTIONS_BY_USER_ID)
    public ResponseEntity<List<TransactionResponse>> getTransactionsByUserId(
            @Parameter(description = "User ID for which transactions are to be retrieved", required = true)
            @RequestParam String userId) {
        List<TransactionResponse> transactions = transactionService.getTransactionsByUserId(userId);
        return ResponseEntity.ok(transactions);
    }

    //servisler arası endpointler,buradan aşağısı
    @Operation(summary = "Create a new transaction,servisler arası endpoint", description = "Creates a new transaction and returns its details.")
    @PostMapping(CREATE_TRANSACTION)
    public ResponseEntity<TransactionResponse> createTransaction(@RequestBody TransactionRequest transactionRequest) {
        TransactionResponse response = transactionService.createTransaciton(transactionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Operation(summary = "Refund deposits to both parties,servisler arası endpoint", description = "Processes refunds for both parties involved in an exchange.")
    @PutMapping(REFUND_DEPOSITS)
    public ResponseEntity<TransactionResponse> refundBothParties(
            @Parameter(description = "Request body containing exchange completion details", required = true)
            @RequestBody ExchangeComplatedRequest exchangeComplatedRequest) {
        return ResponseEntity.ok(transactionService.refundBothParties(exchangeComplatedRequest));
    }

    @Operation(summary = "Finalize payment,servisler arası endpoint", description = "Finalizes the payment for a transaction after an exchange is completed.")
    @PutMapping(FINALIZE_PAYMENT)
    public ResponseEntity<TransactionResponse> finalizePayment(
            @Parameter(description = "Request body containing exchange completion details", required = true)
            @RequestBody ExchangeComplatedRequest exchangeComplatedRequest) {
        return ResponseEntity.ok(transactionService.finalizePayment(exchangeComplatedRequest));
    }

    @Operation(summary = "Take payment,servisler arası endpoint", description = "Processes payment collection for a transaction.")
    @PutMapping(TAKE_PAYMENT)
    public ResponseEntity<Void> takePayment(
            @Parameter(description = "Request body containing payment collection details", required = true)
            @RequestBody TakePaymentRequest takePaymentRequest) {
        transactionService.takePayment(takePaymentRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Transfer all funds,servisler arası endpoint", description = "Transfers the entire amount to the specified party.")
    @PutMapping(TRANSFER_ALL)
    public ResponseEntity<TransactionResponse> transferAll(
            @Parameter(description = "Request body containing transfer details", required = true)
            @RequestBody TransferAllReq transferAllReq) {
        return ResponseEntity.ok(transactionService.transferAll(transferAllReq));
    }
    @Operation(summary = "servisler arası endpoint")
    @PostMapping(CREATE_ACCOUNT)
    public ResponseEntity<Boolean> createAccount(@RequestBody AccountRequest accountRequest){
        log.info("account request {}" ,accountRequest);
        transactionService.createAccount(accountRequest);
        return new ResponseEntity<>(true, HttpStatus.CREATED);
    }
    @Operation(summary = "servisler arası endpoint")
    @PutMapping(SET_STATUS)
    public ResponseEntity<Boolean> setStatus(@RequestParam String transacId,@RequestParam String status){
        log.info("Transaction {} will be set to : {}" ,transacId,status);
        transactionService.setTransactionStatus(transacId,status);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<Double> getTrustFee(@PathVariable Long bookId) {
        Double trustFee =transactionService.calculateTrustFee(bookId);
        return ResponseEntity.ok(trustFee);
    }

    @Operation(summary = "kullanıcının takaslarını getirir")
    @GetMapping(GET_USERS_EXCHANGES)
    public ResponseEntity<List<TransactionResponse>> getUsersExchanges(@RequestParam String ownerId){
        return new ResponseEntity<>(transactionService.getUsersExchanges(ownerId),HttpStatus.OK);
    }

    @Operation(summary = "kullanıcının satışlarını getirir")
    @GetMapping(GET_USERS_SALES)
    public ResponseEntity<List<TransactionResponse>> getUsersSales(@RequestParam String ownerId){
        return new ResponseEntity<>(transactionService.getUsersSales(ownerId),HttpStatus.OK);
    }

    @Operation(summary = "kullanıcının takaslarının tüm bilgilerini getirir",description = "takaslarla ilgili her halt gelir")
    @GetMapping(GET_TRANSACTION_ALL_INFOS)
    public ResponseEntity<List<TransactionInfo>> transactionAllInfos(@RequestParam String userId){
        return new ResponseEntity<>(this.transactionService.transactionAllInfos(userId),HttpStatus.OK);
    }

    @Operation(summary = "kullanıcının takaslarının listId'ye göre bilgilerini getirir",description = "takaslarla ilgili her halt gelir")
    @GetMapping(GET_TRANSACTION_INFOS)
    public ResponseEntity<TransactionInfo> transactionInfos(@RequestParam String listId,@RequestParam String userId ){
        log.info("transactions info fetching..");
        return new ResponseEntity<>(this.transactionService.transactionInfosByListIdAndUserId(listId,userId),HttpStatus.OK);
    }


}