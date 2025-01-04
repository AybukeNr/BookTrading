package org.example.service;

import org.example.dto.request.CreateCardRequest;
import org.example.dto.response.CardResponse;

import java.util.List;

public interface ICardService {

    List<CardResponse> getAllCards();

    CardResponse createCard(CreateCardRequest createCardRequest);

    List<CardResponse> getUsersCards(String userId);

}