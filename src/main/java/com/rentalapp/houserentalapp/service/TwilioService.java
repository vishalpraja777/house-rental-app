package com.rentalapp.houserentalapp.service;

import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;

public interface TwilioService {
    Verification generateOTP(String phoneNumber, String channelType);

    VerificationCheck verifyUserOTP(String phoneNumber, String otp);
}
