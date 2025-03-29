package org.example.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.*;
import org.example.dto.response.BookResponse;
import org.example.entity.enums.BookCondition;
import org.example.external.ListManager;
import org.example.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.constant.RestApiList.*;
import static org.example.constant.RestApiList.UPDATE_BOOK;

@RestController
@CrossOrigin("*")
@RequestMapping(BOOKS)
@RequiredArgsConstructor
public class BookController {
    private static final Logger log = LoggerFactory.getLogger(BookController.class);
    private final BookService bookService;



    @Operation(
            summary = "Getting All Books REST API",
            description = "REST API to get All Books"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error"
            )
    }
    )
    @GetMapping(GET_ALL_BOOKS)
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        List<BookResponse> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @Operation(
            summary = "Getting All Books By OwnerId REST API",
            description = "REST API to get All Books"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error"
            )
    }
    )
    @GetMapping(GET_BOOKS_BY_OWNER_ID)
    public ResponseEntity<List<BookResponse>> getBooksByOwnerId(@RequestParam String ownerId) {
        List<BookResponse> books = bookService.getBooksByOwnerId(ownerId);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @Operation(
            summary = "Creating Book REST API",
            description = "REST API to get All Books"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error"
            )
    }
    )
    @PostMapping(CREATE_BOOK)
    public ResponseEntity<BookResponse> createBook(@RequestBody BookRequest bookRequest) {
        BookResponse bookResponse = bookService.createBook(bookRequest);
        return new ResponseEntity<>(bookResponse, HttpStatus.OK);
    }
    @Operation(
            summary = "Creating many Book REST API",
            description = "REST API to Creating many Book"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error"
            )
    }
    )
    @PostMapping(CREATE_MANY_BOOK)
    public ResponseEntity<List<BookResponse>> createAlotBook(@RequestBody List<BookRequest> bookRequests){
        List<BookResponse> bookResponses = bookService.createAlotBooks(bookRequests);
        return new ResponseEntity<>(bookResponses, HttpStatus.OK);
    }
    @PostMapping(CREATE_LISTS)
    public ResponseEntity<Boolean> createList(@RequestBody BookListRequest bookListRequest){
        log.info("Recieved list request : {}",bookListRequest);
        bookService.createList(bookListRequest);
        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
    }
    @GetMapping(GET_BOOK_BY_ID)
    public ResponseEntity<BookResponse> findById(@RequestParam Long id){
        BookResponse bookResponse = bookService.getBookById(id);
        return new ResponseEntity<>(bookResponse,HttpStatus.OK);
    }
    //servisler arası endpoint
    @PutMapping(UPDATE_BOOK_STATUS)
    public ResponseEntity<Boolean> updateBookStat(@RequestBody UpdateBookStat updateBookStat){
        bookService.updateBookStatus(updateBookStat);
        return new ResponseEntity<>(true,HttpStatus.OK);

    }
    @PutMapping(UPDATE_BOOK)
    public ResponseEntity<BookResponse> updateBook(
            @RequestParam Long id,
            @RequestBody UpdateBookRequest bookRequest
    ) {
        try {
            BookResponse updatedBook = bookService.updateBook( id, bookRequest);
            return ResponseEntity.ok(updatedBook);
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping(DELETE_BOOK + "/{id}") // ✅ URL içinde kitap ID'si olacak
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteBookById(id);
            return ResponseEntity.noContent().build(); // ✅ 204 No Content
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // ✅ 404 Not Found
        }
    }


    @GetMapping(GET_BOOK_CONDITION)
    public ResponseEntity<BookCondition> getBookCondition(@PathVariable Long bookId) {
        BookCondition condition = bookService.getBookConditionById(bookId);
        return ResponseEntity.ok(condition);
    }
}
