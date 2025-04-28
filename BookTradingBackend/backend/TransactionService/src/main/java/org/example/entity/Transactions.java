package org.example.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.enums.TransactionStatus;
import org.example.entity.enums.TransactionType;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "transactions")
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @UuidGenerator
    private String transactionId;

    private String listId;
    private String ownerId; // İlk tarafın kullanıcı ID'si
    private String offererId; // Karşı tarafın kullanıcı ID'si

    private Double ownerDeposit; // Göndericinin yatırdığı güvence bedeli
    private Double offererDeposit;// Alıcının yatırdığı güvence bedeli
    private Double trustFee; // iki tarafında yatırması gereken güvence bedeli,satış durumunda ilan fiyatı


    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status; // ONGOING, COMPLETED, CANCELLED

    private LocalDateTime deadline;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
