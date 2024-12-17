package org.example.repository;

import org.example.document.Lists;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ListsRepository extends MongoRepository<Lists, String> {

    Optional<List<Lists>> findAllByOwnerId(String ownerId);

    Optional<Lists> findById(String listId);


}
