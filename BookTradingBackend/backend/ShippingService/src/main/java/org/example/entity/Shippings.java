package org.example.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.enums.ShippingStatus;
import org.hibernate.annotations.UuidGenerator;


import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "shippings")
public class Shippings {

    @Id
    private String id;
    @UuidGenerator
    private String shippingSerialNumber;
    private String senderId; // Gönderen kullanıcı ID
    private String receiverId; // Alıcı kullanıcı ID
    private Long bookId; // Gönderilen kitap ID
    private String trackingNumber; // Kargo takip numarası
    @Enumerated(EnumType.STRING)
    private ShippingStatus status; // Kargo durumu
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
