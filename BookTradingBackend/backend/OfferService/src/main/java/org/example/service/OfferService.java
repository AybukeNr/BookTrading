package org.example.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Offer;
import org.example.entity.enums.OfferStatus;
import org.example.dto.request.CreateOfferRequest;
import org.example.dto.request.UpdateOfferRequest;
import org.example.dto.response.OfferBookResponse;
import org.example.dto.response.ListResponse;
import org.example.dto.response.SentOffer;
import org.example.exception.ErrorType;
import org.example.exception.OfferException;
import org.example.external.BookManager;
import org.example.external.ListManager;
import org.example.repository.OfferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OfferService {
    private final OfferRepository offerRepository;
    private final ListManager listManager;
    private final BookManager bookManager;

    @Transactional
    public Offer createOffer(CreateOfferRequest offer) {
        // İlgili listeyi ve kitabı servislerden al
        ListResponse offerList = getList(offer.getListingId());
        OfferBookResponse offeredBook = getBook(offer.getOfferedBookId());

        // Yeni bir teklif oluştur ve kaydet
        Offer newOffer = offerRepository.save(
                Offer.builder()
                        .offeredListId(offerList.getListid())
                        .offeredBookId(offer.getOfferedBookId())
                        .offerList(offerList)
                        .offeredBook(offeredBook)
                        .offerStatus(OfferStatus.GONDERILDI)
                        .offererId(offer.getOffererId())
                        .createdDate(LocalDateTime.now())
                        .build()
        );

        // Teklifi ilana gönder
        listManager.takeOffer(
                SentOffer.builder()
                        .offerId(newOffer.getId())
                        .offererId(newOffer.getOffererId())
                        .offerListId(offer.getListingId())
                        .offeredBookId(offer.getOfferedBookId())
                        .offeredBook(offeredBook)
                        .offerStatus(OfferStatus.ALINDI)
                        .createdDate(LocalDateTime.now())
                        .build()
        );

        return newOffer;
    }

    private ListResponse getList(String listingId) {
        return Optional.ofNullable(listManager.getListById(listingId).getBody())
                .orElseThrow(() -> new OfferException(ErrorType.LIST_NOT_FOUND));
    }

    private OfferBookResponse getBook(Long bookId) {
        return Optional.ofNullable(bookManager.findById(bookId).getBody())
                .orElseThrow(() -> new OfferException(ErrorType.BOOK_NOT_FOUND));
    }

    @Transactional//todo -> kaubl ve ret sadece ilan sahibi,iptal edildi ise sadece teklif sahibi tarafından gerçekleştirilmeli
    public Offer updateOffer(UpdateOfferRequest updateOfferRequest) {
        Offer offer = offerRepository.findByOffererIdAndId(updateOfferRequest.getOffererId(), updateOfferRequest.getOfferId())
                .orElseThrow(() -> new OfferException(ErrorType.OFFER_NOT_FOUND));

        log.info("Updating offer: {} {}", offer,updateOfferRequest.getOfferStatus());

        offer.setOfferStatus(OfferStatus.valueOf(updateOfferRequest.getOfferStatus()));
        offer.setUpdatedDate(LocalDateTime.now());

        log.info("Updating offer: {}", updateOfferRequest);
        Offer updatedOffer = offerRepository.save(offer);
        listManager.updateOffer(updateOfferRequest);

        log.info("Offer updated successfully: {}", updatedOffer);

        return updatedOffer;
    }

    public List<Offer> getUsersOffers(String userId){
        return offerRepository.findAllByOffererId(userId).orElse(new ArrayList<>());
    }


    public List<Offer> getAllOffers() {
        List<Offer> offers = offerRepository.findAll();
        return offers;
    }
    public Offer getOfferById(String id) {
        return offerRepository.findById(id).orElseThrow(() -> new OfferException(ErrorType.OFFER_NOT_FOUND));
    }


    }





