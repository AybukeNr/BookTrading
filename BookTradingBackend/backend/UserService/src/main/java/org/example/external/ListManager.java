package org.example.external;


import org.example.dto.response.ListMailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.example.constant.RestApiList.LIST_MAIL_INFOS;

@FeignClient(url = "http://localhost:8081/api/v1/lists",name = "listsManager")
public interface ListManager {

    @GetMapping(LIST_MAIL_INFOS)
    public ResponseEntity<ListMailResponse> mailInfo(@RequestParam String listId);
}
