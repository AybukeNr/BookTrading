package org.example.controller;


import lombok.RequiredArgsConstructor;
import org.example.dto.request.CreateCardRequest;
import org.example.dto.response.CardResponse;
import org.example.service.ICardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.constant.RestApiList.*;


@RestController
@RequestMapping(CARD)
@RequiredArgsConstructor
public class CardsController {
    private final ICardService cardService;

    @GetMapping(GET_ALL_CARDS)
    public ResponseEntity<List<CardResponse>> getAllCards(){
        return new ResponseEntity<>(cardService.getAllCards(), HttpStatus.OK);
    }
    @GetMapping(GET_USERS_CARDS)
    public ResponseEntity<List<CardResponse>> getCard(@RequestParam String userId){
        return new ResponseEntity<>(cardService.getUsersCards(userId),HttpStatus.OK);
    }
    @PostMapping(CREATE_CARD)
    public ResponseEntity<Void> createCard(@RequestBody CreateCardRequest createCardRequest){
        cardService.createCard(createCardRequest);
        return ResponseEntity.noContent().build();
    }
}
