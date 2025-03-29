package org.example.dto.response;

import lombok.Data;
import org.example.entity.UserRole;

@Data
public class LoginResponse {
    private String token;
    private UserRole role;

}