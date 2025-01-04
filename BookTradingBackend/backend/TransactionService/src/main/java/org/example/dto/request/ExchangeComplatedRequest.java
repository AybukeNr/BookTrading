package org.example.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeComplatedRequest {

    private String transactionId;
    private String ownerId;
    private String offererId;
}
