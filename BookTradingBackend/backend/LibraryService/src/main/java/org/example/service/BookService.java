package org.example.service;


import lombok.RequiredArgsConstructor;
import org.example.dto.request.*;
import org.example.dto.response.BookResponse;
import org.example.dto.response.BookResponseN;
import org.example.dto.response.OfferBookResponse;
import org.example.dto.response.UserResponseId;
import org.example.entity.Books;
import org.example.entity.enums.BookCondition;
import org.example.entity.enums.BookStatus;
import org.example.exception.BookException;
import org.example.exception.ErrorType;
import org.example.external.ListManager;
import org.example.external.UserManager;
import org.example.mapper.BookMapper;
import org.example.repository.BooksRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private static final Logger log = LoggerFactory.getLogger(BookService.class);
    private final BooksRepository bookRepository;
    private final BookMapper bookMapper;
    private final ListManager listManager;
    private final UserManager userManager;
    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::BookToBookResponse)
                .toList();
    }

    public List<BookResponse> getBooksByOwnerId(String id) {
        List<Books> books = bookRepository.findByOwnerId(id).orElseThrow(() -> new BookException(ErrorType.BAD_REQUEST_ERROR));
        return books.stream().map(bookMapper::BookToBookResponse).collect(Collectors.toList());
    }
    public BookResponseN getBookById(Long id) {
        Books book = bookRepository.findById(id)
                .orElseThrow(() -> new BookException(ErrorType.BAD_REQUEST_ERROR));

        // Feign Client ile kullanıcı bilgisi al
        UserResponseId userResponse = userManager.getUserResponseById(book.getOwnerId());

        // Book + User bilgileriyle BookResponseN oluştur
        return BookResponseN.builder()
                .id(book.getId())
                .ownerId(book.getOwnerId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .publisher(book.getPublisher())
                .publishedDate(book.getPublishedDate())
                .condition(book.getCondition())
                .image(book.getImage())
                .category(book.getCategory())
                .firstName(userResponse.getFirstName())
                .lastName(userResponse.getLastName())
                .userName(userResponse.getUserName())
                .trustPoint(userResponse.getTrustPoint())
                .build();
    }

    public OfferBookResponse getOfferBookById(Long id) {
        return bookMapper.bookToOfferBookResponse(bookRepository.findById(id).orElseThrow(() -> new BookException(ErrorType.BAD_REQUEST_ERROR)));
    }

    public BookResponse getBookByTitle(String title) {
        Books books = bookRepository.findByTitle(title).orElseThrow(() -> new BookException(ErrorType.BAD_REQUEST_ERROR));
        return bookMapper.BookToBookResponse(books);
    }

    public BookResponse createBook(BookRequest bookRequest) {
        Books books = bookMapper.BookResquestToBook(bookRequest);
        log.info("Create book: {}", books);
        log.info("Create book: {}", books.toString());
        bookRepository.save(books);
        return bookMapper.BookToBookResponse(books);

    }
    public List<BookResponse> createAlotBooks(List<BookRequest> bookRequests) {
        List<Books> books = bookRequests.stream().map(bookMapper::BookResquestToBook).collect(Collectors.toList());
        return bookRepository.saveAll(books).stream().map(bookMapper::BookToBookResponse).collect(Collectors.toList());
    }


    public void createList(BookListRequest bookListRequest) {
        // Kullanıcının etkin kitaplarını getir ve kontrol et
//        List<Books> enabledBooks = bookRepository.findEnabledBooksByUserId(bookListRequest.getOwnerId())
//                .orElseThrow(() -> new BookException(ErrorType.BAD_REQUEST_ERROR, "Kullanıcının etkin kitabı bulunamadı."));
//
//        // İlgili kitabı enabledBooks listesinden filtreleyerek bul
//        Books book = enabledBooks.stream()
//                .filter(b -> b.getId().equals(bookListRequest.getBookId()))
//                .findFirst()
//                .orElseThrow(() -> new BookException(ErrorType.BOOK_NOT_FOUND, "Kitap bulunamadı. ID: " + bookListRequest.getBookId()));
        Books book = bookRepository.findOptionalById(bookListRequest.getBookId())
                .orElseThrow(() -> new BookException(ErrorType.BOOK_NOT_FOUND));
        // Kitap bilgilerini DTO'ya dönüştür
        BookResponse bookResponse = bookMapper.BookToBookResponse(book);

        // ListRequest oluştur
        ListRequest listRequest = ListRequest.builder()
                .price(bookListRequest.getPrice())
                .bookInfo(bookResponse)
                .type(bookListRequest.getType())
                .ownerId(bookListRequest.getOwnerId())
                .build();

        // Kitap durumunu devre dışı yap ve kaydet
        book.setStatus(BookStatus.DISABLED);
        bookRepository.save(book); // Güncellemeyi kaydet

        // Liste oluşturma işlemini yönlendir
        log.info("Create list: {}", listRequest);
        listManager.createList(listRequest);
    }

    public Boolean updateBookStatus(UpdateBookStat updateBookStat){
        Books book = bookRepository.findById(updateBookStat.getBookId()).orElseThrow(() -> new BookException(ErrorType.BOOK_NOT_FOUND));
        book.setStatus(BookStatus.valueOf(updateBookStat.getStatus()));
        bookRepository.save(book);
        return true;
    }
    public BookResponse updateBook(Long bookId, UpdateBookRequest updateBookRequest) {
        Books existingBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + bookId));

        bookMapper.updateBookFromRequest(updateBookRequest, existingBook);
        bookRepository.save(existingBook);

        // BookUpdateRequest oluşturup id'yi manuel ekliyoruz
        BookUpdateRequest bookUpdateRequest = BookUpdateRequest.builder()
                .id(bookId)
                .title(updateBookRequest.getTitle())
                .author(updateBookRequest.getAuthor())
                .isbn(updateBookRequest.getIsbn())
                .publisher(updateBookRequest.getPublisher())
                .publishedDate(updateBookRequest.getPublishedDate())
                .image(updateBookRequest.getImage())
                .category(updateBookRequest.getCategory())
                .description(updateBookRequest.getDescription())
                .condition(updateBookRequest.getCondition())
                .build();

        listManager.updateBookInfo(bookUpdateRequest);

        return bookMapper.BookToBookResponse(existingBook);
    }


    public void deleteBookById(Long id) {
        try {
            Books existingBook = bookRepository.findById(id)
                    .orElseThrow(() -> new BookException(ErrorType.BOOK_NOT_FOUND));

            listManager.deleteAllListsByBookId(existingBook.getId());
            log.info("Deleted all lists for bookId: {}", existingBook.getId());

            bookRepository.delete(existingBook);
            log.info("Deleted book with ID: {}", existingBook.getId());

        } catch (Exception e) {
            log.error("Kitap silinirken hata oluştu: {}", e.getMessage(), e);
            throw new BookException(ErrorType.INTERNAL_SERVER_ERROR);
        }
    }
    public BookCondition getBookConditionById(Long bookId) {
        Books book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookException(ErrorType.BOOK_NOT_FOUND));
        return book.getCondition();
    }


}
