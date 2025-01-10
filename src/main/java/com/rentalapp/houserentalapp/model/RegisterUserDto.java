package com.rentalapp.houserentalapp.model;

import com.rentalapp.houserentalapp.model.entities.Users;
import lombok.Data;

@Data
public class RegisterUserDto {

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String phone;
    private Users.Role role;

}
