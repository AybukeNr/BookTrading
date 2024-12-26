package org.example.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateShippingRequest {

    private String listID;
    private String senderId;
    private String receiverId;
    private Long sendingBookId;
    private Long offeredBookId;
}
