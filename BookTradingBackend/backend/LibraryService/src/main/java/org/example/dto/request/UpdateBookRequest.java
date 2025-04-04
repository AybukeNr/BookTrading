package org.example.dto.request;

import lombok.Data;
import org.example.entity.enums.BookCategory;

@Data
public class UpdateBookRequest {
    private String title;
    private String author;
    private String isbn;
    private String publisher;
    private String publishedDate;
    private String image;
    private BookCategory category;
}
