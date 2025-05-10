package org.example.dto.request;

import lombok.Data;
import org.example.entity.enums.BookCategory;
import org.example.entity.enums.BookCondition;

@Data
public class UpdateBookRequest {
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
