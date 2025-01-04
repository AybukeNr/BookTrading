package org.example.dto.request;

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCardRequest {
    private String userId;
    private String fullName;
    private String cardNumber;
    private String cvv;
    private String expiryDate;
}
