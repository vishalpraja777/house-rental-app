package com.rentalapp.houserentalapp.controller;

import com.rentalapp.houserentalapp.model.LoginUserDto;
import com.rentalapp.houserentalapp.model.OtpVerificationResponse;
import com.rentalapp.houserentalapp.model.RegisterUserDto;
import com.rentalapp.houserentalapp.model.entities.Users;
import com.rentalapp.houserentalapp.service.OtpService;
import com.rentalapp.houserentalapp.service.UserService;
import com.rentalapp.houserentalapp.util.ResponseObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication APIs", description = "Register and Login APIs")
public class AuthController {

    private final UserService userService;

    private final OtpService otpService;

    public AuthController(UserService userService, OtpService otpService) {
        this.userService = userService;
        this.otpService = otpService;
    }

    @PostMapping("/login")
    @Operation(summary = "User Login API")
    public ResponseEntity<ResponseObject<String>> login(@RequestBody LoginUserDto user) {
        log.info("User Login API Called for User: {}", user.getUsername());
        return userService.login(user);
    }

    @PostMapping("/register")
    @Operation(summary = "User Register API")
    public ResponseEntity<ResponseObject<String>> register(@RequestBody RegisterUserDto user) {
        log.info("User Register API Called for User: {}", user.getUsername());
        return userService.register(user);
    }

    @GetMapping(value = "/generateOTP/{phoneNumber}")
    public ResponseEntity<ResponseObject<String>>  generateOTP(@PathVariable(name = "phoneNumber") String phoneNumber){
        log.info("Generate OTP API Called for Phone Number: {}", phoneNumber);
        return otpService.generateOTP(phoneNumber);
    }

    @GetMapping("/verifyOTP/{phoneNumber}")
    public ResponseEntity<ResponseObject<OtpVerificationResponse>> verifyUserOTP(@PathVariable(name = "phoneNumber") String phoneNumber,
                                                                                 @RequestParam(name = "otp") String otp) {
        log.info("Verify OTP API Called for Phone Number: {}", phoneNumber);
        return otpService.verifyUserOTP(phoneNumber, otp);

    }

}
