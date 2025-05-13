package org.example.mapper;

import org.example.dto.request.BookRequest;
import org.example.dto.request.UpdateBookRequest;
import org.example.dto.response.BookResponse;
import org.example.dto.response.OfferBookResponse;
import org.example.entity.Books;
import org.example.entity.enums.BookCategory;
import org.example.entity.enums.BookCondition;
import org.example.entity.enums.BookStatus;
import org.springframework.stereotype.Component;


@Component
public class BookMapper {

    public BookResponse BookToBookResponse(Books books){
        return BookResponse.builder()
                .title(books.getTitle())
                .author(books.getAuthor())
                .isbn(books.getIsbn())
                .publisher(books.getPublisher())
                .publishedDate(books.getPublishedDate())
                .image(books.getImage())
                .status(books.getStatus())
                .category(books.getCategory())
                .description(books.getDescription())
                .condition(books.getCondition())
                .id(books.getId())
                .build();
    }

    public Books BookResquestToBook(BookRequest bookRequest){
        return Books.builder()
                .ownerId(bookRequest.getOwnerId())
                .author(bookRequest.getAuthor())
                .title(bookRequest.getTitle())
                .isbn(bookRequest.getIsbn())
                .publisher(bookRequest.getPublisher())
                .publishedDate(bookRequest.getPublishedDate())
                .category(bookRequest.getCategory())
                .description(bookRequest.getDescription())
                .status(BookStatus.ENABLED)
                .condition(bookRequest.getCondition())
                .image(bookRequest.getImage())
                .build();

    }

    public OfferBookResponse bookToOfferBookResponse(Books books){
        return OfferBookResponse.builder().
                isbn(books.getIsbn()).
                title(books.getTitle()).
                id(books.getId()).
                image(books.getImage()).
                condition(books.getCondition()).
                description(books.getDescription()).
                publishedDate(books.getPublishedDate())
                        .publisher(books.getPublisher())
                                .author(books.getAuthor())
                                        .category(String.valueOf(books.getCategory()))
                                                .build();

    }


    public void updateBookFromRequest(UpdateBookRequest bookRequest, Books book) {
        if (bookRequest.getTitle() != null && !bookRequest.getTitle().isEmpty()) {
            book.setTitle(bookRequest.getTitle());
        }
        if (bookRequest.getAuthor() != null && !bookRequest.getAuthor().isEmpty()) {
            book.setAuthor(bookRequest.getAuthor());
        }
        if (bookRequest.getIsbn() != null && !bookRequest.getIsbn().isEmpty()) {
            book.setIsbn(bookRequest.getIsbn());
        }
        if (bookRequest.getPublisher() != null && !bookRequest.getPublisher().isEmpty()) {
            book.setPublisher(bookRequest.getPublisher());
        }
        if (bookRequest.getPublishedDate() != null && !bookRequest.getPublishedDate().isEmpty()) {
            book.setPublishedDate(bookRequest.getPublishedDate());
        }
        if (bookRequest.getImage() != null && !bookRequest.getImage().isEmpty()) {
            book.setImage(bookRequest.getImage());
        }
        if (bookRequest.getCategory() != null) {
            book.setCategory(bookRequest.getCategory());
        }
        if (bookRequest.getDescription() != null) {
            book.setDescription(bookRequest.getDescription());
        }
        if (bookRequest.getCondition() != null) {
            book.setCondition(bookRequest.getCondition());
        }
    }
}
