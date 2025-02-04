package org.example.external;


import org.example.dto.request.mail.ListMailRequest;
import org.example.dto.request.mail.TransactionMailReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.example.constants.RestApiList.*;

@FeignClient(url = "http://localhost:8080/api/v1/mail",name = "mailManager")
public interface MailManager {

    @PostMapping(TEST_SALE_LIST_MAIL)
    public void testSaleMail(@RequestBody ListMailRequest request);

    @PostMapping(TEST_TRANSACTION_CREATED)
    public void testTransactionCreated(@RequestBody TransactionMailReq transactionMailReq);

    @PostMapping(TEST_EXCHANGE_LIST_UPDATE)
    public void testExchangeListUpdated(@RequestBody ListMailRequest listMailReq);

    @PostMapping(TEST_ACCEPTED_OFFER)
    public void testAcceptedOffer(@RequestBody ListMailRequest listMailRequest);
}
