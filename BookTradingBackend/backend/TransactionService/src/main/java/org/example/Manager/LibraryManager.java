package org.example.Manager;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static org.example.constant.RestApiList.GET_BOOK_CONDITION;

@FeignClient(url="http://localhost:9090/api/v1/books",name = "libraryManager")
public interface LibraryManager {
    @GetMapping(GET_BOOK_CONDITION)
    String getBookCondition(@PathVariable Long bookId);
}
