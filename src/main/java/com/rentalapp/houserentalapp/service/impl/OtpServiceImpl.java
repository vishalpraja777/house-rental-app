package com.rentalapp.houserentalapp.service.impl;

import com.rentalapp.houserentalapp.dao.UserRepository;
import com.rentalapp.houserentalapp.model.OtpVerificationResponse;
import com.rentalapp.houserentalapp.service.OtpService;
import com.rentalapp.houserentalapp.service.TwilioService;
import com.rentalapp.houserentalapp.util.Constants;
import com.rentalapp.houserentalapp.util.CustomResponseUtil;
import com.rentalapp.houserentalapp.util.ResponseObject;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

import static java.util.Objects.nonNull;

@Slf4j
@Service
public class OtpServiceImpl implements OtpService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TwilioService twilioService;

    @Override
    public ResponseEntity<ResponseObject<String>> generateOTP(String phoneNumber) {
        try {
            // Phone Number format +91987654321
            Verification verification = twilioService.generateOTP(phoneNumber, "sms");
            log.info(verification.getStatus());
            log.info("OTP has been successfully generated, and awaits your verification {}", LocalDateTime.now());
            return CustomResponseUtil.getSuccessResponse("OTP has been sent to the phone number: " + phoneNumber, HttpStatus.OK);
        } catch (Exception e) {
            log.error(Constants.ERROR_SENDING_OTP + ", possible cause: " + e.getMessage());
            return CustomResponseUtil.getFailureResponse(Constants.ERROR_SENDING_OTP , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseObject<OtpVerificationResponse>> verifyUserOTP(String phoneNumber, String otp) {
        try {
            OtpVerificationResponse otpVerificationResponse = new OtpVerificationResponse();
            VerificationCheck verificationCheck = twilioService.verifyUserOTP(phoneNumber, otp);

            if(Objects.equals(verificationCheck.getStatus(), "approved")) {
                otpVerificationResponse.setOtpVerified(true);
                if (nonNull(userRepository.findByPhone(phoneNumber))) {
                    otpVerificationResponse.setIsRegistered(true);
                    return CustomResponseUtil.getSuccessResponse(otpVerificationResponse, HttpStatus.OK);
                }
                otpVerificationResponse.setIsRegistered(false);
                return CustomResponseUtil.getSuccessResponse(otpVerificationResponse, HttpStatus.OK);
            }
            otpVerificationResponse.setOtpVerified(false);
            return CustomResponseUtil.getSuccessResponse(otpVerificationResponse, HttpStatus.UNAUTHORIZED);

        } catch (Exception e) {
            return CustomResponseUtil.getFailureResponse("Verification failed.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
