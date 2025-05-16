package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.response.offer.OfferBookResponse;
import org.example.dto.response.transaciton.ExchangeInfos;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeDetails {
    private OfferBookResponse acceptedBook;
    private ListBookResponse listBook;
}
