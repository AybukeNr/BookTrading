package org.example.external;


import org.example.dto.request.SalesRequest;
import org.example.dto.request.UpdateListReq;
import org.example.dto.response.ExchangeDetails;
import org.example.dto.response.ListResponsePayment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

import static org.example.constant.RestApiList.*;

@FeignClient(url = "http://localhost:8081/api/v1/lists",name = "listsManager")
public interface ListManager {

    @PutMapping(UPDATE_LIST_STATUS)
    public ResponseEntity<Boolean> updateListStatus(@RequestBody UpdateListReq listReq);

    @PutMapping (PROCESS_SALES)
    public ResponseEntity<Boolean> processSales(@RequestBody SalesRequest sale);

    @GetMapping(GET_LIST_PRICE)
    public ResponseEntity<Double> getListPrice(@RequestParam String listId);

    @GetMapping(GET_EXCHANGE_BOOKS)
    public ResponseEntity<ExchangeDetails> getExchangeBooks(@RequestParam String listId);

    @GetMapping(GET_LIST_TYPE)
    public ResponseEntity<String> getListType(@RequestParam String listId);

    @GetMapping(GET_LISTS_OWNERID)
    public ResponseEntity<String> getListOwnerId(@RequestParam String listId);


}
