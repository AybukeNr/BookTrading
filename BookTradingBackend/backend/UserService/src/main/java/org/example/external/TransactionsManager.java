package org.example.external;


import org.example.dto.request.AccountRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import static org.example.constant.RestApiList.CREATE_ACCOUNT;

@FeignClient(url = "http://localhost:9093/api/v1/transactions",name = "transactionsManager")
public interface TransactionsManager {

    @PostMapping(CREATE_ACCOUNT)
    public ResponseEntity<Boolean> createAccount(AccountRequest accountRequest);

}
