package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.response.enums.BookCondition;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    private BookCondition condition;

}
