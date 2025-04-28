package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.enums.ListType;
import org.example.entity.enums.ListsStatus;

import java.math.BigDecimal;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListResponse {
    private String listId;
    private ListBookResponse book;
    private ListsStatus status;
    private ListType type;
    private Double price;

}
