package org.example.mapper;


import org.example.dto.request.CreateExchangeRequest;
import org.example.dto.request.CreateShippingRequest;
import org.example.dto.response.ExchangeResponse;
import org.example.dto.response.ShippingResponse;
import org.example.entity.Exchange;
import org.example.entity.Shippings;
import org.example.entity.enums.ShippingStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ShippingMapper {
    /***
     * İlan sahibinin karşı tarafa göndereceği kargoyyu oluşturur
     * @param createShippingRequest
     * @return
     */
    public Shippings RequestToOwnerShipping(CreateShippingRequest createShippingRequest) {
        return Shippings.builder().senderId(createShippingRequest.getOwnerId())
                .recieverAddress(createShippingRequest.getOffererAddress()).build();


    }

    /**
     * Teklif sahibinin ilan sahibine göndereceği kargoyu oluşturur
     * @param createShippingRequest
     * @return
     */
    public Shippings RequestToOffererShipping(CreateShippingRequest createShippingRequest) {
        return Shippings.builder().senderId(createShippingRequest.getOffererId())
                .recieverAddress(createShippingRequest.getOwnerAddress()).build();

    }
    public ShippingResponse ShippingToResponse(Shippings shippings) {
        return ShippingResponse.builder()
                .senderId(shippings.getSenderId())
                .recieverAddress(shippings.getRecieverAddress())
                .trackingNumber(shippings.getTrackingNumber())
                .shippingStatus(shippings.getStatus())
                .createdDate(shippings.getCreatedDate())
                .updatedDate(shippings.getUpdatedDate())
                .deadline(shippings.getDeadline())
                .build();
    }

    public ExchangeResponse exchangeToResponse(Exchange exchange) {
        return ExchangeResponse.builder()
                .transactionId(exchange.getTransactionId())
                .listId(exchange.getListId())
                .ownerTrackingNumber(exchange.getOwnerTrackingNumber())
                .offererTrackingNumber(exchange.getOffererTrackingNumber())
                .status(exchange.getStatus())
                .createdDate(exchange.getCreatedDate())
                .updatedDate(exchange.getUpdatedDate())
                .deadline(exchange.getDeadline())
                .build();
    }


}
