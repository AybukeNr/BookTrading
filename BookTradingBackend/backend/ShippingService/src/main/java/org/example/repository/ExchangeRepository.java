package org.example.repository;

import org.example.entity.ExchangeManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExchangeRepository extends JpaRepository<ExchangeManager,Long> {
    Optional<ExchangeManager> findByTransactionId(String transactionId);
}
