package org.example.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.response.enums.ListType;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequest {
    private Long bookId;
    private String listId;
    private ListType listType;
    private String ownerId; // İlk tarafın kullanıcı ID'si
    private String offererId; // Karşı tarafın kullanıcı ID'si
    private Double ownerDeposit; // Göndericinin yatırdığı güvence bedeli
    private Double offererDeposit; // Alıcının yatırdığı güvence bedeli
    private Double price;
}
