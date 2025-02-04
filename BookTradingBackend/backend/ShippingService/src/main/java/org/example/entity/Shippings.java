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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @UuidGenerator
    private String shippingSerialNumber;
    private String listId;
    private String senderId;// Gönderen kullanıcı ID
    private String recieverId;
    private String recieverAddress;
    private String trackingNumber; // Kargo takip numarası
    @Enumerated(EnumType.STRING)
    private ShippingStatus status; // Kargo durumu
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private LocalDateTime deadline;
}
