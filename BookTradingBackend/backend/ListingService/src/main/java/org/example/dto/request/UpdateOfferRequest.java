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
public class UpdateOfferRequest {
    private String offererId;
    private String offerId;
    private String listingId;
    private String offerStatus;
    private Long bookId;
}
