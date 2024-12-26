package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Lists;
import org.example.dto.request.ListRequest;
import org.example.dto.request.UpdateOfferRequest;
import org.example.dto.response.ListResponse;
import org.example.dto.response.OfferStatus;
import org.example.dto.response.SentOffer;
import org.example.exception.ErrorType;
import org.example.exception.ListException;
import org.example.mapper.ListsMapper;
import org.example.repository.ListsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class ListsService {
    private final ListsRepository listsRepository;
    private final ListsMapper listMapper;

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

    //todo:atomic operations
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
    @Transactional//todo -> concurrency
    public Boolean updateOffer(UpdateOfferRequest updateOfferRequest) {
        Lists list = listsRepository.findById(updateOfferRequest.getListingId())
                .orElseThrow(() -> new ListException(ErrorType.LIST_NOT_FOUND));

        SentOffer targetOffer = list.getOffers().stream()
                .filter(offer -> offer.getOfferId().equals(updateOfferRequest.getOfferId())
                        && offer.getOffererId().equals(updateOfferRequest.getOffererId()))
                .findFirst()
                .orElseThrow(() -> new ListException(ErrorType.OFFER_NOT_FOUND));

        targetOffer.setOfferStatus(OfferStatus.valueOf(updateOfferRequest.getOfferStatus()));
        targetOffer.setUpdatedDate(LocalDateTime.now());

        listsRepository.save(list);
        log.info("Update List after update: {} {}", targetOffer.getOfferListId(), targetOffer.getOfferStatus());

        return true; //
    }


    




}
