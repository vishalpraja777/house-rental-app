package com.rentalapp.houserentalapp.service;

import com.rentalapp.houserentalapp.model.OtpVerificationResponse;
import com.rentalapp.houserentalapp.util.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface OtpService {
    ResponseEntity<ResponseObject<String>> generateOTP(String phoneNumber);

    ResponseEntity<ResponseObject<OtpVerificationResponse>> verifyUserOTP(String phoneNumber, String otp);
}
