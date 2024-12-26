package org.example.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.enums.TransactionStatus;

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

    private String senderId; // İlk tarafın kullanıcı ID'si
    private String receiverId; // Karşı tarafın kullanıcı ID'si

    private Double senderDeposit; // Göndericinin yatırdığı güvence bedeli
    private Double receiverDeposit; // Alıcının yatırdığı güvence bedeli

    @Enumerated(EnumType.STRING)
    private TransactionStatus status; // ONGOING, COMPLETED, CANCELLED

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
