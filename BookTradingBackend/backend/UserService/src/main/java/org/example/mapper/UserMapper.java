package org.example.mapper;


import org.example.entity.User;
import org.example.dto.request.UserRequest;
import org.example.dto.response.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    //todo: phone number
    public User UserRequestMapToUser(UserRequest request) {
        return User.builder().email(request.getEmail())
                .address(request.getAddress())
                .firstName(request.getFirstName())
                .iban(request.getIban())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(request.getPassword()).build();
    }
   public UserResponse UserMapToUserResponse(User user) {
        return UserResponse.builder()
                .userName(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .mailAddress(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress()).build();
    }

}