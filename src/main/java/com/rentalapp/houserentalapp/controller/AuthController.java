package com.rentalapp.houserentalapp.controller;

import com.rentalapp.houserentalapp.model.Users;
import com.rentalapp.houserentalapp.service.UserService;
import com.rentalapp.houserentalapp.util.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseObject<String>> login(@RequestBody Users user) {
        return userService.login(user);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseObject<Users>> register(@RequestBody Users user) {
        return userService.register(user);
    }

}
