package org.example.mapper;


import org.example.dto.response.ListMailResponse;
import org.example.entity.Lists;
import org.example.entity.enums.ListsStatus;

import org.example.dto.request.ListRequest;
import org.example.dto.response.ListResponse;
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
                .build();
    }
    public ListMailResponse ListToListMailResponse(Lists lists){
        return ListMailResponse.builder()
                .title(lists.getBookInfo().getTitle())
                .listBookImage(lists.getBookInfo().getListBookImage())
                .category(String.valueOf(lists.getBookInfo().getCategory()))
                .isbn(lists.getBookInfo().getIsbn())
                .author(lists.getBookInfo().getAuthor())
                .publishedDate(lists.getBookInfo().getPublishedDate())
                .publisher(lists.getBookInfo().getPublishedDate())
                .build();
    }
}
