package org.example.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    @NotNull(message = "User name cannot be empty")
    @Schema(example = "John")
    private String firstName;

    @NotNull(message = "User last name cannot be empty")
    @Schema(example = "Doe")
    private String lastName;

    @NotNull(message = "UserName cannot be empty")
    @Schema(example = "JohnDOe")
    private String userName;

    @NotNull(message = "phone cannot be empty")
    @Schema(example = "0543 788 91 36")
    private String phoneNumber;

    @NotNull(message = "email cannot be empty")
    @Schema(example = "johndoe@gmail.com")
    private String email;
    @NotNull
    @Schema(example = "selam21")
    private String password;
    @NotNull
    @Schema(example = "TR33 0006 1005 1978 6457 8413 26")
    private String iban;
    @NotNull
    @Schema(example = "Kılıçali Paşa Mh. Beyoğlu/İstanbul PK:34433")
    private String address;
    private Double trustPoint;
    private List<String> userInterests;


}