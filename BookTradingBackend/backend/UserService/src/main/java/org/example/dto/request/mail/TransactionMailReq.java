package org.example.dto.request.mail;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionMailReq {

    private String ownerId;
    private String offererId;
    private Double trustFee;
    private String status;
    private String listId;
    private String offeredBookName;
    private String offeredBookImage;
    private String listBookName;
    private String listBookImage;
    private LocalDateTime deadline;

}
