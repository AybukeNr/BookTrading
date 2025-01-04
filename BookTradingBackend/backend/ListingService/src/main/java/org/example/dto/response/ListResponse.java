package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.enums.ListType;
import org.example.entity.enums.ListsStatus;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListResponse {
    private String listId;
    private ListBookResponse book;
    private ListsStatus status;

}
