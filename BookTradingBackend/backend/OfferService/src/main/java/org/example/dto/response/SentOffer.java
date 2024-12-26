package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.enums.OfferStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SentOffer {
    private String offerId;
    private String offererId;
    private String offerListId;
    private Long offeredBookId;
    private OfferBookResponse offeredBook;
    private OfferStatus offerStatus;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
