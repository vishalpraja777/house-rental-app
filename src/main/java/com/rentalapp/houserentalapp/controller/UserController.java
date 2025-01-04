package com.rentalapp.houserentalapp.controller;

import com.rentalapp.houserentalapp.model.ChangePassword;
import com.rentalapp.houserentalapp.model.entities.Users;
import com.rentalapp.houserentalapp.service.UserService;
import com.rentalapp.houserentalapp.util.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/get-user/{userId}")
    public ResponseEntity<ResponseObject<Users>> getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("/update-user/{userId}")
    public ResponseEntity<ResponseObject<Users>> updateUserById(@PathVariable Long userId, @RequestBody Users user) {
        return userService.updateUserById(userId, user);
    }

    @PostMapping("/change-user-status/{userId}")
    public ResponseEntity<ResponseObject<String>> changeUserStatus(@PathVariable Long userId, @RequestBody Users user) {
        return userService.changeUserStatus(userId, user);
    }

    @PostMapping("/change-password/{userId}")
    public ResponseEntity<ResponseObject<String>> changePassword(@PathVariable Long userId, @RequestBody ChangePassword changePassword) {
        return userService.changePassword(userId, changePassword);
    }

}
