package org.example.dto.response.offer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfferListResponse {
    private String listid;
    private String ownerId;
    private OfferBookResponse book;

}
