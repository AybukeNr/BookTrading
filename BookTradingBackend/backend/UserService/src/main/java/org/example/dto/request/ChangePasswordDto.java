package org.example.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangePasswordDto {
    @NotNull
    private String username;
    @NotNull
    private String oldPassword;
    @NotNull
    private String newPassword;
    @NotNull
    private String token;
}
