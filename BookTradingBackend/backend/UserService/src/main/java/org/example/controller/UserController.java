package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.entity.User;
import org.example.dto.response.UserResponse;
import org.example.service.UserService;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

import static org.example.constant.RestApiList.*;


@RestController
@CrossOrigin("*")
@RequestMapping(USERS)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Getting All Users REST API",
            description = "REST API to get All users"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error"
            )
    }
    )
    @GetMapping(GET_ALL_USERS)
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    @GetMapping(GET_USER_BY_ID)
    public ResponseEntity<UserResponse> getUserById(@RequestParam String id) {
        UserResponse userResponse = userService.findUserById(id);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
    @GetMapping(GET_ADDRESSES)
    public ResponseEntity<Map<String, String>> getAddresses(@RequestParam String ownerId, @RequestParam String offererId) {
        Map<String, String> addresses = userService.getUsersAddressesAsMap(ownerId, offererId); // Bu metod bir Map döndürmeli.
        return ResponseEntity.ok(addresses); // ResponseEntity ile sarmalanmış Map döndürülüyor.
    }

}