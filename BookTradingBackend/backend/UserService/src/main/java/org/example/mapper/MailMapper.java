package org.example.mapper;


import org.example.dto.request.mail.ListMailRequest;
import org.example.dto.request.mail.MailRequest;
import org.example.dto.request.mail.ShippingMailReq;
import org.example.dto.request.mail.TransactionMailReq;
import org.example.entity.Mail;
import org.springframework.stereotype.Component;

@Component
public class MailMapper {
    public Mail mapRegisterMailToMail(MailRequest request) {
        Mail mail = new Mail();
        mail.setSentToMailAddress(request.getSentToMailAddress());
        return mail;

    }
    public Mail mapSaleListMailRequestToMail(ListMailRequest request) {
        Mail mail = new Mail();
        mail.setListId(request.getListId());
        mail.setListBookName(request.getListBookName());
        mail.setListBookImage(request.getListBookImage());
        mail.setDeadline(request.getShipmentdeadline());
        return mail;

    }
    public Mail mapExchangeMailRequestToMail(ListMailRequest request) {
        Mail mail = new Mail();
        mail.setListId(request.getListId());
        mail.setListBookName(request.getListBookName());
        mail.setListBookImage(request.getListBookImage());
        mail.setDeadline(request.getShipmentdeadline());
       // mail.setTrustFee(request.getTrustFee());
        mail.setOfferedBookName(request.getOfferedBookName());
        mail.setOfferedBookImage(request.getOfferedBookImage());
        return mail;

    }
    public Mail mapShippingMailRequestToMail(ShippingMailReq request) {
        Mail mail = new Mail();
        mail.setTrackingNumber(request.getTrackingNumber());
        return mail;
    }
    public Mail mapTransactionMailRequestToMail(TransactionMailReq request) {
        Mail mail = new Mail();
        //mail.setTrustFee(request.getTrustFee());
        mail.setStatus(request.getStatus());
        mail.setDeadline(request.getDeadline());
        mail.setOfferedBookImage(request.getOfferedBookImage());
        mail.setOfferedBookName(request.getOfferedBookName());
        mail.setListBookImage(request.getListBookImage());
        mail.setListId(request.getListId());
        mail.setListBookName(request.getListBookName());
        return mail;
    }


}
