package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.dto.request.UpdateUserDto;
import org.example.entity.User;
import org.example.dto.response.UserResponse;
import org.example.exception.AuthException;
import org.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

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

    @PutMapping("/update/{userId}")
    public User updateOneUser(@PathVariable String userId,@RequestBody User newUser){
        return userService.updateOneUser(userId,newUser);
    }
    @PutMapping("updateUser/{userId}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable String userId,@RequestBody UpdateUserDto updateUserDto
    ) {
        try {
            UserResponse updatedUser = userService.updateUser(userId, updateUserDto);
            return ResponseEntity.ok(updatedUser);
        }
        catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PutMapping("/update/{userId}")
    public User updateOneUser(@PathVariable String userId,@RequestBody User newUser){
        return userService.updateOneUser(userId,newUser);
    }
    @PutMapping("updateUser/{userId}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable String userId,@RequestBody UpdateUserDto updateUserDto
    ) {
        try {
            UserResponse updatedUser = userService.updateUser(userId, updateUserDto);
            return ResponseEntity.ok(updatedUser);
        }
        catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        try {
            userService.deleteUserById(userId);
            return ResponseEntity.noContent().build();  // 204 No Content yanıtı döner

        }
        catch (AuthException e) {
            return ResponseEntity.status(404).body(null);
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        try {
            userService.deleteUserById(userId);
            return ResponseEntity.noContent().build();  // 204 No Content yanıtı döner

        }
        catch (AuthException e) {
            return ResponseEntity.status(404).body(null);
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }


}