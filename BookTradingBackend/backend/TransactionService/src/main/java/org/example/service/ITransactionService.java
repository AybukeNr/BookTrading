package org.example.service;

import org.example.dto.request.*;
import org.example.dto.response.CardResponse;
import org.example.dto.response.PaymentResponse;
import org.example.dto.response.TransactionResponse;
import org.example.entity.Transactions;

import java.util.List;

public interface ITransactionService {


    public List<TransactionResponse> getAllTransactions();

    public TransactionResponse createTransaciton(TransactionRequest transactionRequest);

    public List<TransactionResponse> getTransactionsByUserId(String userId);

    public TransactionResponse getTransaction(String transactionId);

    public TransactionResponse takePayment(TakePaymentRequest takePaymentRequest);

    public void checkTransactionStatus();

    public TransactionResponse refundBothParties(ExchangeComplatedRequest exchangeComplatedRequest);

    public TransactionResponse finalizePayment(ExchangeComplatedRequest exchangeComplatedRequest);

    public TransactionResponse transferAll(TransferAllReq transferAllReq);

    public boolean isDeadlineExceeded(Transactions transaction);

    public Boolean createAccount(AccountRequest accountRequest);



}