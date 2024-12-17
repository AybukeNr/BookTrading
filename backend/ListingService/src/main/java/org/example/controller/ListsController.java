package org.example.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.example.dto.request.CancelOfferRequest;
import org.example.dto.request.ListRequest;
import org.example.dto.response.ListResponse;

import org.example.dto.response.SentOffer;
import org.example.service.ListsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.example.constants.RestApiList.*;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping(LISTS)
@Slf4j
public class ListsController {
    private final ListsService listsService;



    @PostMapping(CREATE_LISTS)
    public ResponseEntity<Boolean> createList(@RequestBody ListRequest lists) {
        log.info("Received ListRequest: {}", lists);
        log.info("Received Book: {}", lists.getBookInfo());
        ListResponse response = listsService.createLists(lists);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping(GET_ALL_LISTS)
    public ResponseEntity<List<ListResponse>> getLists() {
        List<ListResponse> lists = listsService.getAllLists();
        return new ResponseEntity<>(lists, HttpStatus.OK);
    }
    @GetMapping(GET_LISTS_BY_OWNER_ID)
    public ResponseEntity<List<ListResponse>> getListsByOwnerId(@RequestParam String ownerId) {
        List<ListResponse> lists = listsService.getListsByOwnerId(ownerId);
        return new ResponseEntity<>(lists, HttpStatus.OK);
    }
    @GetMapping(GET_LIST_BY_ID)
    public ResponseEntity<ListResponse> getListById(@RequestParam String listId) {
        ListResponse lists = listsService.getListById(listId);
        log.info("Received ListRequest: {}", lists);
        log.info("Received Book: {}", lists.getBook());
        return new ResponseEntity<>(lists, HttpStatus.OK);
    }
    @PostMapping(TAKE_OFFER)
    public ResponseEntity<Boolean> takeOffer(@RequestBody SentOffer offer) {
        log.info("Received OfferRequest: {}", offer);
        listsService.takeOffers(offer);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
    @GetMapping(GET_RECIEVED_OFFERS)
    public ResponseEntity<List<SentOffer>> getRecievedOffers(@RequestParam String userId) {
        List<SentOffer> lists = listsService.getRecievedOffers(userId);
        return new ResponseEntity<>(lists, HttpStatus.OK);
    }
    @PostMapping(CANCEL_OFFER)
    public ResponseEntity<Boolean> cancelOffer(@RequestBody CancelOfferRequest offer) {
        log.info("Received OfferRequest: {}", offer);
        listsService.cancelOffer(offer);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }


}
