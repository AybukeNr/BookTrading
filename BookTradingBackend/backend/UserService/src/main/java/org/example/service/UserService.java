package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.User;
import org.example.dto.request.UserRequest;
import org.example.dto.response.UserResponse;
import org.example.exception.AuthException;
import org.example.exception.ErrorType;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse createUser(UserRequest userRequest) {
        User user = userMapper.UserRequestMapToUser(userRequest);
        log.info("Create user: {}", user);
        return userMapper.UserMapToUserResponse(userRepository.save(user));
    }

    //todo:update user herhangi bir şeyi güncelleme
    public User updateUser(User user) {
        return user;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public UserResponse findUserById(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AuthException(ErrorType.USER_NOT_FOUND));
        return userMapper.UserMapToUserResponse(user);
    }
}
