package org.example.repository;

import org.example.entity.Offer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OfferRepository extends MongoRepository<Offer, String> {
    Optional<Offer> findByOffererIdAndId(String ownerId, String id);
    Optional<List<Offer>> findAllByOffererId(String ownerId);
    Optional<List<Offer>> findAllByOffererIdAndOfferStatus(String ownerId,String status);

}
