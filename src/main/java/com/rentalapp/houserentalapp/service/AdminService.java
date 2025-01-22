package com.rentalapp.houserentalapp.service;

import com.rentalapp.houserentalapp.model.entities.Property;
import com.rentalapp.houserentalapp.model.entities.Users;
import com.rentalapp.houserentalapp.util.ResponseObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminService {
    ResponseEntity<ResponseObject<List<Users>>> getAllUsers(Pageable pageable);

    ResponseEntity<ResponseObject<String>> changeUserStatusRole(Long userId, Users.Status status, Users.Role role);

    ResponseEntity<ResponseObject<String>> deleteUser(Long userId);

    ResponseEntity<ResponseObject<List<Property>>> getAllProperties(Pageable pageable);
}
