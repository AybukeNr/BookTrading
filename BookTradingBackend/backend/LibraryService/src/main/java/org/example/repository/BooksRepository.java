package org.example.repository;

import org.example.entity.Books;
import org.example.entity.enums.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BooksRepository extends JpaRepository<Books, Long> {
    Optional<Books> findByIsbn(String isbn);
    Optional<Books> findByTitle(String title);
    Optional<Books> findByAuthor(String author);
    Optional<Books> findByTitleAndAuthor(String title, String author);
    Optional<List<Books>> findByOwnerId(String ownerId);
    Optional<Books> findOptionalById(Long id);
    Optional<Books> findOptionalByPk(String pk);

//    @Query("SELECT b FROM Books b WHERE b.ownerId = :id AND b.status = 0")
//   Optional<List<Books>> findEnabledBooksByUserId(@Param("id") String id);

  //  @Query("SELECT b FROM Books b WHERE b.ownerId = :id AND b.status = :status")
    //Optional<List<Books>> findBooksByUserIdAndStatus(@Param("id") String id, @Param("status") int status);
}
