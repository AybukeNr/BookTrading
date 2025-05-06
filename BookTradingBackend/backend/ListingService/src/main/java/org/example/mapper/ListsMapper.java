package org.example.mapper;


import org.example.dto.response.*;
import org.example.entity.Lists;
import org.example.entity.enums.ListsStatus;

import org.example.dto.request.ListRequest;
import org.springframework.stereotype.Component;

@Component
public class ListsMapper {
    public Lists ListRequestToList(ListRequest listRequest){
        return Lists.builder()
                .bookInfo(listRequest.getBookInfo())
                .status(ListsStatus.CLOSED)
                .type(listRequest.getType())
                .price(listRequest.getPrice())
                .ownerId(listRequest.getOwnerId()).build();

    }
    public ListResponse ListToListResponse(Lists lists){
        return ListResponse.builder()
                .listId(lists.getId())
                .book(lists.getBookInfo())
                .status(lists.getStatus())
                .price(lists.getPrice())
                .type(lists.getType())
                .build();
    }


    public OfferBookResponse listBookToOfferBook(Lists list){
        ListBookResponse listBookResponse = list.getBookInfo();
        return OfferBookResponse.builder()
                .image(listBookResponse.getImage())
                .publisher(listBookResponse.getPublisher())
                .title(listBookResponse.getTitle())
                .publishedDate(listBookResponse.getPublishedDate())
                .isbn(listBookResponse.getIsbn())
                .author(listBookResponse.getAuthor())
                .category(listBookResponse.getCategory().getDisplayName())
                .id(listBookResponse.getId())
                .build();
    }

    public OfferListResponse ListToOfferListResponse(Lists lists){
        return OfferListResponse.builder().listid(lists.getId())
                .book(listBookToOfferBook(lists))
                .build();
    }
    public ListMailResponse ListToListMailResponse(Lists lists){
        return ListMailResponse.builder()
                .title(lists.getBookInfo().getTitle())
                .listBookImage(lists.getBookInfo().getImage())
                .category(String.valueOf(lists.getBookInfo().getCategory()))
                .isbn(lists.getBookInfo().getIsbn())
                .author(lists.getBookInfo().getAuthor())
                .publishedDate(lists.getBookInfo().getPublishedDate())
                .publisher(lists.getBookInfo().getPublishedDate())
                .build();
    }
}
