package org.example.service;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.request.*;
import org.example.dto.request.mail.ListMailRequest;
import org.example.dto.request.mail.TransactionMailReq;
import org.example.dto.response.*;
import org.example.dto.response.offer.OfferBookResponse;
import org.example.dto.response.offer.OfferListResponse;
import org.example.dto.response.offer.OfferStatus;
import org.example.dto.response.offer.SentOffer;
import org.example.dto.response.transaciton.TransactionResponse;
import org.example.entity.Lists;
import org.example.entity.enums.ListsStatus;
import org.example.exception.ErrorType;
import org.example.exception.ListException;
import org.example.external.*;
import org.example.mapper.ListsMapper;
import org.example.repository.ListsRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
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
    private final RecommendationManager recommendationManager;

    @Value("${recommendation.url}")
    private String recommendationServiceUrl;


    public ListMailResponse listMailSummary(String listid){
        Lists lists = listsRepository.findById(listid).orElseThrow(() -> new ListException(ErrorType.LIST_NOT_FOUND));
        ListMailResponse mailResponse = listMapper.ListToListMailResponse(lists);
        return mailResponse;
    }

    public List<ListResponse> getAllLists() {
        return listsRepository.findAll().stream().map(listMapper::ListToListResponse).toList();
    }
    public List<ListResponseN> getListsExcludingOwner(String ownerId) {
        List<Lists> lists = listsRepository.findByOwnerIdNot(ownerId);

        return lists.stream()
                .filter(list ->
                        list.getStatus() != ListsStatus.AWAITING_SHIPMENT &&
                                list.getStatus() != ListsStatus.SUSPENDED)
                .map(list -> {
                    // Kullanıcı bilgisi alınır
                    UserResponse userResponse = userManager.getUserResponseById(list.getOwnerId());

                    // Kitap bilgisi doğrudan alınıyor
                    ListBookResponse bookResponse = list.getBookInfo();

                    // ListResponseN oluşturuluyor
                    return ListResponseN.builder()
                            .listId(list.getId())
                            .book(bookResponse)
                            .status(list.getStatus())
                            .type(list.getType())
                            .price(list.getPrice())
                            .user(userResponse)
                            .build();
                })
                .toList();
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

    public ListResponseN getListById(String id) {
        // Retrieve list by ID
        Lists lists = listsRepository.findById(id)
                .orElseThrow(() -> new ListException(ErrorType.LIST_NOT_FOUND));

        log.info("Received List: {}", lists);
        log.info("Received Book: {}", lists.getBookInfo());

        // Fetch user details from user service using the Feign client
        UserResponse userResponse = userManager.getUserResponseById(lists.getOwnerId());

        log.info("Received User: {}", userResponse);

        // Map List to ListResponseN
        ListResponseN listResponseN = ListResponseN.builder()
                .listId(lists.getId())
                .book(lists.getBookInfo()) // assuming the ListBookResponse is embedded in Lists
                .status(lists.getStatus())
                .type(lists.getType())
                .price(lists.getPrice())
                .user(userResponse) // Set the user details
                .build();

        log.info("Mapped ListResponseN: {}", listResponseN);

        return listResponseN;
    }
    public void updateBookInfo(BookUpdateRequest request) {
        List<Lists> lists = listsRepository.findByBookInfo_Id(request.getId());
        for (Lists list : lists) {
            ListBookResponse bookInfo = list.getBookInfo();
            if (bookInfo != null) {
                bookInfo.setTitle(request.getTitle());
                bookInfo.setAuthor(request.getAuthor());
                bookInfo.setIsbn(request.getIsbn());
                bookInfo.setPublisher(request.getPublisher());
                bookInfo.setPublishedDate(request.getPublishedDate());
                bookInfo.setImage(request.getImage());
                bookInfo.setCategory(request.getCategory());
                bookInfo.setDescription(request.getDescription());
                bookInfo.setCondition(request.getCondition());
            }
        }
        listsRepository.saveAll(lists);
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
        log.info("Offer creation mail sent:{}", request);
        return listMapper.ListToListResponse(updatedLists);
    }
    private OfferBookResponse mapToOfferBookResponse(ListBookResponse book) {
        if (book == null) return null;

        return OfferBookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .publisher(book.getPublisher())
                .publishedDate(book.getPublishedDate())
                .category(String.valueOf(book.getCategory()))
                .description(book.getDescription())
                .image(book.getImage())
                .condition(book.getCondition())
                .build();
    }


    //kullanıcıya ait olan ilanların tekliflerini görüntüler
    public List<SentOffer> getRecievedOffers(String userId) {
        List<Lists> userLists = listsRepository.findAllByOwnerId(userId)
                .orElseThrow(() -> new ListException(ErrorType.LIST_NOT_FOUND));

        return userLists.stream()
                .filter(list -> list.getOffers() != null)
                .flatMap(list -> list.getOffers().stream().map(offer -> {
                    UserResponse offererUser = null;
                    try {
                        offererUser = userManager.getUserResponseById(offer.getOffererId());
                    } catch (Exception e) {
                        System.out.println("Kullanıcı bilgisi çekilemedi: " + offer.getOffererId());
                    }

                    return SentOffer.builder()
                            .offerId(offer.getOfferId())
                            .offererId(offer.getOffererId())
                            .offerer(offererUser) // kullanıcı bilgisi eklendi
                            .offerListId(list.getId())
                            .offerList(OfferListResponse.builder()
                                    .listid(list.getId())
                                    .ownerId(list.getOwnerId())
                                    .book(mapToOfferBookResponse(list.getBookInfo()))
                                    .build())
                            .offeredBookId(offer.getOfferedBookId())
                            .offeredBook(offer.getOfferedBook())
                            .offerStatus(offer.getOfferStatus())
                            .createdDate(offer.getCreatedDate())
                            .updatedDate(offer.getUpdatedDate())
                            .build();
                }))
                .collect(Collectors.toList());
    }

    //ilanın kabul edilen teklifinin kitabını ve ilandaki kitabı çeker
    public ExchangeDetails getExchangeBooks(String listId) {
        ExchangeDetails exchangeDetails = new ExchangeDetails();
         Lists list = listsRepository.findById(listId)
                .orElseThrow(() -> new ListException(ErrorType.LIST_NOT_FOUND));
         OfferBookResponse acceptedBook = list.getOffers().stream().filter(offer -> OfferStatus.KABUL.equals(offer.getOfferStatus())).toList().getFirst().getOfferedBook();
         ListBookResponse listBook = list.getBookInfo();
         exchangeDetails.setListBook(listBook);
         exchangeDetails.setAcceptedBook(acceptedBook);
         return exchangeDetails;
    }

    // Kabul edilen teklifteki alınacak kitabı bulmak için yardımcı metod
    private ListBookResponse fetchReceivedBook(String listId) {
        Lists list = listsRepository.findById(listId)
                .orElseThrow(() -> new ListException(ErrorType.LIST_NOT_FOUND));
        return list.getBookInfo();
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
        if(OfferStatus.valueOf(updateOfferRequest.getOfferStatus()).equals(OfferStatus.KABUL)){
            offers = list.getOffers().stream()
                    .filter(sentOffer -> !sentOffer.getOfferId().equals(updateOfferRequest.getOfferId()))
                    .map(o -> {
                        o.setOfferStatus(OfferStatus.RET);
                        return o;
                    })
                    .toList();
            bookManager.updateBookStat(UpdateBookStat.builder()
                    .status("REMOVED")
                    .bookId(list.getBookInfo().getId()).build());
            list.setStatus(ListsStatus.AWAITING_SHIPMENT);
        }
        targetOffer.setUpdatedDate(LocalDateTime.now());
        listsRepository.save(list);

        if (updateOfferRequest.getOfferStatus().equals(String.valueOf(OfferStatus.KABUL) )) {
            processAcceptedOffer(list, targetOffer);
            ListMailRequest request = new ListMailRequest();
            request.setOwnerId(list.getOwnerId());
            request.setOffererId(targetOffer.getOffererId());
            request.setListId(targetOffer.getOfferListId());
            request.setOfferedBookName(targetOffer.getOfferedBook().getTitle());
            request.setOfferedBookImage(targetOffer.getOfferedBook().getImage());
            request.setListBookName(list.getBookInfo().getTitle());
            request.setListBookImage(list.getBookInfo().getImage());
            request.setPaymentdeadline(LocalDateTime.now().plusMinutes(5));
            request.setShipmentdeadline(LocalDateTime.now().plusMinutes(5));
            log.info("Update list mail sent");
        }
        else if (updateOfferRequest.getOfferStatus().equals(String.valueOf(OfferStatus.RET)) && updateOfferRequest.getOfferStatus().equals(String.valueOf(OfferStatus.RET))) {
            UpdateBookStat updateBookStat = UpdateBookStat.builder()
                    .bookId(targetOffer.getOfferedBookId())
                    .status("ENABLED").build();
            bookManager.updateBookStat(updateBookStat);
            list.setStatus(ListsStatus.OPEN);
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
        bookManager.updateBookStat(UpdateBookStat.builder()
                .status("REMOVED").bookId(list.getBookInfo().getId()).build());
        list.setStatus(ListsStatus.AWAITING_SHIPMENT);// Shipping oluştur
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
        mailRequest.setTrustFee(list.getPrice());
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
        bookManager.updateBookStat(UpdateBookStat.builder()
                .bookId(list.getBookInfo().getId())
                .status("REMOVED")
                .build());
        // ✅ Karşı teklif edilen kitabın durumunu da REMOVED yap
        if (targetOffer.getOfferedBook() != null && targetOffer.getOfferedBook().getId() != null) {
            bookManager.updateBookStat(UpdateBookStat.builder()
                    .bookId(targetOffer.getOfferedBook().getId())
                    .status("REMOVED")
                    .build());
            log.info("Offered book status updated to REMOVED for bookId: {}", targetOffer.getOfferedBook().getId());
        }

        // İlan durumunu güncelle
        list.setStatus(ListsStatus.AWAITING_SHIPMENT);
        listsRepository.save(list); // değişikliklerin kaydedilmesi için

        List<Lists> offeredBookLists = listsRepository.findByBookInfo_Id(targetOffer.getOfferedBook().getId());
        if (!offeredBookLists.isEmpty()) {
            Lists offererList = offeredBookLists.get(0); // aynı kitabın birden fazla ilanı olabilir ama genelde 1 tane olur
            offererList.setStatus(ListsStatus.AWAITING_SHIPMENT);
            listsRepository.save(offererList);
            log.info("Offerer's list (bookId: {}) status updated to AWAITING_SHIPMENT", targetOffer.getOfferedBook().getId());
        } else {
            log.warn("No list found for offeredBookId: {}", targetOffer.getOfferedBook().getId());
        }

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
        transactionMailReq.setTrustFee(response.getTrustFee());
        transactionMailReq.setOffererId(targetOffer.getOffererId());
        transactionMailReq.setStatus(String.valueOf(response.getStatus()));
        transactionMailReq.setListBookImage(list.getBookInfo().getImage());
        transactionMailReq.setListBookName(list.getBookInfo().getTitle());
        transactionMailReq.setOfferedBookName(targetOffer.getOfferedBook().getTitle());
        transactionMailReq.setOfferedBookImage(targetOffer.getOfferedBook().getImage());
        transactionMailReq.setDeadline(LocalDateTime.now().plusMinutes(5));
        mailManager.testTransactionCreated(transactionMailReq);
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
        mailRequest.setTrustFee(response.getTrustFee());
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
        list.setStatus(list.getStatus() == ListsStatus.OPEN ? ListsStatus.CLOSED : ListsStatus.OPEN);
        listsRepository.save(list);
        log.info("Updated List Status: {} -> {}", listId, list.getStatus());
        return true;
    }

    public Double getListPrice(String listId){
        Lists list = listsRepository.findById(listId)
                .orElseThrow(() -> new ListException(ErrorType.LIST_NOT_FOUND));
        log.info("lists price: {}" ,list.getPrice());
        return list.getPrice() != null ? list.getPrice() : 0.0;
    }

    public OfferListResponse getOfferListById(String listId){
        Lists list = listsRepository.findById(listId)
                .orElseThrow(() -> new ListException(ErrorType.LIST_NOT_FOUND));
        return listMapper.ListToOfferListResponse(list);
    }

    public String getListType(String listId){
        Lists list = listsRepository.findById(listId)
                .orElseThrow(() -> new ListException(ErrorType.LIST_NOT_FOUND));
        return String.valueOf(list.getType());
    }
    public String getListOwner(String listId){
        Lists list = listsRepository.findById(listId)
                .orElseThrow(() -> new ListException(ErrorType.LIST_NOT_FOUND));
        return list.getOwnerId();
    }

    public List<ListResponse> getCategoryRecs(String userId){
        Set<String> categories = recommendationManager.getFilteredRecommendations(userId).getBody();
        log.info("kullanıcı tercihi kategoriler:{}",categories);
        List<Lists> lists = listsRepository.findByCategory(categories,userId);
        return lists.stream().map(listMapper::ListToListResponse).toList();
    }
    public List<Long> getRecommendations(List<Long> bookIds) {
        List<Integer> ids = bookIds.stream().mapToInt(Math::toIntExact).boxed().collect(Collectors.toList());
        RestTemplate restTemplate = new RestTemplate();
        String url = recommendationServiceUrl;
        log.info("url : {}",url);

        // Map.of yerine HashMap kullanımı
        Map<String, List<Integer>> request = new HashMap<>();
        request.put("visited_ids", ids);

        // JSON gövdesini doğru serileştirme
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, List<Integer>>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

        List<Integer> intBooks = (List<Integer>) response.getBody().get("recommended_ids");
        List<Long> longBooks = intBooks.stream().mapToLong(Integer::longValue).boxed().collect(Collectors.toList());
        return longBooks;
    }


    public List<ListResponse> getAllRecommendations(RecRequest recRequest) {
        // recommedation_service'den gelen öneriler
        List<Long> bookIds = getRecommendations(recRequest.getBookIds());
        List<Lists> allLists = listsRepository.findByBookInfo_idIn(bookIds);
        log.info("recommendations: {}",allLists);
        List<ListResponse> categoyRecs = getCategoryRecs(recRequest.getUserId());
        // List objelerini ListResponse'a dönüştür
        return allLists.stream()
                .map(listMapper::ListToListResponse)
                .toList();
    }


}
