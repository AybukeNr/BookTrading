package org.example.service;

import lombok.RequiredArgsConstructor;

import org.example.document.User;
import org.example.dto.request.LoginRequest;

import org.example.dto.response.LoginResponse;
import org.example.exception.AuthException;
import org.example.exception.ErrorType;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.example.util.JwtTokenManager;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtTokenManager jwtTokenManager;
    private final UserMapper userMapper;
    private final UserService userService;


    public LoginResponse login(LoginRequest loginRequest){
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new AuthException(ErrorType.INVALID_USERNAME_OR_PASSWORD, "Email/Password Incorrect"));
        if(!user.getPassword().equals(loginRequest.getPassword()))
            throw new AuthException(ErrorType.INVALID_USERNAME_OR_PASSWORD, "Email/Password Incorrect");
        String token = jwtTokenManager.createToken(user.getId(),user.getUsername(),user.getPhoneNumber())
                .orElseThrow(() -> new AuthException(ErrorType.INVALID_TOKEN, "Geçersiz Oturum"));
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        return loginResponse ;
    };
    //todo : change password
    //todo : forgot password


}
