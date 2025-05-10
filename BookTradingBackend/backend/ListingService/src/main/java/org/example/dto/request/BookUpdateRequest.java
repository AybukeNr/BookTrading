package org.example.dto.request;

import lombok.Data;
import org.example.dto.response.BookCondition;
import org.example.entity.enums.BookCategory;
@Data
public class BookUpdateRequest {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String publisher;
    private String publishedDate;
    private String image;
    private BookCategory category;
    private String description;
    private BookCondition condition;

}
