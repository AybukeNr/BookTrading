package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.request.AccountRequest;
import org.example.dto.request.CreateCardRequest;
import org.example.dto.request.mail.RegisterMailRequest;
import org.example.dto.request.UpdateUserDto;
import org.example.dto.response.UserContactInfos;
import org.example.dto.response.UserResponseId;
import org.example.entity.User;
import org.example.dto.request.UserRequest;
import org.example.dto.response.UserResponse;
import org.example.entity.UserRole;
import org.example.exception.AuthException;
import org.example.exception.ErrorType;

import org.example.external.CardsManager;
import org.example.external.TransactionsManager;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.example.util.CardNumberUtil;
import org.example.util.JwtTokenManager;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TransactionsManager transactionsManager;
    private final CardsManager cardsManager;
    private final MailService mailService;

    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        User user = userMapper.UserRequestMapToUser(userRequest);
        log.info("Create user: {}", user);
        userRepository.save(user);
        AccountRequest accountRequest = AccountRequest.builder()
                .userId(user.getId())
                .iban(userRequest.getIban())
                .fullName(userRequest.getFirstName() +" "+userRequest.getLastName()).build();
        transactionsManager.createAccount(accountRequest);
        cardsManager.createCard(CreateCardRequest.builder().cardNumber(CardNumberUtil.generateCardNumber()).cvv("123")
                .expiryDate("12/25").fullName(user.getFirstName()+""+user.getLastName()).userId(user.getId()).build());
        log.info("Account created: {}", accountRequest);
//        RegisterMailRequest request = new RegisterMailRequest();
//        request.setPassword(userRequest.getPassword());
//        request.setUsername(userRequest.getUserName());
//        request.setSentToMailAddress(userRequest.getEmail());
//        mailService.sendRegisterMail(request);
        return userMapper.UserMapToUserResponse(user);
    }

    @Transactional
    public UserResponse updateUser( UpdateUserDto updateUserDto) {
        User user = userRepository.findById(updateUserDto.getUserId()).orElseThrow(() -> new AuthException(ErrorType.USER_NOT_FOUND));
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
    public Map<String, String> getUsersAddressesAsMap(String ownerId, String offererId) {
        List<String> addresses = userRepository.findAddressesByIds(ownerId,offererId);
        log.info("recieved list of addresses: {}", addresses);
        if (addresses.size() < 2) {
            throw new RuntimeException("Both addresses (owner and offerer) must be available.");
        }
        Map<String, String> addressMap = new HashMap<>();
        addressMap.put("ownerAddress", addresses.get(0));
        addressMap.put("offererAddress", addresses.get(1));
        return addressMap;
    }
    @Transactional
    public void deleteUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException(ErrorType.USER_NOT_FOUND));
        userRepository.delete(user);
    }


    @Transactional
    public User updateOneUser(String userId, User newUser) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            User foundUser = user.get();
            foundUser.setFirstName((newUser.getFirstName()));
            foundUser.setLastName((newUser.getLastName()));
            foundUser.setEmail(newUser.getEmail());
            userRepository.save(foundUser);
            return foundUser;
        } else return null;
    }

    @Transactional
    public void reduceUsertrustPoint(String userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            User foundUser = user.get();
            foundUser.setTrustPoint(user.get().getTrustPoint()-1);
        }
    }
    public UserResponseId getUserResponseById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        return UserResponseId.builder()
                .id(user.getId())
                .userName(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .trustPoint(user.getTrustPoint())
                .build();
    }

    public Map<String,UserContactInfos> getUserContactInfos(String ownerId,String offererId) {
        Map<String,UserContactInfos> userContactInfos = new HashMap<>();
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + ownerId));
        User offerer = userRepository.findById(offererId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + offererId));
        UserContactInfos ownerInfo = UserContactInfos.builder().email(owner.getEmail()).phone(owner.getPhoneNumber())
                        .address(owner.getAddress()).fullName(owner.getFirstName()+""+owner.getLastName()).build();
        UserContactInfos offererInfos = UserContactInfos.builder().email(offerer.getEmail()).phone(offerer.getPhoneNumber())
                .address(offerer.getAddress()).fullName(offerer.getFirstName()+""+offerer.getLastName()).build();
        userContactInfos.put("owner",ownerInfo);
        userContactInfos.put("offerer",offererInfos);
        return userContactInfos;

    }

    public List<String> getUsersInterests(String userId){
        return userRepository.findById(userId).get().getUserInterests();
    }
}
