package org.example.external;

import org.example.dto.request.CreateCardRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.example.constant.RestApiList.CREATE_CARD;

@FeignClient(url = "http://localhost:9093/api/v1/cards",name = "cardsManager")
public interface CardsManager {

    @PostMapping(CREATE_CARD)
    public ResponseEntity<Void> createCard(@RequestBody CreateCardRequest createCardRequest);
}
