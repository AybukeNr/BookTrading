package org.example.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.enums.BookCategory;
import org.example.entity.enums.BookCondition;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookUpdateRequest {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String publisher;
    private String publishedDate;
    private String image;
    private String category;
    private String description;
    private String condition;
}

