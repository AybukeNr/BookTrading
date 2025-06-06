package org.example.service;

import org.example.dto.request.*;
import org.example.dto.response.CardResponse;
import org.example.dto.response.PaymentResponse;
import org.example.dto.response.TransactionInfo;
import org.example.dto.response.TransactionResponse;
import org.example.entity.Transactions;
import org.example.entity.enums.TransactionStatus;

import java.util.List;

public interface ITransactionService {


    public List<TransactionResponse> getAllTransactions();

    public TransactionResponse createTransaciton(TransactionRequest transactionRequest);

    public List<TransactionResponse> getTransactionsByUserId(String userId);

    public TransactionResponse getTransaction(String transactionId);

    public void takePayment(TakePaymentRequest takePaymentRequest);

    public void checkTransactionStatus();

    public Boolean setTransactionStatus(String id, String status);

    public Double calculateTrustFee(Long bookId);

    public TransactionResponse refundBothParties(ExchangeComplatedRequest exchangeComplatedRequest);

    public TransactionResponse finalizePayment(ExchangeComplatedRequest exchangeComplatedRequest);

    public TransactionResponse transferAll(TransferAllReq transferAllReq);

    public boolean isDeadlineExceeded(Transactions transaction);

    public Boolean createAccount(AccountRequest accountRequest);

    public List<TransactionResponse> getUsersExchanges(String ownerId);

    public List<TransactionResponse> getUsersSales(String ownerId);

    public List<TransactionInfo> transactionAllInfos(String userId);

    public TransactionInfo transactionInfosByListIdAndUserId(String listId,String userId);



}
