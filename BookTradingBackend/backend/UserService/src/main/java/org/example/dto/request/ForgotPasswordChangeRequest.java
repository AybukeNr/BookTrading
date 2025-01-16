package org.example.dto.request;

import lombok.Data;

@Data
public class ForgotPasswordChangeRequest {
    private String newPassword;
}
