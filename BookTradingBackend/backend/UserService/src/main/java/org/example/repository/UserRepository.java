package org.example.repository;

import org.example.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);         // Null güvenliği için Optional
    Optional<User> findByUsername(String username);   // Null güvenliği için Optional
    Optional<User> findByPhoneNumber(String phone);   // Null güvenliği için Optional
    @Query(value = "{ '_id': { $in: [?0, ?1] } }", fields = "{ 'address': 1 }")
    List<String> findAddressesByIds(String ownerId, String offererId);

}
