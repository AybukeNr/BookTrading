package org.example.dto.request;

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Kullanıcı kredi kartı bilgilerini içeren istek modeli")
public class CreateCardRequest {

    @NotNull(message = "User ID cannot be empty")
    @Schema(description = "Kullanıcı ID'si", example = "12345")
    private String userId;

    @NotNull(message = "Full name cannot be empty")
    @Schema(description = "Kart sahibinin tam adı", example = "John Doe")
    private String fullName;

    @NotNull(message = "Card number cannot be empty")
    @Schema(description = "Kredi kartı numarası", example = "4111111111111111")
    private String cardNumber;

    @NotNull(message = "CVV cannot be empty")
    @Schema(description = "Kartın güvenlik kodu (CVV)", example = "123")
    private String cvv;

    @NotNull(message = "Expiry date cannot be empty")
    @Schema(description = "Son kullanma tarihi (MM/YY formatında)", example = "12/25")
    private String expiryDate;
}
