package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.enums.BookCategory;
import org.example.entity.enums.BookCondition;
import org.example.entity.enums.BookStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {
    private String title;
    private String author;
    private String isbn;
    private String publisher;
    private String publishedDate;
    private String image;
    private String category;
    private BookCondition condition;
    private Long id;
    private BookStatus status;
    private String description;

}
