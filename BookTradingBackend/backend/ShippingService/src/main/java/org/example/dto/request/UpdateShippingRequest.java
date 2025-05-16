package org.example.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateShippingRequest {
    private String shippingSerialNumber;
    @NotNull(message = "kargo takip no boş olamaz")
    @Schema(example = "1234567897")
    @Pattern(regexp="(^[1-9][0-9]{10}$)",message="geçersiz takip no")
    private String trackingNumber;
    private String userId;
}
