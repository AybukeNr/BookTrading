package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.request.UpdateUserDto;
import org.example.entity.User;
import org.example.dto.request.UserRequest;
import org.example.dto.response.UserResponse;
import org.example.exception.AuthException;
import org.example.exception.ErrorType;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public UserResponse updateUser(String userId, UpdateUserDto updateUserDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AuthException(ErrorType.USER_NOT_FOUND));
        userMapper.updateUserFromDto(updateUserDto,user);
        userRepository.save(user);
        return userMapper.UserMapToUserResponse(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public UserResponse findUserById(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AuthException(ErrorType.USER_NOT_FOUND));
        return userMapper.UserMapToUserResponse(user);
    }

    @Transactional
    public void deleteUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException(ErrorType.USER_NOT_FOUND));
        userRepository.delete(user);
    }

    public  User updateOneUser(String userId,User newUser){
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            User foundUser =user.get();
            foundUser.setFirstName((newUser.getFirstName()));
            foundUser.setLastName((newUser.getLastName()));
            foundUser.setEmail(newUser.getEmail());
            userRepository.save(foundUser);
            return foundUser;
        } else return null;
    }

}
