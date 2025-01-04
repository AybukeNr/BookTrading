package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.enums.ExchangeStatus;
import org.example.entity.enums.ExchangeType;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "exchanges")
public class Exchange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String transactionId; // İşlem kimliği
    private String listId; // İlgili ilan kimliği
    private String ownerId;
    private String offererId;
    private String ownerShippingSerialNumber;
    private String offererShippingSerialNumber;
    private String ownerTrackingNumber;
    private String offererTrackingNumber;
    @Enumerated(EnumType.STRING)
    private ExchangeType exchangeType;
    @Enumerated(EnumType.STRING)
    private ExchangeStatus status; // Takas durumunu belirten alan
    private LocalDateTime createdDate;
    private LocalDateTime deadline;
    private LocalDateTime updatedDate;
}
