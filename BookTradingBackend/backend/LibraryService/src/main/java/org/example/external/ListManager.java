package org.example.external;


import org.example.dto.request.BookUpdateRequest;
import org.example.dto.request.ListRequest;
import org.example.dto.request.UpdateBookRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.example.constant.RestApiList.CREATE_LISTS;

@FeignClient(url = "http://localhost:8081/api/v1/lists",name = "listsManager")
public interface ListManager {

    @PostMapping(CREATE_LISTS)
    public ResponseEntity<Boolean> createList(@RequestBody ListRequest lists);

    @DeleteMapping("/delete/by-book/{bookId}")
    ResponseEntity<String> deleteAllListsByBookId(@PathVariable("bookId") Long bookId);

    @PutMapping("/update-book-info")
    void updateBookInfo(@RequestBody BookUpdateRequest bookUpdateRequest);

}
