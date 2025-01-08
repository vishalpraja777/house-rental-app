package com.rentalapp.houserentalapp.controller;

import com.rentalapp.houserentalapp.model.LoginUserDto;
import com.rentalapp.houserentalapp.model.RegisterUserDto;
import com.rentalapp.houserentalapp.model.entities.Users;
import com.rentalapp.houserentalapp.service.UserService;
import com.rentalapp.houserentalapp.util.ResponseObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication APIs", description = "Register and Login APIs")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    @Operation(summary = "User Login API")
    public ResponseEntity<ResponseObject<String>> login(@RequestBody LoginUserDto user) {
        return userService.login(user);
    }

    @PostMapping("/register")
    @Operation(summary = "User Register API")
    public ResponseEntity<ResponseObject<Users>> register(@RequestBody RegisterUserDto user) {
        return userService.register(user);
    }

}
