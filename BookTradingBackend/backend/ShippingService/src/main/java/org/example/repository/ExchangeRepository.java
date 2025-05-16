package org.example.repository;

import org.example.entity.Exchange;
import org.example.entity.enums.ExchangeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExchangeRepository extends JpaRepository<Exchange,Long> {
    Optional<Exchange> findByTransactionId(String transactionId);
    Optional<Exchange> findByListId(String listId);
    List<Exchange> findAllByStatus(ExchangeStatus status);
    List<Exchange> findAllByOwnerIdOrOffererId(String senderId,String recieverId);

}
