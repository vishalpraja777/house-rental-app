package com.rentalapp.houserentalapp.model;

import lombok.Data;

@Data
public class OtpVerificationResponse {

    private Boolean otpVerified;
    private Boolean isRegistered;

}
