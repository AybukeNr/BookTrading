package org.example.external;


import org.example.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

import static org.example.constants.RestApiList.GET_ADDRESSES;
import static org.example.constants.RestApiList.GET_USER_RESPONSE_BY_ID;

@FeignClient(url = "http://localhost:8080/api/v1/users",name = "usersManager")
public interface UserManager {

    @GetMapping(GET_ADDRESSES)
    public Map<String, String> getAddresses(@RequestParam String ownerId , @RequestParam String offererId);

    @GetMapping(GET_USER_RESPONSE_BY_ID)
    UserResponse getUserResponseById(@RequestParam String id);

}
