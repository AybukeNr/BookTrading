package org.example.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.document.Offer;
import org.example.dto.request.CancelOfferRequest;
import org.example.dto.request.CreateOfferRequest;
import org.example.service.OfferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.constant.RestApiList.*;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping(OFFERS)
@Slf4j
public class OfferController {
    private final OfferService offerService;

    @GetMapping(GET_ALL_OFFERS)
    public ResponseEntity<List<Offer>> getAllOffers() {
        log.info("Getting all offers...");
        List<Offer> offerResponses = offerService.getAllOffers();
        return new ResponseEntity<>(offerResponses, HttpStatus.OK);
    }

    @PostMapping(CREATE_OFFER)
    public ResponseEntity<Offer> createOffer(@RequestBody CreateOfferRequest offerRequest) {
        log.info("Creating offer [{}]", offerRequest);
        Offer offerResponse = offerService.createOffer(offerRequest);
        return new ResponseEntity<>(offerResponse, HttpStatus.CREATED);
    }

    @GetMapping(GET_OFFERS_BY_OWNER_ID)
    public ResponseEntity<List<Offer>> getUsersOffers(@RequestParam String userId) {
        log.info("Getting user offers [{}]", userId);
        List<Offer> offers = offerService.getUsersOffers(userId);
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

    @PostMapping(CANCEL_OFFER)
    public ResponseEntity<Offer> cancelOffer(@RequestBody CancelOfferRequest cancelOfferRequest){
        log.info("Cancelling offer [{}]", cancelOfferRequest);
        Offer offer = offerService.cancelOffer(cancelOfferRequest);
        return new ResponseEntity<>(offer, HttpStatus.OK);
    }

}
