package org.example.entity;

import jakarta.persistence.*;
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
@Entity
@Table(name = "book")
public class Books {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ownerId;
    private String title;
    private String author;
    private String isbn;
    private String publisher;
    private String publishedDate;
    private byte[] image;
    @Enumerated(EnumType.STRING)
    private BookCategory category;
    private BookCondition condition;
    private BookStatus status;


}