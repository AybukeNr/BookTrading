package org.example.external;


import org.example.dto.request.SalesRequest;
import org.example.dto.request.UpdateListReq;
import org.example.dto.response.ListResponsePayment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.example.constant.RestApiList.*;

@FeignClient(url = "http://localhost:8081/api/v1/lists",name = "listsManager")
public interface ListManager {



    @PutMapping(UPDATE_LIST_STATUS)
    public ResponseEntity<Boolean> updateListStatus(@RequestBody UpdateListReq listReq);

    @PutMapping (PROCESS_SALES)
    public ResponseEntity<Boolean> processSales(@RequestBody SalesRequest sale);

}
