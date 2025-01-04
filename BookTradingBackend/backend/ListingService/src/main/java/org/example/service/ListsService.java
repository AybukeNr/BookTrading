package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.request.*;
import org.example.dto.response.TransactionResponse;
import org.example.entity.Lists;
import org.example.dto.response.ListResponse;
import org.example.dto.response.OfferStatus;
import org.example.dto.response.SentOffer;
import org.example.entity.enums.ListsStatus;
import org.example.exception.ErrorType;
import org.example.exception.ListException;
import org.example.external.BookManager;
import org.example.external.ShippingManager;
import org.example.external.TransactionManager;
import org.example.external.UserManager;
import org.example.mapper.ListsMapper;
import org.example.repository.ListsRepository;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

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

    public List<ListResponse> getAllLists() {
        return listsRepository.findAll().stream().map(listMapper::ListToListResponse).toList();
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
    public Boolean updateOffer(UpdateOfferRequest updateOfferRequest) {
        // Listeyi al
        Lists list = listsRepository.findById(updateOfferRequest.getListingId())
                .orElseThrow(() -> new ListException(ErrorType.LIST_NOT_FOUND));//sadece ilgili ilanı bulması için refactor

        // Teklifi bul
        SentOffer targetOffer = list.getOffers().stream()
                .filter(offer -> offer.getOfferId().equals(updateOfferRequest.getOfferId())
                        && offer.getOffererId().equals(updateOfferRequest.getOffererId()))
                .findFirst()
                .orElseThrow(() -> new ListException(ErrorType.OFFER_NOT_FOUND));

        // Teklif durumu güncelle
        targetOffer.setOfferStatus(OfferStatus.valueOf(updateOfferRequest.getOfferStatus()));
        targetOffer.setUpdatedDate(LocalDateTime.now());
        listsRepository.save(list);

        // Eğer teklif kabul edildiyse
        if (updateOfferRequest.getOfferStatus().equals(String.valueOf(OfferStatus.KABUL) )) {
            processAcceptedOffer(list, targetOffer);
            UpdateBookStat updateBookStat = UpdateBookStat.builder()
                            .bookId(targetOffer.getOfferedBookId())
                                    .status("DISABLED").build();
            bookManager.updateBookStat(updateBookStat);
        }

        log.info("Offer updated successfully: Offer ID = {}, Status = {}", targetOffer.getOfferId(), targetOffer.getOfferStatus());
        return true;
    }
    public Boolean processSales(SalesRequest salesRequest){
        Lists list = listsRepository.findById(salesRequest.getListId()).orElseThrow(() -> new ListException(ErrorType.LIST_NOT_FOUND));
        TransactionRequest transactionRequest = TransactionRequest.builder()
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
                .ownerAddress(addresses.get("ownerAddress")) // Owner'ın adresi
                .offererAddress(addresses.get("offererAddress")) // Offerer'ın adresi
                .build();
        shippingManager.createShipping(shippingRequest);

        return true;

    }

    private void processAcceptedOffer(Lists list, SentOffer targetOffer) {
        // Kullanıcı adreslerini al
        Map<String, String> addresses =  userManager.getAddresses(list.getOwnerId(), targetOffer.getOffererId());

        // Transaction oluştur
        TransactionRequest transactionRequest = TransactionRequest.builder()
                .listId(list.getId())
                .listType(list.getType())
                .ownerId(list.getOwnerId())
                .offererId(targetOffer.getOffererId())
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

        log.info("Shipping created successfully for transaction: {}", response.getTransactionId());
    }



    @Transactional
    public Boolean updateListStatus(UpdateListReq listReq) {
        Lists list = listsRepository.findById(listReq.getListingId()).orElseThrow(() -> new ListException(ErrorType.LIST_NOT_FOUND));
        list.setStatus(ListsStatus.valueOf(listReq.getListStatus()));
        listsRepository.save(list);
        log.info("Update List after update: {} {}", listReq.getListingId(), list.getStatus());
        return true;
    }


    




}
