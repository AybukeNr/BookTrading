package org.example.service;

import lombok.RequiredArgsConstructor;

import org.example.entity.User;
import org.example.dto.request.LoginRequest;

import org.example.dto.response.LoginResponse;
import org.example.exception.AuthException;
import org.example.exception.ErrorType;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.example.util.JwtTokenManager;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtTokenManager jwtTokenManager;
//    private final UserMapper userMapper;
//    private final UserService userService;


    public LoginResponse login(LoginRequest loginRequest){
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new AuthException(ErrorType.INVALID_USERNAME_OR_PASSWORD, "Email/Password Incorrect"));
        if(!user.getPassword().equals(loginRequest.getPassword()))
            throw new AuthException(ErrorType.INVALID_USERNAME_OR_PASSWORD, "Email/Password Incorrect");
        String token = jwtTokenManager.createToken(user.getId(),user.getUsername(),user.getPhoneNumber(),user.getRole())
                .orElseThrow(() -> new AuthException(ErrorType.INVALID_TOKEN, "Geçersiz Oturum"));
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        loginResponse.setRole(user.getRole());
        return loginResponse ;
    }
    // şifreyi güvenli bir şekilde saklamak için Spring Security'nin BCrypt şifreleme algoritmasını kullana
/*
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new AuthException(ErrorType.INVALID_USERNAME_OR_PASSWORD, "Email/Password Incorrect"));

        // BCrypt ile eski şifreyi karşılaştırıyoruz
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new AuthException(ErrorType.INVALID_USERNAME_OR_PASSWORD, "Email/Password Incorrect");
        }

        String token = jwtTokenManager.createToken(user.getId(), user.getUsername(), user.getPhoneNumber())
                .orElseThrow(() -> new AuthException(ErrorType.INVALID_TOKEN, "Geçersiz Oturum"));
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        return loginResponse;
        }

        public void changePassword(String userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException(ErrorType.USER_NOT_FOUND, "User not found"));

        // Eski şifreyi karşılaştırıyoruz
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new AuthException(ErrorType.INVALID_PASSWORD, "Old password is incorrect");
        }

        // Yeni şifreyi şifreleyip veritabanına kaydediyoruz
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }


/*i

    // @Transactional
    /* public String changePasswords(ChangePasswordDto changePasswordDto){
        String userId = verifyToken(changePasswordDto.getToken() )
                .orElseThrow(() -> new AuthException(ErrorType.INVALID_TOKEN, "Geçersiz Oturum"));
        User user = userService.findUserById(userId)
                .orElseThrow(() -> new AuthException(ErrorType.USER_NOT_FOUND, "Kullanıcı Bulunamadı"));

        if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())) {
            throw new AuthException(ErrorType.BAD_REQUEST_ERROR, "Şifre Hatalı");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        save(user);
        return "Password changed successfully";
    }

    public Optional<String> verifyToken(String token) {
        return jwtTokenManager.getByIdFromToken(token);
    }
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = userService.findById(userId);
        user.setPassword(request.getNewPassword());
        userRepository.save(user);
    }
     */
    //todo : forgot password
    //todo : forgot password


}
