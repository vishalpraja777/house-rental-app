package com.rentalapp.houserentalapp.service;

import com.rentalapp.houserentalapp.model.ChangePassword;
import com.rentalapp.houserentalapp.model.LoginUserDto;
import com.rentalapp.houserentalapp.model.RegisterUserDto;
import com.rentalapp.houserentalapp.model.entities.Users;
import com.rentalapp.houserentalapp.util.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<ResponseObject<Users>> getLoggedInUser();

    ResponseEntity<ResponseObject<Users>> register(RegisterUserDto user);

    ResponseEntity<ResponseObject<String>> login(LoginUserDto user);

    ResponseEntity<ResponseObject<Users>> getUserById(Long userId);

    ResponseEntity<ResponseObject<Users>> updateUserById(Long userId, Users user);

    ResponseEntity<ResponseObject<String>> changeUserStatus(Long userId, Users.Status status);

    ResponseEntity<ResponseObject<String>> changePassword(Long userId, ChangePassword changePassword);

}
