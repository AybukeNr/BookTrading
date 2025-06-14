package org.example.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.request.UpdateBookStat;
import org.example.dto.response.UserResponse;
import org.example.entity.Offer;
import org.example.entity.enums.OfferStatus;
import org.example.dto.request.CreateOfferRequest;
import org.example.dto.request.UpdateOfferRequest;
import org.example.dto.response.OfferBookResponse;
import org.example.dto.response.OfferListResponse;
import org.example.dto.response.SentOffer;
import org.example.exception.ErrorType;
import org.example.exception.OfferException;
import org.example.external.BookManager;
import org.example.external.ListManager;
import org.example.external.UserManager;
import org.example.mapper.OfferMapper;
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
    private final UserManager userManager;
    private final OfferMapper offerMapper;

    @Transactional
    public Offer createOffer(CreateOfferRequest offer) {
        // İlgili listeyi ve kitabı servislerden al
        OfferListResponse offerList = getList(offer.getListingId());
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

       SentOffer sentOffer =  SentOffer.builder()
                .offerId(newOffer.getId())
                .offererId(newOffer.getOffererId())
                .offerListId(offer.getListingId())
                .offeredBookId(offer.getOfferedBookId())
                .offeredBook(offeredBook)
                .offerStatus(OfferStatus.ALINDI)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        listManager.takeOffer(sentOffer);

        bookManager.updateBookStat(UpdateBookStat.builder()
                .bookId(offer.getOfferedBookId())
                .status("DISABLED").build());
        return newOffer;
    }

    private OfferListResponse getList(String listingId) {
        return Optional.ofNullable(listManager.getOfferListById(listingId).getBody())
                .orElseThrow(() -> new OfferException(ErrorType.LIST_NOT_FOUND));
    }

    private OfferBookResponse getBook(Long bookId) {
        return Optional.ofNullable(bookManager.getOfferBookById(bookId).getBody())
                .orElseThrow(() -> new OfferException(ErrorType.BOOK_NOT_FOUND));
    }
    //todo -> kaubl ve ret sadece ilan sahibi,iptal edildi ise sadece teklif sahibi tarafından gerçekleştirilmeli
    @Transactional
    public Boolean updateOffer(UpdateOfferRequest updateOfferRequest) {
        Offer offer = offerRepository.findByOffererIdAndId(updateOfferRequest.getOffererId(), updateOfferRequest.getOfferId())
                .orElseThrow(() -> new OfferException(ErrorType.OFFER_NOT_FOUND));

        log.info("Updating offer: {} {}", offer,updateOfferRequest.getOfferStatus());

        offer.setOfferStatus(OfferStatus.valueOf(updateOfferRequest.getOfferStatus()));
        offer.setUpdatedDate(LocalDateTime.now());

        log.info("Updating offer: {}", updateOfferRequest);
        Offer updatedOffer = offerRepository.save(offer);
        listManager.updateOffer(updateOfferRequest);

        log.info("Offer updated successfully: {}", updatedOffer);

        return true;
    }

    public List<Offer> getUsersOffers(String userId) {
        List<Offer> offers = offerRepository.findAllByOffererId(userId).orElse(new ArrayList<>());

        for (Offer offer : offers) {
            if (offer.getOfferList() != null && offer.getOfferList().getOwnerId() != null) {
                try {
                    UserResponse user = userManager.getUserResponseById(offer.getOfferList().getOwnerId());
                    offer.getOfferList().setOwner(user);
                } catch (Exception e) {
                    // Feign call failed, log or handle as needed
                    System.out.println("User bilgisi çekilemedi: " + e.getMessage());
                }
            }
        }

        return offers;
    }

    public List<Offer> getAcceptedOffers(String userId){
        List<Offer> acceptedOffers = offerRepository.findAcceptedOffersByUserId(userId);
        return acceptedOffers;
    }

    public List<Offer> getAllOffers() {
        List<Offer> offers = offerRepository.findAll();
        return offers;
    }
    public Offer getOfferById(String id) {
        return offerRepository.findById(id).orElseThrow(() -> new OfferException(ErrorType.OFFER_NOT_FOUND));
    }



    }





