package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.enums.ExchangeStatus;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "exchanges")
public class ExchangeManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @UuidGenerator
    private String transactionId; // İşlem kimliği
    private String listId; // İlgili ilan kimliği
    private String ownerTrackingNumber; // Gönderen tarafın kargo ID'si
    private String offererTrackingNumber; // Alıcı tarafın kargo ID'si
    private String ownerShippingSerialNumber;
    private String offererShippingSerialNumber;
    private ExchangeStatus status; // Takas durumunu belirten alan
    private LocalDateTime createdDate;
    private LocalDateTime deadline;
    private LocalDateTime updatedDate;
}
