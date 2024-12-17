package org.example.repository;

import org.example.document.Books;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BooksRepository extends JpaRepository<Books, Long> {
    Optional<Books> findByIsbn(String isbn);
    Optional<Books> findByTitle(String title);
    Optional<Books> findByAuthor(String author);
    Optional<Books> findByTitleAndAuthor(String title, String author);
    Optional<List<Books>> findByOwnerId(String ownerId);
    Optional<Books> findOptionalById(Long id);

}
