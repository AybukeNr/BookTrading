package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.request.AccountRequest;
import org.example.entity.User;
import org.example.dto.request.UserRequest;
import org.example.dto.response.UserResponse;
import org.example.exception.AuthException;
import org.example.exception.ErrorType;

import org.example.external.TransactionsManager;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TransactionsManager transactionsManager;

    public UserResponse createUser(UserRequest userRequest) {
        User user = userMapper.UserRequestMapToUser(userRequest);
        log.info("Create user: {}", user);
        userRepository.save(user);
        AccountRequest accountRequest = AccountRequest.builder()
                .userId(user.getId())
                .iban(userRequest.getIban())
                .fullName(userRequest.getFirstName() +" "+userRequest.getLastName()).build();
        transactionsManager.createAccount(accountRequest);
        log.info("Account created: {}", accountRequest);
        return userMapper.UserMapToUserResponse(user);
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
    public Map<String, String> getUsersAddressesAsMap(String ownerId, String offererId) {
        List<String> addresses = userRepository.findAddressesByIds(ownerId, offererId);
        if (addresses.size() < 2) {
            throw new RuntimeException("Both addresses (owner and offerer) must be available.");
        }
        Map<String, String> addressMap = new HashMap<>();
        addressMap.put("ownerAddress", addresses.get(0));
        addressMap.put("offererAddress", addresses.get(1));
        return addressMap;
    }


}
