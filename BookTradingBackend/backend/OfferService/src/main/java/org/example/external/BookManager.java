package org.example.external;


import org.example.dto.request.UpdateBookStat;
import org.example.dto.response.OfferBookResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import static org.example.constant.RestApiList.*;

@FeignClient(url = "http://localhost:9090/api/v1/books",name = "booksManager")
public interface BookManager {
    @GetMapping(GET_OFFER_BOOK_BY_ID)
    public ResponseEntity<OfferBookResponse> getOfferBookById(@RequestParam Long id);

    @PutMapping(UPDATE_BOOK_STATUS)
    public ResponseEntity<Boolean> updateBookStat(@RequestBody UpdateBookStat updateBookStat);
}
