package org.example.repository;

import org.example.entity.Lists;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ListsRepository extends MongoRepository<Lists, String> {

    Optional<List<Lists>> findAllByOwnerId(String ownerId);

    Optional<Lists> findById(String listId);

    List<Lists> findByBookInfo_Id(Long Id);

    List<Lists> findByOwnerIdNot(String ownerId);

    @Query("{ 'category': { $in: ?0 }, 'ownerId': { $ne: ?1 } }")
    List<Lists> findByCategory(Set<String> categories, String ownerId);

    @Query("{ 'bookInfo._id': ?0 }")
    Optional<Lists> findByBookInfoId(Long bookId);


}
