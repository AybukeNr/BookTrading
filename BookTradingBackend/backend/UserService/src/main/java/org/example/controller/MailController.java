package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.mail.*;
import org.example.service.MailService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.example.constant.RestApiList.*;


@RestController
@RequestMapping(MAIL)
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;

    @PostMapping(TEST_REGISTER_MAIL)
    public void testRegisterMail(@RequestBody RegisterMailRequest request){
        mailService.sendRegisterMail(request);
    }

    @PostMapping(TEST_FORGOT_MAIL)
    public void testForgotMail(@RequestBody ForgotMailRequest request) {
        mailService.sendForgotMail(request);
    }

    @PostMapping(TEST_SALE_LIST_MAIL)
    public void testSaleMail(@RequestBody ListMailRequest request) {
        mailService.sendSaleList(request);
    }

    @PostMapping(TEST_SHIPPING_MAIL)
    public void testShippingMail(@RequestBody ShippingMailReq shippingMailReq){
        mailService.sendShipping(shippingMailReq);
    }
    @PostMapping(TEST_TRANSACTION_CREATED)
    public void testTransactionCreated(@RequestBody TransactionMailReq transactionMailReq){
        mailService.sendTransaction(transactionMailReq);
        mailService.sendOffererTransaction(transactionMailReq);
    }
    @PostMapping(TEST_TRANSACTION_COMPLATED)
    public void testTransactionComplated(@RequestBody TransactionMailReq transactionMailReq){

    }
    @PostMapping(TEST_EXCHANGE_LIST_UPDATE)
    public void testExchangeListUpdated(@RequestBody ListMailRequest listMailReq){
        mailService.sendExchangeList(listMailReq);
    }

    @PostMapping(TEST_ACCEPTED_OFFER)
    public void testAcceptedOffer(@RequestBody ListMailRequest listMailRequest){
        mailService.sendAcceptedOffer(listMailRequest);
    }



}