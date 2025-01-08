package com.rentalapp.houserentalapp.controller;

import com.rentalapp.houserentalapp.model.ChangePassword;
import com.rentalapp.houserentalapp.model.entities.Users;
import com.rentalapp.houserentalapp.service.UserService;
import com.rentalapp.houserentalapp.util.ResponseObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User APIs", description = "Adding, Updating, Getting and Deleting User APIs")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/get-user/{userId}")
    @Operation(summary = "Get User By Id API")
    public ResponseEntity<ResponseObject<Users>> getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("/update-user/{userId}")
    @Operation(summary = "Update User By Id API")
    public ResponseEntity<ResponseObject<Users>> updateUserById(@PathVariable Long userId, @RequestBody Users user) {
        return userService.updateUserById(userId, user);
    }

    @PostMapping("/change-user-status/{userId}")
    @Operation(summary = "Change User Status API")
    public ResponseEntity<ResponseObject<String>> changeUserStatus(@PathVariable Long userId, @RequestParam Users.Status status) {
        return userService.changeUserStatus(userId, status);
    }

    @PostMapping("/change-password/{userId}")
    @Operation(summary = "Change User Password API")
    public ResponseEntity<ResponseObject<String>> changePassword(@PathVariable Long userId, @RequestBody ChangePassword changePassword) {
        return userService.changePassword(userId, changePassword);
    }

}
