package org.example.mapper;


import org.example.dto.request.UpdateUserDto;
import org.example.entity.User;
import org.example.dto.request.UserRequest;
import org.example.dto.response.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    //todo: phone number

    public void updateUserFromDto(UpdateUserDto updateUserDto, User user) {
        // user nesnesinin mevcut değerlerini updateUserDto'dan gelen yeni değerlerle güncelliyoruz
        if (updateUserDto.getFirstName() != null && !updateUserDto.getFirstName().isEmpty()) {
            user.setFirstName(updateUserDto.getFirstName());
        }
        if (updateUserDto.getLastName() != null && !updateUserDto.getLastName().isEmpty()) {
            user.setLastName(updateUserDto.getLastName());
        }
        if (updateUserDto.getEmail() != null  && !updateUserDto.getEmail().isEmpty()) {
            user.setEmail(updateUserDto.getEmail());
        }
        if (updateUserDto.getAddress() != null  && !updateUserDto.getAddress().isEmpty()) {
            user.setAddress(updateUserDto.getAddress());
        }
        if (updateUserDto.getPhoneNumber() != null && !updateUserDto.getPhoneNumber().isEmpty()) {
            user.setPhoneNumber(updateUserDto.getPhoneNumber());
        }
    }
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