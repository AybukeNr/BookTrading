package org.example.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.response.OfferStatus;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelOfferRequest {

    private String listId;
    private String offererId;       // Teklifi yapan kullanıcı
    private String offerId;
    private LocalDateTime cancelledOn;
}
