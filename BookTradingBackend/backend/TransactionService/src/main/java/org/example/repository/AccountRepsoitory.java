package org.example.repository;

import org.example.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepsoitory extends JpaRepository<Account, Long> {
    Optional<Account> findByUserId(String userId);
}
