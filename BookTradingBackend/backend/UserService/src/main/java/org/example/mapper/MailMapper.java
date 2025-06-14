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
        if (request.getSentToMailAddress() != null) {
            mail.setSentToMailAddress(request.getSentToMailAddress());
        }
        return mail;
    }

    public Mail mapSaleListMailRequestToMail(ListMailRequest request) {
        Mail mail = new Mail();
        if (request.getListId() != null) {
            mail.setListId(request.getListId());
        }
        if (request.getListBookName() != null) {
            mail.setListBookName(request.getListBookName());
        }
        if (request.getListBookImage() != null) {
            mail.setListBookImage(request.getListBookImage());
        }
        if (request.getShipmentdeadline() != null) {
            mail.setDeadline(request.getShipmentdeadline());
        }
        return mail;
    }

    public Mail mapExchangeMailRequestToMail(ListMailRequest request) {
        Mail mail = new Mail();
        if (request.getListId() != null) {
            mail.setListId(request.getListId());
        }
        if (request.getListBookName() != null) {
            mail.setListBookName(request.getListBookName());
        }
        if (request.getListBookImage() != null) {
            mail.setListBookImage(request.getListBookImage());
        }
        if (request.getShipmentdeadline() != null) {
            mail.setDeadline(request.getShipmentdeadline());
        }
        if (request.getTrustFee() != null) {
            mail.setTrustFee(request.getTrustFee());
        }
        if (request.getOfferedBookName() != null) {
            mail.setOfferedBookName(request.getOfferedBookName());
        }
        if (request.getOfferedBookImage() != null) {
            mail.setOfferedBookImage(request.getOfferedBookImage());
        }
        return mail;
    }

    public Mail mapShippingMailRequestToMail(ShippingMailReq request) {
        Mail mail = new Mail();
        if (request.getTrackingNumber() != null) {
            mail.setTrackingNumber(request.getTrackingNumber());
        }
        return mail;
    }

    public Mail mapTransactionMailRequestToMail(TransactionMailReq request) {
        Mail mail = new Mail();
        if (request.getTrustFee() != null) {
            mail.setTrustFee(request.getTrustFee());
        }
        if (request.getStatus() != null) {
            mail.setStatus(request.getStatus());
        }
        if (request.getDeadline() != null) {
            mail.setDeadline(request.getDeadline());
        }
        if (request.getOfferedBookImage() != null) {
            mail.setOfferedBookImage(request.getOfferedBookImage());
        }
        if (request.getOfferedBookName() != null) {
            mail.setOfferedBookName(request.getOfferedBookName());
        }
        if (request.getListBookImage() != null) {
            mail.setListBookImage(request.getListBookImage());
        }
        if (request.getListId() != null) {
            mail.setListId(request.getListId());
        }
        if (request.getListBookName() != null) {
            mail.setListBookName(request.getListBookName());
        }
        return mail;
    }
}

