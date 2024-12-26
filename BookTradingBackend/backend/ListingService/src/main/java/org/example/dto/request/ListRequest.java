package org.example.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.enums.ListType;

import org.example.dto.response.ListBookResponse;


import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListRequest {
    private String ownerId;
    private ListBookResponse bookInfo;
    private ListType type;
    private BigDecimal price;

}
