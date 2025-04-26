package org.example.repository;

import org.example.entity.Lists;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ListsRepository extends MongoRepository<Lists, String> {

    Optional<List<Lists>> findAllByOwnerId(String ownerId);

    Optional<Lists> findById(String listId);

    List<Lists> findByBookInfo_Id(Long Id);


}
