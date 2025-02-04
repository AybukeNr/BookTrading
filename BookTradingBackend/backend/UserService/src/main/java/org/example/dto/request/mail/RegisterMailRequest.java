package org.example.dto.request.mail;


import lombok.*;

@Getter
@Setter
public class RegisterMailRequest extends MailRequest {
    private String username;
    private String password;
}
