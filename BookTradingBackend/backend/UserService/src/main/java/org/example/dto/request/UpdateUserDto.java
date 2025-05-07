package org.example.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUserDto {

    @NotNull(message = "User name cannot be empty")
    private String userId;
    @NotNull(message = "User name cannot be empty")
    @Schema(example = "John")
    private String firstName;

    @NotNull(message = "User last name cannot be empty")
    @Schema(example = "Doe")
    private String lastName;

    @Schema(example = "0543 788 91 36")
    private String phoneNumber;

    @NotNull(message = "email cannot be empty")
    @Schema(example = "johndoe@gmail.com")
    private String email;
    @NotNull
    @Schema(example = "Kılıçali Paşa Mh. Beyoğlu/İstanbul PK:34433")
    private String address;
    private  String iban;
    private String password;
}