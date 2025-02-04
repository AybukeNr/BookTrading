package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "mail")
public class Mail {
    @Id
    private String id;
    private String token;
    private String sentToMailAddress;
    private String owneraddress;
    private String offereraddress;
    private String ownerFullName;
    private String offererFullName;
    private String listId;
    private String listBookName;
    private String listBookImage;
    private String offeredBookName;
    private String offeredBookImage;
    private LocalDateTime deadline;
    private String trackingNumber;
    private Double trustFee;
    private String title;
    private String author;
    private String isbn;
    private String publisher;
    private String publishedDate;
    private String category;
    private String status;
    @CreatedDate
    LocalDateTime createdDate;
    @LastModifiedDate
    LocalDateTime updatedDate;
}
