package org.example.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateShippingRequest {

    private String listId;
    private ListType listType;
    private String ownerId;
    private String offererId;
    private String ownerAddress;
    private String offererAddress;
    private String transactionId;

}
