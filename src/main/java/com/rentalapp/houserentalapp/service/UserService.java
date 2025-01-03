package com.rentalapp.houserentalapp.service;

import com.rentalapp.houserentalapp.model.Users;
import com.rentalapp.houserentalapp.util.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<ResponseObject<Users>> register(Users user);

    ResponseEntity<ResponseObject<String>> login(Users user);

    ResponseEntity<ResponseObject<Users>> getUserById(Long userId);

    ResponseEntity<ResponseObject<Users>> updateUserById(Long userId, Users user);
}
