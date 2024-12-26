package org.example.mapper;

import org.example.dto.request.BookRequest;
import org.example.dto.response.BookResponse;
import org.example.entity.Books;
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
                .category(books.getCategory())
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
                .image(bookRequest.getImage()).build();

    }
}
