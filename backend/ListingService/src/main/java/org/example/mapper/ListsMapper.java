package org.example.mapper;


import org.example.document.Lists;
import org.example.document.enums.ListsStatus;

import org.example.dto.request.ListRequest;
import org.example.dto.response.ListResponse;
import org.springframework.stereotype.Component;

@Component
public class ListsMapper {
    public Lists ListRequestToList(ListRequest listRequest){
        return Lists.builder()
                .bookInfo(listRequest.getBookInfo())
                .status(ListsStatus.OPEN)
                .type(listRequest.getType())
                .price(listRequest.getPrice())
                .ownerId(listRequest.getOwnerId()).build();

    }
    public ListResponse ListToListResponse(Lists lists){
        return ListResponse.builder()
                .book(lists.getBookInfo())
                .status(lists.getStatus())
                .build();
    }
}
