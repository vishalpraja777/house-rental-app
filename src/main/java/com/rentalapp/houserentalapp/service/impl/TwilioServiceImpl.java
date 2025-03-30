package com.rentalapp.houserentalapp.service.impl;

import com.rentalapp.houserentalapp.service.TwilioService;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioServiceImpl implements TwilioService {

//    @Value("${twilio.account.sid}")
//    private String twilioAccountSid;
//
//    @Value("${twilio.auth.token}")
//    private String twilioAuthToken;

    @Value("${twilio.service.id}")
    private String twilioServiceId;

    public TwilioServiceImpl(@Value("${twilio.account.sid}") String twilioAccountSid, @Value("${twilio.auth.token}") String twilioAuthToken) {
//        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Twilio.init(twilioAccountSid, twilioAuthToken);
    }

    @Override
    public Verification generateOTP(String phoneNumber, String channelType) {
        return Verification.creator(
                        twilioServiceId, // this is your verification sid
                        phoneNumber, //this is your Twilio verified recipient phone number
                        channelType) // this is your channel type
                .create();
    }

    @Override
    public VerificationCheck verifyUserOTP(String phoneNumber, String otp) {
        return VerificationCheck.creator(
                        twilioServiceId)
                .setTo(phoneNumber)
                .setCode(otp)
                .create();
    }
}
