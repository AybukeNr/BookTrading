package org.example.mapper;

import org.example.dto.request.TransactionRequest;
import org.example.dto.response.TransactionResponse;
import org.example.entity.Transactions;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public Transactions requestToTransaction(TransactionRequest transactionRequest) {
        return Transactions.builder()
                .listId(transactionRequest.getListId())
                .ownerId(transactionRequest.getOwnerId())
                .offererId(transactionRequest.getOffererId())
                .build();
    }
    public TransactionResponse transactionToResponse(Transactions transactions) {
        return TransactionResponse.builder()
                .listId(transactions.getListId())
                .transactionId(transactions.getTransactionId())
                .transactionType(transactions.getTransactionType())
                .ownerId(transactions.getOwnerId())
                .offererId(transactions.getOffererId())
                .ownerDeposit(transactions.getOwnerDeposit())
                .offererDeposit(transactions.getOffererDeposit())
                .status(transactions.getStatus())
                .transactionType(transactions.getTransactionType())
                .createdDate(transactions.getCreatedDate())
                .updatedDate(transactions.getUpdatedDate()).build();
    }
}
