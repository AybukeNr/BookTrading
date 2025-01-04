package org.example.repository;

import org.example.entity.Cards;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface CardsRepository extends JpaRepository<Cards, Long> {
    Optional<Cards> findByCardNumber(String cardNumber);
    Optional<Cards> findByUserId(String userId);
    Optional<List<Cards>> findAllByUserId(String userId);
}
