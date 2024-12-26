package org.example.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.example.entity.enums.ListType;

import java.math.BigDecimal;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookListRequest {
    private String ownerId;
    private Long bookId;
    private ListType type;
    private BigDecimal price;
}
