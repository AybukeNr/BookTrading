package org.example.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.enums.ExchangeStatus;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeResponse {
    private String transactionId; // İşlem kimliği
    private String listId; // İlgili ilan kimliği
    private String ownerTrackingNumber; // Gönderen tarafın kargo ID'si
    private String offererTrackingNumber; // Alıcı tarafın kargo ID'si
    private ExchangeStatus status; // Takas durumunu belirten alan
    private LocalDateTime createdDate;
    private LocalDateTime deadline;
    private LocalDateTime updatedDate;
}
