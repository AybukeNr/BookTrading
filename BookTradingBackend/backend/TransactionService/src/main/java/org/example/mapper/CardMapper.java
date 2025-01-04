package org.example.mapper;

import org.example.dto.request.CreateCardRequest;
import org.example.dto.response.CardResponse;
import org.example.entity.Cards;
import org.springframework.stereotype.Component;

@Component
public class CardMapper {
    public Cards requsetToCards(CreateCardRequest cards) {
        return Cards.builder()
                .userId(cards.getUserId())
                .cardNumber(cards.getCardNumber())
                .cvv(cards.getCvv())
                .fullName(cards.getFullName())
                .expiryDate(cards.getExpiryDate()).build();
    }
    public CardResponse cardsToResponse(Cards cards) {
        return CardResponse.builder()
                .cardNumber(cards.getCardNumber())
                .cvv(cards.getCvv())
                .expiryDate(cards.getExpiryDate())
                .fullName(cards.getFullName())
                .build();
    }

}
