package org.example.dto.response.transaciton;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeInfos {
    private String userTracking;
    private String status;
    private String shippingSerialNumber; //gönderici olunan kargonun seri numarası
}
