package org.example.external;


import org.example.dto.request.mail.ShippingMailReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.example.constant.RestApiList.TEST_SHIPPING_MAIL;

@FeignClient(url = "http://localhost:8080/api/v1/mail",name = "mailManager")
public interface MailManager {

    @PostMapping(TEST_SHIPPING_MAIL)
    public void testShippingMail(@RequestBody ShippingMailReq shippingMailReq);




}
