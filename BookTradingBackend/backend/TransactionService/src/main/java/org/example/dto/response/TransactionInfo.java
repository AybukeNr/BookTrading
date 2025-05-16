package org.example.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionInfo {
    private String transactionId;
    private Double ownerDeposit; // Göndericinin yatırdığı güvence bedeli
    private Double offererDeposit;// Alıcının yatırdığı güvence bedeli
    private Double trustFee;
    private
}
