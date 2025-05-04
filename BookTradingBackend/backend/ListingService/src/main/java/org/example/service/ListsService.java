package org.example.service;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.request.*;
import org.example.dto.request.mail.ListMailRequest;
import org.example.dto.request.mail.TransactionMailReq;
import org.example.dto.response.*;
import org.example.entity.Lists;
import org.example.entity.enums.ListsStatus;
import org.example.exception.ErrorType;
import org.example.exception.ListException;
import org.example.external.*;
import org.example.mapper.ListsMapper;
import org.example.repository.ListsRepository;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class ListsService {
    private final ListsRepository listsRepository;
    private final ListsMapper listMapper;
    private final TransactionManager transactionManager;
    private final ShippingManager shippingManager;
    private final UserManager userManager;
    private final BookManager bookManager;
    private final OfferManager offerManager;
    private final MailManager mailManager;



    public ListMailResponse listMailSummary(String listid){
        Lists lists = listsRepository.findById(listid).orElseThrow(() -> new ListException(ErrorType.LIST_NOT_FOUND));
        ListMailResponse mailResponse = listMapper.ListToListMailResponse(lists);
        return mailResponse;
    }

    public List<ListResponse> getAllLists() {
        return listsRepository.findAll().stream().map(listMapper::ListToListResponse).toList();
    }
    @Transactional
    public Boolean deleteList(String listId) {
        Lists list = listsRepository.findById(listId)
                .orElseThrow(() -> new ListException(ErrorType.LIST_NOT_FOUND));

        if (list.getBookInfo() != null && list.getBookInfo().getId() != null) {
            UpdateBookStat updateBookStat = UpdateBookStat.builder()
                    .bookId(list.getBookInfo().getId()) //
                    .status("ENABLED") //
                    .build();

            bookManager.updateBookStat(updateBookStat);
            log.info("Book status updated for bookId: {}", list.getBookInfo().getId());
        } else {
            log.warn("BookInfo or bookId is null for listId: {}, skipping book status update.", listId);
        }
        // İlanı sil
        listsRepository.deleteById(listId);
        log.info("List deleted successfully: {}", listId);
        return true;
    }
    //kitabı silerken kitaba ait tüm ilanları silen metot
    @Transactional
    public void deleteAllListsByBookId(Long bookId) {
        List<Lists> lists = listsRepository.findAll()
                .stream()
                .filter(list ->
                        list.getBookInfo() != null &&
                                list.getBookInfo().getId() != null &&
                                list.getBookInfo().getId().equals(bookId)
                )
                .collect(Collectors.toList());

        for (Lists list : lists) {
            listsRepository.deleteById(list.getId());
            log.info("Deleted list with ID: {} for bookId: {}", list.getId(), bookId);
        }

        log.info("Total deleted lists for bookId {}: {}", bookId, lists.size());
    }

    @Transactional
    public void updateBookInfoInLists(ListBookResponse updatedBookInfo) {
        List<Lists> listsWithBook = listsRepository.findAll().stream()
                .filter(list -> list.getBookInfo() != null &&
                        updatedBookInfo.getId().equals(list.getBookInfo().getId()))
                .collect(Collectors.toList());

        for (Lists list : listsWithBook) {
            list.setBookInfo(updatedBookInfo);
            listsRepository.save(list);
            log.info("Updated bookInfo in list: {}", list.getId());
        }

        log.info("Updated bookInfo in {} lists for bookId: {}", listsWithBook.size(), updatedBookInfo.getId());
    }

    public ListResponse getListById(String id) {
        Lists lists = listsRepository.findById(id).orElseThrow(() -> new ListException(ErrorType.LIST_NOT_FOUND));
        log.info("Recieved List: {}", lists);
        log.info("Recieved Book: {}", lists.getBookInfo());
        ListResponse listResponse = listMapper.ListToListResponse(lists);
        log.info("Recieved List: {}", listResponse);
        log.info("Recieved Book : {}", listResponse.getBook());
        return listResponse;
    }
    public ListResponse getListByIdForPayment(String id) {
        Lists lists = listsRepository.findById(id).orElseThrow(() -> new ListException(ErrorType.LIST_NOT_FOUND));
        log.info("Recieved List: {}", lists);
        log.info("Recieved Book: {}", lists.getBookInfo());
        ListResponse listResponse = listMapper.ListToListResponse(lists);
        log.info("Recieved List: {}", listResponse);
        log.info("Recieved Book : {}", listResponse.getBook());
        return listResponse;
    }

    public List<ListResponse> getListsByOwnerId(String ownerId) {
        List<Lists> lists = listsRepository.findAllByOwnerId(ownerId)
                .orElseThrow(() -> new ListException(ErrorType.INTERNAL_SERVER_ERROR));

        return lists.stream()
                .map(listMapper::ListToListResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ListResponse createLists(ListRequest lists) {
        Lists newLists = listMapper.ListRequestToList(lists);
        log.info("Create List {}", newLists);
        return listMapper.ListToListResponse(listsRepository.save(newLists));
    }

    //todo:bir veya birden fazla ilan aynı anda teklif alabilir
    @Transactional
    public ListResponse takeOffers(SentOffer offer) {
        log.info("Recieved offer {} :",offer);
        Lists lists = listsRepository.findById(offer.getOfferListId())
                .orElseThrow(() -> new ListException(ErrorType.LIST_NOT_FOUND));
        if (lists.getOffers() == null) {
            lists.setOffers(new ArrayList<>());
        }
        offer.setOfferStatus(OfferStatus.ALINDI);
        lists.getOffers().add(offer);
        Lists updatedLists = listsRepository.save(lists);
        log.info("Take List after update: {}", updatedLists);
        ListMailRequest request = new ListMailRequest();
        request.setOwnerId(lists.getOwnerId());
        request.setOffererId(offer.getOffererId());
        request.setListId(offer.getOfferListId());
        request.setOfferedBookName(offer.getOfferedBook().getTitle());
        request.setOfferedBookImage(offer.getOfferedBook().getImage());
        request.setListBookName(lists.getBookInfo().getTitle());
        request.setListBookImage(lists.getBookInfo().getImage());
        mailManager.testExchangeListUpdated(request);
        return listMapper.ListToListResponse(updatedLists);
    }

    //kullanıcıya ait olan ilanların tekliflerini görüntüler
    public List<SentOffer> getRecievedOffers(String userId) {
        List<Lists> userLists = listsRepository.findAllByOwnerId(userId).orElseThrow(() -> new ListException(ErrorType.LIST_NOT_FOUND));

        return userLists.stream()
                .filter(list -> list.getOffers() != null)
                .flatMap(list -> list.getOffers().stream())
                .collect(Collectors.toList());

    }

    @Transactional
    public void updateOffer(UpdateOfferRequest updateOfferRequest) {
        // Listeyi al
        Lists list = listsRepository.findById(updateOfferRequest.getListingId())
                .orElseThrow(() -> new ListException(ErrorType.LIST_NOT_FOUND));//sadece ilgili ilanı bulması için refactor

        if (list.getOffers() == null) {
            throw new ListException(ErrorType.OFFER_NOT_FOUND); //
        }

        List<SentOffer> offers = list.getOffers();
        SentOffer targetOffer = offers.stream().filter(sentOffer -> sentOffer.getOfferId().equals(updateOfferRequest.getOfferId())).findFirst()
                .orElseThrow(() -> new ListException(ErrorType.OFFER_NOT_FOUND));

        targetOffer.setOfferStatus(OfferStatus.valueOf(updateOfferRequest.getOfferStatus()));
        targetOffer.setUpdatedDate(LocalDateTime.now());
        listsRepository.save(list);

        if (updateOfferRequest.getOfferStatus().equals(String.valueOf(OfferStatus.KABUL) )) {
            processAcceptedOffer(list, targetOffer);
            UpdateBookStat updateBookStat = UpdateBookStat.builder()
                            .bookId(targetOffer.getOfferedBookId())
                                    .status("DISABLED").build();
            bookManager.updateBookStat(updateBookStat);

            log.info("Update list mail sent");
        }

        log.info("Offer updated successfully: Offer ID = {}, Status = {}", targetOffer.getOfferId(), targetOffer.getOfferStatus());

    }

    public Boolean processSales(SalesRequest salesRequest){
        Lists list = listsRepository.findById(salesRequest.getListId()).orElseThrow(() -> new ListException(ErrorType.LIST_NOT_FOUND));
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .bookId(list.getBookInfo().getId())
                .listId(list.getId())
                .listType(list.getType())
                .ownerId(list.getOwnerId())
                .offererId(salesRequest.getOffererId())
                .listType(list.getType())
                .build();
        TransactionResponse response = transactionManager.createTransaction(transactionRequest).getBody();
        log.info("Transaction created {}",response);
        Map<String, String> addresses =  userManager.getAddresses(list.getOwnerId(), salesRequest.getOffererId());
        // Shipping oluştur
        CreateShippingRequest shippingRequest = CreateShippingRequest.builder()
                .listId(list.getId())
                .listType(list.getType())
                .ownerId(list.getOwnerId())
                .offererId(salesRequest.getOffererId())
                .transactionId(response.getTransactionId())
                .offererAddress(addresses.get("offererAddress")) // Offerer'ın adresi
                .build();
        shippingManager.createShipping(shippingRequest);
        ListMailRequest mailRequest = new ListMailRequest();
        mailRequest.setListId(list.getId());
        mailRequest.setListBookName(list.getBookInfo().getTitle());
        mailRequest.setListBookImage(list.getBookInfo().getImage());
        mailRequest.setOwnerId(list.getOwnerId());
        mailRequest.setOffererId(salesRequest.getOffererId());
        mailRequest.setShipmentdeadline(LocalDateTime.now().plusMinutes(5));
        mailManager.testSaleMail(mailRequest);
        return true;

    }

    private void processAcceptedOffer(Lists list, SentOffer targetOffer) {
        Map<String, String> addresses =  userManager.getAddresses(list.getOwnerId(), targetOffer.getOffererId());
        // Transaction oluştur
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .bookId(list.getBookInfo().getId())
                .listId(list.getId())
                .listType(list.getType())
                .ownerId(list.getOwnerId())
                .offererId(targetOffer.getOffererId())
                .price(list.getPrice())
                .build();
        TransactionResponse response = transactionManager.createTransaction(transactionRequest).getBody();
        log.info("Transaction created {}",response);

        // Shipping oluştur
        CreateShippingRequest shippingRequest = CreateShippingRequest.builder()
                .listId(list.getId())
                .listType(list.getType())
                .ownerId(list.getOwnerId())
                .offererId(targetOffer.getOffererId())
                .transactionId(response.getTransactionId())
                .ownerAddress(addresses.get("ownerAddress")) // Owner'ın adresi
                .offererAddress(addresses.get("offererAddress")) // Offerer'ın adresi
                .build();
        shippingManager.createShipping(shippingRequest);
        TransactionMailReq transactionMailReq = new TransactionMailReq();
        transactionMailReq.setListId(list.getId());
        transactionMailReq.setOwnerId(list.getOwnerId());
        transactionMailReq.setOffererId(targetOffer.getOffererId());
        transactionMailReq.setStatus(String.valueOf(response.getStatus()));
        transactionMailReq.setListBookImage(list.getBookInfo().getImage());
        transactionMailReq.setListBookName(list.getBookInfo().getTitle());
        transactionMailReq.setOfferedBookName(targetOffer.getOfferedBook().getTitle());
        transactionMailReq.setOfferedBookImage(targetOffer.getOfferedBook().getImage());
        transactionMailReq.setDeadline(LocalDateTime.now().plusMinutes(5));
        mailManager.testTransactionCreated(transactionMailReq);
        //todo -> trustfee set edilecek,transaction serviste responsa ekle,deadline da
        log.info("Shipping created successfully for transaction: {}", response.getTransactionId());
        ListMailRequest mailRequest = new ListMailRequest();
        mailRequest.setListId(list.getId());
        mailRequest.setListBookImage(list.getBookInfo().getImage());
        mailRequest.setListBookName(list.getBookInfo().getTitle());
        mailRequest.setOwnerId(list.getOwnerId());
        mailRequest.setOffererId(targetOffer.getOffererId());
        mailRequest.setOfferedBookImage(targetOffer.getOfferedBook().getImage());
        mailRequest.setOfferedBookName(targetOffer.getOfferedBook().getTitle());
        mailRequest.setShipmentdeadline(LocalDateTime.now().plusMinutes(5));
        mailRequest.setPaymentdeadline(LocalDateTime.now().plusMinutes(5));
        mailManager.testAcceptedOffer(mailRequest);
        log.info("Accepted Offer Mail sent");

    }



    @Transactional
    public Boolean updateListStatus(UpdateListReq listReq) {
        Lists list = listsRepository.findById(listReq.getListingId()).orElseThrow(() -> new ListException(ErrorType.LIST_NOT_FOUND));
        list.setStatus(ListsStatus.valueOf(listReq.getListStatus()));
        listsRepository.save(list);
        log.info("Update List after update: {} {}", listReq.getListingId(), list.getStatus());
        return true;
    }

    private void sendTransactionCreated(TransactionMailReq mailRequest) {
        mailManager.testTransactionCreated(mailRequest);

    }

    private void sendSaleMail(ListMailRequest mailRequest){
        mailManager.testSaleMail(mailRequest);
    }

    @Transactional
    public Boolean updateListApprovalStatus(String listId) {
        Lists list = listsRepository.findById(listId)
                .orElseThrow(() -> new ListException(ErrorType.LIST_NOT_FOUND));
        // Eğer şu an OPEN ise CLOSE yap, CLOSE ise OPEN yap
        list.setStatus(list.getStatus() == ListsStatus.OPEN ? ListsStatus.CLOSED : ListsStatus.OPEN);
        listsRepository.save(list);
        log.info("Updated List Status: {} -> {}", listId, list.getStatus());
        return true;
    }
    //81

    public Double getListPrice(String listId){
        String priceStr = listsRepository.findPriceById(listId);
        return priceStr != null ? Double.valueOf(priceStr) : 0.0;
    }

    public OfferListResponse getOfferListById(String listId){
        Lists list = listsRepository.findById(listId)
                .orElseThrow(() -> new ListException(ErrorType.LIST_NOT_FOUND));
        return listMapper.ListToOfferListResponse(list);
    }
}
