package org.example.dto.request.mail;



import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class ListMailRequest {
    private String ownerId;
    private String offererId;
    private String listId;
    private String listBookName;
    private String listBookImage;
    private String offeredBookName;
    private String offeredBookImage;
    private String token;
    private Double trustFee;
    private LocalDateTime paymentdeadline;
    private LocalDateTime shipmentdeadline;

}
