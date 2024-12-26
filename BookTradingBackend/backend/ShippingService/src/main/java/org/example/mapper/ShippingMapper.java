package org.example.mapper;


import org.example.dto.request.CreateShippingRequest;
import org.example.dto.response.ExchangeResponse;
import org.example.dto.response.ShippingResponse;
import org.example.entity.ExchangeManager;
import org.example.entity.Shippings;
import org.example.entity.enums.ExchangeStatus;
import org.example.entity.enums.ShippingStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ShippingMapper {
    public Shippings RequestToSenderShipping(CreateShippingRequest createShippingRequest) {
        return Shippings.builder().senderId(createShippingRequest.getSenderId())
                .receiverId(createShippingRequest.getReceiverId())
                .bookId(createShippingRequest.getSendingBookId())
                .status(ShippingStatus.BEKLEMEDE)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

    }
    public Shippings RequestToReceiverShipping(CreateShippingRequest createShippingRequest) {
        return Shippings.builder().senderId(createShippingRequest.getReceiverId())
                .receiverId(createShippingRequest.getSenderId())
                .bookId(createShippingRequest.getOfferedBookId())
                .status(ShippingStatus.BEKLEMEDE)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();
    }
    public ShippingResponse ShippingToSenderResponse(Shippings shippings) {
        return ShippingResponse.builder()
                .senderId(shippings.getSenderId())
                .senderBookId(shippings.getBookId())
                .trackingNumber(shippings.getTrackingNumber())
                .shippingStatus(shippings.getStatus())
                .createdDate(shippings.getCreatedDate())
                .updatedDate(shippings.getUpdatedDate())
                .build();
    }
    public ShippingResponse ShippingToReceiverResponse(Shippings shippings) {
        return ShippingResponse.builder()
                .senderBookId(shippings.getBookId())
                .senderId(shippings.getReceiverId())
                .trackingNumber(shippings.getTrackingNumber())
                .shippingStatus(shippings.getStatus())
                .createdDate(shippings.getCreatedDate())
                .updatedDate(shippings.getUpdatedDate())
                .build();
    }
    public ExchangeResponse exchangeToResponse(ExchangeManager exchange) {
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
