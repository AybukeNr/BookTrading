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
public class TransactionRequest {

    private String listId;
    private ListType listType;
    private String ownerId; // İlk tarafın kullanıcı ID'si
    private String offererId; // Karşı tarafın kullanıcı ID'si

}
