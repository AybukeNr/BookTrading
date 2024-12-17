package org.example.external;


import org.example.document.Offer;
import org.example.dto.request.CancelOfferRequest;
import org.example.dto.response.ListResponse;

import org.example.dto.response.SentOffer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import static org.example.constant.RestApiList.*;

@FeignClient(url = "http://localhost:8081/api/v1/lists",name = "listsManager")
public interface ListManager {

    @GetMapping(GET_LIST_BY_ID)
    public ResponseEntity<ListResponse> getListById(@RequestParam String listId);

    @PostMapping(TAKE_OFFER)
    public ResponseEntity<Boolean> takeOffer(@RequestBody SentOffer offer);

    @PostMapping(CANCEL_OFFER)
    public ResponseEntity<Boolean> cancelOffer(@RequestBody CancelOfferRequest cancelOfferRequest);

}
