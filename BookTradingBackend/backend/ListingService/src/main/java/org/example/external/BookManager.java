package org.example.external;


import org.example.dto.request.UpdateBookStat;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.example.constants.RestApiList.UPDATE_BOOK_STATUS;

@FeignClient(url = "http://localhost:9090/api/v1/books",name = "bookManager")
public interface BookManager {

    @PutMapping(UPDATE_BOOK_STATUS)
    public ResponseEntity<Boolean> updateBookStat(@RequestBody UpdateBookStat updateBookStat);
}
