package org.example.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.request.mail.*;
import org.example.dto.response.ListMailResponse;
import org.example.entity.Mail;
import org.example.entity.User;
import org.example.exception.AuthException;
import org.example.exception.ErrorType;
import org.example.external.ListManager;
import org.example.mapper.MailMapper;
import org.example.repository.MailRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
    @Value("${front-end.url}")
    private String frontendAddress;

    @Value("${front-end.register-mail-template.name}")
    private String registerMailTemplateName;

    @Value("${front-end.register-mail-template.subject}")
    private String registerMailSubject;

    @Value("${front-end.password-rest-mail-template.name}")
    private String resetPasswordMailTemplateName;

    @Value("${front-end.password-rest-mail-template.subject}")
    private String resetPasswordMailSubject;

    @Value("${front-end.update-list-exc-template.name}")
    private String updatelistexctemplate;

    @Value("${front-end.update-list-exc-template.subject}")
    private String updateListexcSubject;

    @Value("${front-end.update-list-sale-template.subject}")
    private String updateListSaleSubject;

    @Value("${front-end.update-list-sale-template.name}")
    private String updateListSale;

    @Value("${front-end.shipping-template.name}")
    private String shippingTemplate;

    @Value("${front-end.shipping-template.subject}")
    private String shippingSubject;

    @Value("${front-end.transaction-created-template.name}")
    private String transactionCreatedTemplate;

    @Value("${front-end.transaction-created-template.subject}")
    private String transactionCreatedSubject;

    @Value("${front-end.transaction-complated-template.name}")
    private String transactionComplatedTemplate;

    @Value("${front-end.transaction-complated-template.subject}")
    private String transactionComplatedSubject;

    @Value("${front-end.offer-accepted-template.name}")
    private String offerAcceptedTemplate;

    @Value("${front-end.offer-accepted-template.subject}")
    private String offerAcceptedSubject;


    private final MailRepository mailRepository;
    private final JavaMailSender javaMailSender;
    private final MailMapper mailMapper;
    private final Configuration freeMarkerConfig;
    private final UserRepository userRepository;
    private final ListManager listManager;




    public void sendRegisterMail(RegisterMailRequest mailRequest) {
        Map<String, Object> variables = new HashMap<>();
        sendAuthMail(mailRequest, variables, registerMailTemplateName, registerMailSubject);
    }



    public void sendForgotMail(ForgotMailRequest mailRequest) {
        Map<String, Object> variables = new HashMap<>();
        sendAuthMail(mailRequest, variables, resetPasswordMailTemplateName, resetPasswordMailSubject);
    }

    public void sendExchangeList(ListMailRequest mailRequest) {
        Map<String, Object> variables = new HashMap<>();
        Mail mail = mailMapper.mapExchangeMailRequestToMail(mailRequest);
        User owner = userRepository.findById(mailRequest.getOwnerId()).orElseThrow(()-> new AuthException(ErrorType.USER_NOT_FOUND));
        mail.setSentToMailAddress(owner.getEmail());
        variables.put("listBookName", mail.getListBookName());
        variables.put("offeredBookName",mail.getOfferedBookName());
        variables.put("listId",mail.getListId());
        variables.put("exchangeLink",createExchangeLink());
        variables.put("listBookImage",mail.getListBookImage());
        variables.put("offeredBookImage",mail.getOfferedBookImage());
        variables.put("offersLink",createOffersLink());
        sendMail(mail, variables, updatelistexctemplate, updateListexcSubject);
    }
    public void sendSaleList(ListMailRequest mailRequest) {
        Map<String, Object> variables = new HashMap<>();
        Mail mail = mailMapper.mapSaleListMailRequestToMail(mailRequest);
        User owner = userRepository.findById(mailRequest.getOwnerId()).orElseThrow(()-> new AuthException(ErrorType.USER_NOT_FOUND));
        User offerer = userRepository.findById(mailRequest.getOwnerId()).orElseThrow(()-> new AuthException(ErrorType.USER_NOT_FOUND));
        mail.setSentToMailAddress(owner.getEmail());
        mail.setOffereraddress(offerer.getAddress());
        mail.setDeadline(mailRequest.getPaymentdeadline());
        mail.setOffererFullName(offerer.getFirstName()+ " " + offerer.getLastName());
        log.info("Sending email to: {}, Address: {}, Name: {}", mail.getSentToMailAddress(), mail.getOffereraddress(), mail.getOffererFullName());
        variables.put("listBookName", mail.getListBookName());
        variables.put("listsLink",createSaleLink());
        variables.put("listId", mail.getListId());
        variables.put("buyerName",mail.getOffererFullName());
        variables.put("buyerAddress",mail.getOffereraddress());
        variables.put("lastShippingDate",mailRequest.getShipmentdeadline());
        variables.put("salesLink",createSaleLink());
        variables.put("listBookImage",mail.getListBookImage());
        variables.put("price",mailRequest.getTrustFee());
        sendMail(mail, variables, updateListSale, updateListSaleSubject);
    }


    public void sendShipping(ShippingMailReq mailRequest) {
        Map<String, Object> variables = new HashMap<>();
        Mail mail = mailMapper.mapShippingMailRequestToMail(mailRequest);
        User owner = userRepository.findById(mailRequest.getSenderId()).orElseThrow(()-> new AuthException(ErrorType.USER_NOT_FOUND));
        User offerer = userRepository.findById(mailRequest.getRecipientId()).orElseThrow(()-> new AuthException(ErrorType.USER_NOT_FOUND));
        ListMailResponse listMailResponse = listManager.mailInfo(mailRequest.getListId()).getBody();
        log.info("List boook {}",listMailResponse);
        mail.setOwnerFullName(owner.getFirstName()+ " " + owner.getLastName());
        mail.setOffereraddress(offerer.getAddress());
        mail.setOffererFullName(offerer.getFirstName()+ " " + offerer.getLastName());
        mail.setOfferedBookImage(listMailResponse.getListBookImage());
        mail.setAuthor(listMailResponse.getAuthor());
        mail.setSentToMailAddress(offerer.getEmail());
        mail.setCategory(listMailResponse.getCategory());
        mail.setPublisher(listMailResponse.getPublisher());
        mail.setPublishedDate(listMailResponse.getPublishedDate());
        mail.setTitle(listMailResponse.getTitle());
        variables.put("listBookName", mail.getTitle());
        variables.put("receiverName",mail.getOffererFullName());
        variables.put("address",mail.getOffereraddress());
        variables.put("trackingNumber",mail.getTrackingNumber());
        variables.put("author",mail.getAuthor());
        variables.put("category",mail.getCategory());
        variables.put("publisher",mail.getPublisher());
        variables.put("publishDate",mail.getPublishedDate());
        variables.put("listBookImage",listMailResponse.getListBookImage());
        sendMail(mail, variables, shippingTemplate, shippingSubject);
    }
    public void sendTransaction(TransactionMailReq mailRequest) {
        log.info("owner mail creating..");
        Map<String, Object> variables = new HashMap<>();
        Mail mail = mailMapper.mapTransactionMailRequestToMail(mailRequest);
        User offerer = userRepository.findById(mailRequest.getOffererId()).orElseThrow(()-> new AuthException(ErrorType.USER_NOT_FOUND));
        User owner = userRepository.findById(mailRequest.getOwnerId()).orElseThrow(()-> new AuthException(ErrorType.USER_NOT_FOUND));
        log.info("owner email :{}",owner.getEmail());
        mail.setOffererFullName(offerer.getFirstName()+ " " + offerer.getLastName());
        mail.setSentToMailAddress(owner.getEmail());
        variables.put("listBookName", mail.getListBookName());
        variables.put("listBookImage", mail.getListBookImage());
        variables.put("offeredBookName", mail.getOfferedBookName());
        variables.put("offeredBookImage", mail.getOfferedBookImage());
        variables.put("buyerName",mail.getOffererFullName());
        variables.put("trustFee",mail.getTrustFee());
        variables.put("lastPaymentDate",mail.getDeadline());
        variables.put("tradesLink",createExchangeLink());
        variables.put("receiverAddress",offerer.getAddress());
        sendMail(mail, variables, transactionCreatedTemplate, transactionCreatedSubject);
    }
    public void sendOffererTransaction(TransactionMailReq mailRequest) {
        log.info("offerer mail creating..");
        Map<String, Object> variables = new HashMap<>();
        Mail mail1 = mailMapper.mapTransactionMailRequestToMail(mailRequest);
        User owner = userRepository.findById(mailRequest.getOwnerId()).orElseThrow(()-> new AuthException(ErrorType.USER_NOT_FOUND));
        User offerer = userRepository.findById(mailRequest.getOffererId()).orElseThrow(()-> new AuthException(ErrorType.USER_NOT_FOUND));
        log.info("offerer email :{}",offerer.getEmail());
        mail1.setOwnerFullName(owner.getFirstName()+ " " + owner.getLastName());
        mail1.setSentToMailAddress(offerer.getEmail());
        variables.put("listBookName", mail1.getListBookName());
        variables.put("listBookImage", mail1.getListBookImage());
        variables.put("offeredBookName", mail1.getOfferedBookName());
        variables.put("offeredBookImage", mail1.getOfferedBookImage());
        variables.put("buyerName",mail1.getOwnerFullName());
        variables.put("trustFee",mail1.getTrustFee());
        variables.put("lastPaymentDate",mail1.getDeadline());
        variables.put("tradesLink",createExchangeLink());
        variables.put("receiverAddress",owner.getAddress());
        sendMail(mail1, variables, transactionCreatedTemplate, transactionCreatedSubject);
    }
    //todo -> burası bitecek
    public void sendTransactionComplated(TransactionMailReq mailRequest) {
        Map<String, Object> variables = new HashMap<>();
        Mail mail = mailMapper.mapTransactionMailRequestToMail(mailRequest);
        ListMailResponse listMailResponse = listManager.mailInfo(mailRequest.getListId()).getBody();
        sendMail(mail, variables, transactionComplatedTemplate, transactionComplatedSubject);
    }

    public void sendAcceptedOffer(ListMailRequest listMailRequest){
        Map<String, Object> variables = new HashMap<>();
        Mail mail = mailMapper.mapExchangeMailRequestToMail(listMailRequest);
        Mail mail1 = mailMapper.mapExchangeMailRequestToMail(listMailRequest);
        User offerer = userRepository.findById(listMailRequest.getOffererId()).orElseThrow(()-> new AuthException(ErrorType.USER_NOT_FOUND));
        User owner = userRepository.findById(listMailRequest.getOwnerId()).orElseThrow(()-> new AuthException(ErrorType.USER_NOT_FOUND));
        variables.put("listBookName", mail.getListBookName());
        variables.put("listId",mail.getListId());
        variables.put("listBookImage", mail.getListBookImage());
        variables.put("offeredBookName", mail.getOfferedBookName());
        variables.put("offeredBookImage", mail.getOfferedBookImage());
        variables.put("trustFee",mail.getTrustFee());
        variables.put("lastPaymentDate",mail.getDeadline());
        variables.put("lastShipmentDate",mail.getDeadline());
        variables.put("offersLink",createOffersLink());
        mail.setSentToMailAddress(owner.getEmail());
        mail1.setSentToMailAddress(offerer.getEmail());
        sendMail(mail, variables, offerAcceptedTemplate, offerAcceptedSubject);
        log.info("owner mail sent to : {}",mail.getSentToMailAddress());
        sendMail(mail1, variables, offerAcceptedTemplate, offerAcceptedSubject);
        log.info("offer mail sent to : {}",mail1.getSentToMailAddress());
    }



    private void sendAuthMail(MailRequest mailRequest, Map<String, Object> variables, String templateName, String subject) {
        Mail mail = mailMapper.mapRegisterMailToMail(mailRequest);
        try {
            MimeMessage message = createMimeMessage(mail.getSentToMailAddress(), variables, templateName, subject);
            javaMailSender.send(message);
            mailRepository.save(mail);
            log.info("Email successfully sent to: {}", mail.getSentToMailAddress());
        } catch (MessagingException | IOException | TemplateException | MailSendException e) {
            log.error("Error while sending mail: {}", e.getMessage());
            throw new AuthException(ErrorType.MAIL_COULD_NOT_SENT);
        }
    }

    private void sendMail(Mail mail, Map<String, Object> variables, String templateName, String subject) {
        log.info("recieved mail: {}",mail);
        try {
            MimeMessage message = createMimeMessage(mail.getSentToMailAddress(), variables, templateName, subject);
            javaMailSender.send(message);
            mailRepository.save(mail);
            log.info("Email successfully sent to: {}", mail.getSentToMailAddress());
        } catch (MessagingException | IOException | TemplateException | MailSendException e) {
            log.error("Error while sending mail: {}", e.getMessage());
            throw new AuthException(ErrorType.MAIL_COULD_NOT_SENT);
        }
    }


    private MimeMessage createMimeMessage(String recipient, Map<String, Object> variables, String templateName, String subject) throws MessagingException, IOException, TemplateException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(recipient);
        helper.setSubject(subject);
        helper.setText(generateMailContent(variables, templateName), true);

        return message;
    }

    private String generateMailContent(Map<String, Object> variables, String templateFileName) throws IOException, TemplateException {
        Template template = freeMarkerConfig.getTemplate(templateFileName);
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, variables);
    }

    private String createOffersLink() {
        return frontendAddress + "/myOffers";
    }

    private String createTradeLink() {
        return frontendAddress + "/trade";
    }

    private String createSaleLink(){
        return frontendAddress + "/mySales";
    }
    private String createExchangeLink(){
        return frontendAddress + "/myTrades";
    }
}
