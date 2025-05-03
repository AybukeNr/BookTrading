package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.response.OfferBookResponse;
import org.example.entity.enums.OfferStatus;
import org.example.dto.response.OfferListResponse;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "offers")
public class Offer {
    @Id
    private String id;
    private String offererId;
    private String offeredListId;
    private OfferListResponse offerList;
    private Long offeredBookId;
    private OfferBookResponse offeredBook;
    private OfferStatus offerStatus;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
