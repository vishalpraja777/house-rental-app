package com.rentalapp.houserentalapp.model;

import lombok.Data;

@Data
public class ChangePassword {

    private String oldPassword;
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
