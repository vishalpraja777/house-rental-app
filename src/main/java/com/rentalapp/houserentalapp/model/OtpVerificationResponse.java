package com.rentalapp.houserentalapp.model;

import lombok.Data;

@Data
public class OtpVerificationResponse {

    private Boolean otpVerified;
    private Boolean isRegistered;
    private String jwtToken;

    public OtpVerificationResponse() {
        this.otpVerified = false;
        this.isRegistered = false;
        this.jwtToken = "";
    }
}
