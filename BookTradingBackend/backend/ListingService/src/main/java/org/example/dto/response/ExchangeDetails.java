package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.response.offer.OfferBookResponse;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeDetails {
    private OfferBookResponse sentBook;
    private ListBookResponse receivedBook;
}
