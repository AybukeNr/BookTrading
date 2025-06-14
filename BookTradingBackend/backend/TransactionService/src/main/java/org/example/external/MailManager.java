package org.example.external;


import org.example.dto.request.mail.TransactionMailReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.example.constant.RestApiList.TEST_TRANSACTION_COMPLATED;
import static org.example.constant.RestApiList.TEST_TRANSACTION_CREATED;

@FeignClient(url = "http://localhost:8080/api/v1/mail",name = "mailManager")
public interface MailManager {

    @PostMapping(TEST_TRANSACTION_COMPLATED)
    public void testTransactionComplated(@RequestBody TransactionMailReq transactionMailReq);
}
