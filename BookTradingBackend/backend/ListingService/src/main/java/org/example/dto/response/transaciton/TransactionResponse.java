package org.example.dto.response.transaciton;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private TransactionType transactionType;
    private TransactionStatus status; // ONGOING, COMPLETED, CANCELLED
    private Double trustFee;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private LocalDateTime deadline;
}
