package org.example.external;


import org.example.dto.request.CreateShippingRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import static org.example.constants.RestApiList.CREATE_SHIPPING;

@FeignClient(url = "http://localhost:8082/api/v1/shippings",name = "shippingManager")
public interface ShippingManager {

    @PostMapping(CREATE_SHIPPING)
    public ResponseEntity<Boolean> createShipping(CreateShippingRequest createShippingRequest);
}
