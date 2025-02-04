package org.example.repository;

import org.example.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByPhoneNumber(String phone);
    @Query(value = "{ '_id': { $in: [?0, ?1] } }", fields = "{ 'address': 1 }")
    List<String> findAddressesByIds(String ownerId,String offererId);
    @Query(value = "{ '_id': ?0 }", fields = "{ 'address': 1 }")
    Optional<String> findAddressById(String id);

    @Query(value = "{ '_id': ?0 }", fields = "{ 'email': 1 }")
    Optional<String> findEmailById(String id);

    @Query(value = "{ '_id': ?0 }", fields = "{ 'firstName': 1, 'lastName': 1 }")
    Optional<User> findNameById(String id);

}
