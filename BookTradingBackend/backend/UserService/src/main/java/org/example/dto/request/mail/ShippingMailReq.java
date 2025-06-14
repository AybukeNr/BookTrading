package org.example.dto.request.mail;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingMailReq {

    private String senderId;
    private String recipientId;
    private String trackingNumber;
    private String listId;

}
