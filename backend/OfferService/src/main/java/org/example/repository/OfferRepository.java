package org.example.repository;

import org.example.document.Offer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OfferRepository extends MongoRepository<Offer, String> {
    Optional<Offer> findByOffererIdAndId(String ownerId, String id);
    Optional<List<Offer>> findAllByOffererId(String ownerId);
}
