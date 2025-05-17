package org.example.repository;


import org.example.entity.Transactions;
import org.example.entity.enums.TransactionStatus;
import org.example.entity.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository  extends JpaRepository<Transactions, Long> {
    Optional<Transactions> findById(Long id);
    Optional<Transactions> findByTransactionId(String transactionId);
    Optional<List<Transactions>> findAllByOwnerIdOrOffererId(String ownerId, String offererId);
    Optional<Transactions> findByOwnerIdAndOffererId(String ownerId, String offererId);
    Optional<Transactions> findByOwnerIdAndOffererIdAndTransactionId(String ownerId, String offererId, String transactionId);
    Optional<Transactions> findByListId(String listId);
    List<Transactions> findAllByStatus(TransactionStatus status);
    Optional<List<Transactions>> findAllByOwnerIdOrOffererIdAndTransactionType(String ownerId,String offererId, TransactionType transactionType);

}
