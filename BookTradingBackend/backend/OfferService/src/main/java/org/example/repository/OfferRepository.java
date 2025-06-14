package org.example.repository;

import org.example.entity.Offer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OfferRepository extends MongoRepository<Offer, String> {
    Optional<Offer> findByOffererIdAndId(String ownerId, String id);
    Optional<List<Offer>> findAllByOffererId(String ownerId);
    @Query("{ '$or': [ { 'offererId': ?0 }, { 'offerList.ownerId': ?0 } ], 'offerStatus': 'KABUL' }")
    List<Offer> findAcceptedOffersByUserId(String userId);
    Optional<Offer> findAllByOffererIdAndOfferStatusAndOfferedListId(String ownerId,String status,String listId);

}
