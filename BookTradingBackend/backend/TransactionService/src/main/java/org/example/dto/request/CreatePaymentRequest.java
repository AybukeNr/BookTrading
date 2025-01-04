package org.example.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.response.enums.ListType;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentRequest {

    private String listId;
    private ListType listType;
    private String userId;
    private String fullName;
    private String cardNumber;
    private String cvv;
    private String expiryDate;
    private Double amount;

}
