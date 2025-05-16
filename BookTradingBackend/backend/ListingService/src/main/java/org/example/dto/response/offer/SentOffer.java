package org.example.dto.response.offer;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SentOffer {
    private String offerId;
    private String offererId;
    private String offerListId;
    private UserResponse offerer;
    private OfferListResponse offerList;
    private Long offeredBookId;
    private OfferBookResponse offeredBook;
    private OfferStatus offerStatus;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
