package org.example.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.enums.BookCategory;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookRequest {
    private  Long id;
    private String ownerId;
    private String title;
    private String author;
    private String isbn;
    private String publisher;
    private String publishedDate;
    private String image;
    private BookCategory category;
}
