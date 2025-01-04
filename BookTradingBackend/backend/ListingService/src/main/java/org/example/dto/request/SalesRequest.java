package org.example.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.enums.ListType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesRequest {

    private String listId;
    private String offererId; // Karşı tarafın kullanıcı ID'si

}
