package org.example.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOfferRequest {
    private String offererId;       // Teklifi yapan kullanıcı
    private String listingId;      // Teklif yapılan ilan
    private Long offeredBookId;  // Teklif edilen kitap
}
