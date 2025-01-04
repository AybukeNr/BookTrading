package org.example.dto.response;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.response.enums.TransactionStatus;
import org.example.dto.response.enums.TransactionType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {
    private String listId;
    private String ownerId; // İlk tarafın kullanıcı ID'si
    private String offererId; // Karşı tarafın kullanıcı ID'si

    private String transactionId;
    private Double ownerDeposit; // Göndericinin yatırdığı güvence bedeli
    private Double offererDeposit; // Alıcının yatırdığı güvence bedeli

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status; // ONGOING, COMPLETED, CANCELLED

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
