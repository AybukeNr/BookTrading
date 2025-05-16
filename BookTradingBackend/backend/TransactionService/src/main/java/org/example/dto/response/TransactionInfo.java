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
    private String userId;
    private Double ownerDeposit; // Göndericinin yatırdığı güvence bedeli
    private Double offererDeposit;// Alıcının yatırdığı güvence bedeli
    private Double trustFee;
    private String shippingSerialNumber;
    private ListBookResponse listBook;
    private OfferBookResponse offerBook;
    private UserContactInfos owner;
    private UserContactInfos offerer;
    private String userTracking;
    private String status;
}
