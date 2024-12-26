package org.example.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.response.BookResponse;
import org.example.entity.enums.ListType;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListRequest {
    private String ownerId;
    private BookResponse bookInfo;
    private ListType type;
    private BigDecimal price;
}
