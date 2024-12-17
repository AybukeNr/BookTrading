package org.example.external;


import org.example.dto.response.OfferBookResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.example.constant.RestApiList.GET_BOOK_BY_ID;

@FeignClient(url = "http://localhost:9090/api/v1/books",name = "booksManager")
public interface BookManager {
    @GetMapping(GET_BOOK_BY_ID)
    public ResponseEntity<OfferBookResponse> findById(@RequestParam Long id);
}
