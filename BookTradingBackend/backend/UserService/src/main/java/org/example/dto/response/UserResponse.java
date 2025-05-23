package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private String userName;
    private String firstName;
    private String lastName;
    private String mailAddress;
    private String phoneNumber;
    private String address;
    private String password;
    private String iban;
    private Double trustPoint;
    private List<String> userInterests;

}
