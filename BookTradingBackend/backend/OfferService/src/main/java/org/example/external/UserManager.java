package org.example.external;

import org.example.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.example.constant.RestApiList.GET_USER_RESPONSE_BY_ID;

@FeignClient(url = "http://localhost:8080/api/v1/users",name = "usersManager")
public interface UserManager {
    @GetMapping(GET_USER_RESPONSE_BY_ID)
    UserResponse getUserResponseById(@RequestParam String id);
}