package org.example.service;

import lombok.RequiredArgsConstructor;

import org.example.dto.request.ChangePasswordDto;
import org.example.entity.User;
import org.example.dto.request.LoginRequest;

import org.example.dto.response.LoginResponse;
import org.example.exception.AuthException;
import org.example.exception.ErrorType;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.example.util.JwtTokenManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtTokenManager jwtTokenManager;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder() ;


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
    public void changePassword(String userId, String oldPassword, String newPassword) {
        // Kullanıcıyı ID'ye göre bul
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException(ErrorType.USER_NOT_FOUND, "User not found"));
        // Eski şifreyi kontrol et
        if (!user.getPassword().equals(oldPassword)) {
            throw new AuthException(ErrorType.INVALID_PASSWORD, "Old password is incorrect");
        }

        // Yeni şifreyi güncelle
        user.setPassword(newPassword);
        userRepository.save(user);  // Şifreyi veritabanında güncelle
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
    */



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


}