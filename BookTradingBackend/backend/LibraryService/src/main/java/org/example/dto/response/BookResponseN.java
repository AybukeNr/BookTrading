package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.enums.BookCategory;
import org.example.entity.enums.BookCondition;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponseN {
    private Long id;
    private String ownerId;
    private String title;
    private String author;
    private String isbn;
    private String publisher;
    private String publishedDate;
    private String image;
    private String category;
    private String description;
    private String condition;
    // Kullanıcı bilgileri
    private String firstName;
    private String lastName;
    private String userName;
    private Double trustPoint;

}
