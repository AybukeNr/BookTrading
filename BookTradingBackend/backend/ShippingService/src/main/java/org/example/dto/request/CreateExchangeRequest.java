package org.example.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateExchangeRequest {

    private String transactionId; // İşlem kimliği
    private String listId; // İlgili ilan kimliği
    private String ownerShippingSerialNumber;
    private String offererShippingSerialNumber;
    private ListType listType;
    private String ownerId;
    private String offererId;

}
