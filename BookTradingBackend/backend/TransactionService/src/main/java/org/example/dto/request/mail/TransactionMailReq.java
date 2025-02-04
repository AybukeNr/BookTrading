package org.example.dto.request.mail;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionMailReq {

    private String ownerId;
    private String offererId;
    private Double trustFee;
    private String status;
    private String listId;

}
