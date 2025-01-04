package org.example.external;


import org.example.dto.request.UpdateOfferRequest;
import org.example.dto.response.ListResponse;

import org.example.dto.response.SentOffer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.example.constant.RestApiList.*;

@FeignClient(url = "http://localhost:8081/api/v1/lists",name = "listsManager")
public interface ListManager {

    @GetMapping(GET_LIST_BY_ID)
    public ResponseEntity<ListResponse> getListById(@RequestParam String listId);

    @PostMapping(TAKE_OFFER)
    public ResponseEntity<Boolean> takeOffer(@RequestBody SentOffer offer);

    @PutMapping(UPDATE_OFFER)
    public ResponseEntity<Boolean> updateOffer(@RequestBody UpdateOfferRequest offer);

}
