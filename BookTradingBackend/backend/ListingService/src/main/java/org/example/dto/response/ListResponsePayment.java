package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.enums.ListType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListResponsePayment {
    private String listId;
    private ListType type;
}
