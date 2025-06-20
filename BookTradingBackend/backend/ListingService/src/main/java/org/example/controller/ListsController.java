package org.example.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.example.dto.request.*;
import org.example.dto.response.*;

import org.example.dto.response.offer.OfferListResponse;
import org.example.dto.response.offer.SentOffer;
import org.example.service.ListsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.example.constants.RestApiList.*;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping(LISTS)
@Slf4j
@Tag(name = "Lists Controller", description = "Operations related to book lists")
public class ListsController {

    private final ListsService listsService;

    @DeleteMapping("/delete/{listId}")
    public ResponseEntity<Boolean> deleteList(@PathVariable String listId) {
        Boolean isDeleted = listsService.deleteList(listId);
        return ResponseEntity.ok(isDeleted);
    }

    //Servisler arası endpoint
    @Operation(summary = "Servisler arası endpoint")
    @DeleteMapping("/delete/by-book/{bookId}")
    public ResponseEntity<String> deleteListsByBookId(@PathVariable Long bookId) {
        listsService.deleteAllListsByBookId(bookId);
        return ResponseEntity.ok("All lists with bookId " + bookId + " have been deleted.");
    }
    //servisler arası endpoint
    @Operation(summary = "Create a new list,Servisler arası endpoint", description = "Creates a new book list with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided")
    })
    @PutMapping("/update-book-info")
    public void updateBookInfoInLists(@RequestBody BookUpdateRequest updateBookRequest) {
        System.out.println(">>> updateBookInfoInLists çalıştı! BookId: " + updateBookRequest.getId());
        listsService.updateBookInfo(updateBookRequest);
    }
    @PostMapping(CREATE_LISTS)
    public ResponseEntity<Boolean> createList(@RequestBody ListRequest lists) {
        log.info("Received ListRequest: {}", lists);
        log.info("Received Book: {}", lists.getBookInfo());
        listsService.createLists(lists);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @Operation(summary = "Get all lists", description = "Retrieves all available book lists.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lists retrieved successfully")
    })
    @GetMapping(GET_ALL_LISTS)
    public ResponseEntity<List<ListResponse>> getLists() {
        List<ListResponse> lists = listsService.getAllLists();
        return new ResponseEntity<>(lists, HttpStatus.OK);
    }

    @Operation(summary = "Get lists by owner ID", description = "Retrieves all lists associated with a specific owner.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lists retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Owner not found")
    })
    @GetMapping(GET_LISTS_EXCLUDING_OWNER)
    public ResponseEntity<List<ListResponseN>> getListsExcludingOwner(@RequestParam String ownerId) {
        List<ListResponseN> lists = listsService.getListsExcludingOwner(ownerId);
        return new ResponseEntity<>(lists, HttpStatus.OK);
    }

    @Operation(summary = "Get lists by owner ID", description = "Retrieves all lists associated with a specific owner.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lists retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Owner not found")
    })
    @GetMapping(GET_LISTS_BY_OWNER_ID)
    public ResponseEntity<List<ListResponse>> getListsByOwnerId(@RequestParam String ownerId) {
        List<ListResponse> lists = listsService.getListsByOwnerId(ownerId);
        return new ResponseEntity<>(lists, HttpStatus.OK);
    }

    @Operation(summary = "Get list by ID", description = "Retrieves a specific list by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "List not found")
    })
    @GetMapping(GET_LIST_BY_ID)
    public ResponseEntity<ListResponseN> getListById(@RequestParam String listId) {
        ListResponseN lists = listsService.getListById(listId);
        log.info("Received ListRequest: {}", lists);
        log.info("Received Book: {}", lists.getBook());
        return new ResponseEntity<>(lists, HttpStatus.OK);
    }

    //servisler arası endpoint
    @Operation(summary = "Take an offer,Servisler arası endpoint", description = "Accepts an offer for a specific list.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Offer accepted successfully"),
            @ApiResponse(responseCode = "404", description = "Offer not found")
    })
    @PostMapping(TAKE_OFFER)
    public ResponseEntity<Boolean> takeOffer(@RequestBody SentOffer offer) {
        log.info("Received OfferRequest: {}", offer);
        listsService.takeOffers(offer);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @Operation(summary = "Get received offers", description = "Retrieves all offers received by a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Offers retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping(GET_RECIEVED_OFFERS)
    public ResponseEntity<List<SentOffer>> getRecievedOffers(@RequestParam String userId) {
        List<SentOffer> lists = listsService.getRecievedOffers(userId);
        return new ResponseEntity<>(lists, HttpStatus.OK);
    }

    //servisler arası endpoint
    @Operation(summary = "BURAYI KULLANMA,Servisler arası endpoint", description = "DİKKAT , İLAN GÜNCELLEMEK İÇİN BU ENDPOİNT KULLANILAMAZ,SONSUZ DÖNGÜYE GİRER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Offer updated successfully"),
            @ApiResponse(responseCode = "404", description = "Offer not found")
    })
    @PutMapping(UPDATE_OFFER)
    public ResponseEntity<Void> updateOffer(@RequestBody UpdateOfferRequest offer) {
        log.info("Received OfferRequest: {}", offer);
        listsService.updateOffer(offer);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //servisler arası endpoint
    @Operation(summary = "Update list status,Servisler arası endpoint", description = "Updates the status of a specific list.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List status updated successfully"),
            @ApiResponse(responseCode = "404", description = "List not found")
    })
    @PutMapping (UPDATE_LIST_STATUS)
    public ResponseEntity<Boolean> updateListStatus(@RequestBody UpdateListReq listReq) {
        log.info("Received List Request: {}", listReq);
        return new ResponseEntity<>(listsService.updateListStatus(listReq), HttpStatus.OK);
    }

    @PutMapping("/approval/{listId}")
    public ResponseEntity<String> updateListApprovalStatus(@PathVariable String listId) {
        boolean updated = listsService.updateListApprovalStatus(listId);

        if (updated) {
            return ResponseEntity.ok("List status updated successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to update list status.");
        }
    }

    //servisler arası endpoint
    @Operation(summary = "Servisler arası endpoint")
    @PutMapping (PROCESS_SALES)
    public ResponseEntity<Boolean> processSales(@RequestBody SalesRequest sale) {
        log.info("Received Sale Request: {}", sale);
        listsService.processSales(sale);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
    //servisler arası endpoint
    @Operation(summary = "Servisler arası endpoint")
    @GetMapping(LIST_MAIL_INFOS)
    public ResponseEntity<ListMailResponse> mailInfo(@RequestParam String listId){
        log.info("Getting infos for list {}",listId);
        return new ResponseEntity<>(listsService.listMailSummary(listId), HttpStatus.OK);
    }
    //servisler arası endpoint
    @Operation(summary = "Servisler arası endpoint")
    @GetMapping(GET_LIST_PRICE)
    public ResponseEntity<Double> getListPrice(@RequestParam String listId){
        log.info("Getting price for list {}",listId);
        return new ResponseEntity<>(listsService.getListPrice(listId), HttpStatus.OK);
    }

    @Operation(summary = "Get list by ID", description = "Retrieves a specific list by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "List not found")
    })
    @GetMapping(GET_LIST_BY_ID_OFFER)
    public ResponseEntity<OfferListResponse> getOfferListById(@RequestParam String listId) {
        OfferListResponse lists = listsService.getOfferListById(listId);
        log.info("Received ListRequest: {}", lists);
        log.info("Received Book: {}", lists.getBook());
        return new ResponseEntity<>(lists, HttpStatus.OK);
    }
    @Operation(summary = "Takaslanan kitapları getirir", description = "servisler arası endpoint")
    @GetMapping(GET_EXCHANGE_BOOKS)
    public ResponseEntity<ExchangeDetails> getExchangeBooks(@RequestParam String listId){
        ExchangeDetails acceptedOfferBooks = listsService.getExchangeBooks(listId);
        return new ResponseEntity<>(acceptedOfferBooks, HttpStatus.OK);
    }

    @GetMapping(GET_LIST_TYPE)
    public ResponseEntity<String> getListType(@RequestParam String listId) {
        return new ResponseEntity<>(listsService.getListType(listId),HttpStatus.OK);
    }

    @GetMapping(GET_LISTS_OWNERID)
    public ResponseEntity<String> getListOwnerId(@RequestParam String listId) {
        return new ResponseEntity<>(listsService.getListOwner(listId),HttpStatus.OK);
    }

    @PostMapping(GET_ALL_RECS)
    public ResponseEntity<List<ListResponse>> getAllRecommendations(@RequestBody RecRequest recRequest) {
        log.info("Request: {}", recRequest);
        return new ResponseEntity<>(listsService.getAllRecommendations(recRequest),HttpStatus.OK);
    }

}
