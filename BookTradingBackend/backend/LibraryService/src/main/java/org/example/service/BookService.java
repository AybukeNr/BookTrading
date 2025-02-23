package org.example.service;


import lombok.RequiredArgsConstructor;
import org.example.dto.request.BookListRequest;
import org.example.dto.request.BookRequest;
import org.example.dto.request.ListRequest;
import org.example.dto.request.UpdateBookStat;
import org.example.dto.response.BookResponse;
import org.example.entity.Books;
import org.example.entity.enums.BookCondition;
import org.example.entity.enums.BookStatus;
import org.example.exception.BookException;
import org.example.exception.ErrorType;
import org.example.external.ListManager;
import org.example.mapper.BookMapper;
import org.example.repository.BooksRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Book;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private static final Logger log = LoggerFactory.getLogger(BookService.class);
    private final BooksRepository bookRepository;
    private final BookMapper bookMapper;
    private final ListManager listManager;

    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::BookToBookResponse) // Doğru mapper metodunu çağır
                .toList();
    }

    public List<BookResponse> getBooksByOwnerId(String id) {
        List<Books> books = bookRepository.findByOwnerId(id).orElseThrow(() -> new BookException(ErrorType.BAD_REQUEST_ERROR));
        return books.stream().map(bookMapper::BookToBookResponse).collect(Collectors.toList());
    }
    public BookResponse getBookById(Long id) {
        return bookMapper.BookToBookResponse(bookRepository.findById(id).orElseThrow(() -> new BookException(ErrorType.BAD_REQUEST_ERROR)));
    }

    public BookResponse getBookByTitle(String title) {
        Books books = bookRepository.findByTitle(title).orElseThrow(() -> new BookException(ErrorType.BAD_REQUEST_ERROR));
        return bookMapper.BookToBookResponse(books);
    }

    public BookResponse createBook(BookRequest bookRequest) {
        Books books = bookMapper.BookResquestToBook(bookRequest);
        log.info("Create book: {}", books);
        return bookMapper.BookToBookResponse(bookRepository.save(books));

    }
    public List<BookResponse> createAlotBooks(List<BookRequest> bookRequests) {
        List<Books> books = bookRequests.stream().map(bookMapper::BookResquestToBook).collect(Collectors.toList());
        return bookRepository.saveAll(books).stream().map(bookMapper::BookToBookResponse).collect(Collectors.toList());
    }

    @Transactional
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

    public BookCondition getBookConditionById(Long bookId) {
        Books book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookException(ErrorType.BOOK_NOT_FOUND));
        return book.getCondition();
    }

}
