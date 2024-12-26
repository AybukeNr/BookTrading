package org.example.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.enums.ShippingStatus;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShippingResponse {

    private String senderId;
    private Long senderBookId;
    private String trackingNumber;
    private ShippingStatus shippingStatus;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
