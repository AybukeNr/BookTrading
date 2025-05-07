package org.example.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.enums.BookCategory;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfferBookResponse {
    private String title;
    private String author;
    private String isbn;
    private String publisher;
    private String publishedDate;
    private String category;
    private String image;
    private String description;
    private Long id;

}
