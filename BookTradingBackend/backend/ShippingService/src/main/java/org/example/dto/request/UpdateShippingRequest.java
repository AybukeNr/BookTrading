package org.example.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateShippingRequest {
    private String shippingSerialNumber;
    private String trackingNumber;
    private String userId;
}
