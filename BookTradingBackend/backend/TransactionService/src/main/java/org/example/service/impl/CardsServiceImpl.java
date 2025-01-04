package org.example.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.request.CreateCardRequest;
import org.example.dto.response.CardResponse;
import org.example.entity.Cards;
import org.example.exception.ErrorType;
import org.example.exception.TransactionException;
import org.example.mapper.CardMapper;
import org.example.repository.CardsRepository;
import org.example.service.ICardService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardsServiceImpl implements ICardService {
    private final CardsRepository cardsRepository;
    private final CardMapper cardMapper;

    @Override
    public List<CardResponse> getAllCards() {
        List<Cards> cards = cardsRepository.findAll();
        if (cards.isEmpty()) {
            throw new TransactionException(ErrorType.CARD_NOT_FOUND);
        }
        return cards.stream().map(cardMapper::cardsToResponse).toList();
    }

    @Override
    @Transactional
    public CardResponse createCard(CreateCardRequest createCardRequest) {
        Cards card = cardMapper.requsetToCards(createCardRequest);
        card.setBalance(1000.0);
        card.setSaveDate(LocalDateTime.now());
        log.info("Create card: {}", card);
        return cardMapper.cardsToResponse(cardsRepository.save(card));
    }

    @Override
    public List<CardResponse> getUsersCards(String userId) {
        List<Cards> cards = cardsRepository.findAllByUserId(userId).orElseThrow(() -> new TransactionException(ErrorType.CARD_NOT_FOUND));
        return cards.stream().map(cardMapper::cardsToResponse).toList();
    }
}

