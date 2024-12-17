package org.example.document;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.document.enums.ListType;
import org.example.document.enums.ListsStatus;
import org.example.dto.response.ListBookResponse;


import org.example.dto.response.SentOffer;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



import java.math.BigDecimal;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "lists")
public class Lists {
    @Id
    private String id;
    private String ownerId;
    private ListBookResponse bookInfo;
    private ListsStatus status;
    private ListType type;
    private BigDecimal price;
    private List<SentOffer> offers;


}
