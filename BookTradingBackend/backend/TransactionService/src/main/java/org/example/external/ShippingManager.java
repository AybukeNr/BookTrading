package org.example.external;


import org.example.dto.response.ExchangeInfos;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.example.constant.RestApiList.GET_EXCHANGE_INFOS;

@FeignClient(url = "http://localhost:8082/api/v1/shippings",name = "shippingManager")
public interface ShippingManager {

    @GetMapping(GET_EXCHANGE_INFOS)
    public ResponseEntity<ExchangeInfos> getExchangeInfos(@RequestParam String userId, @RequestParam String listId);
}
