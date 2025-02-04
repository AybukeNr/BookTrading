package org.example.external;


import org.example.dto.request.UpdateOfferRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.example.constants.RestApiList.UPDATE_OFFER;

@FeignClient(url = "http://localhost:9091/api/v1/offers",name = "offerManager")
public interface OfferManager {

    @PutMapping(UPDATE_OFFER)
    public ResponseEntity<BookManager> updateOffer(@RequestBody UpdateOfferRequest updateOfferRequest);


}
