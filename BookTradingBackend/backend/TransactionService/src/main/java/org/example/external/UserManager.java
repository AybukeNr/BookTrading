package org.example.external;


import org.example.dto.response.UserContactInfos;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

import static org.example.constant.RestApiList.GET_USER_INFOS;
import static org.example.constant.RestApiList.REDUCE_TRUST_POINT;

@FeignClient(url = "http://localhost:8080/api/v1/users",name = "usersManager")
public interface UserManager {

    @PutMapping(REDUCE_TRUST_POINT)
    public ResponseEntity<Void> reduceTrustPoint(@RequestParam String userId);

    @GetMapping(GET_USER_INFOS)
    public ResponseEntity<Map<String,UserContactInfos>> getUserContactInfos(@RequestParam String userId, @RequestParam String offererId);
}
