package com.rentalapp.houserentalapp.service;

import com.rentalapp.houserentalapp.model.entities.Users;
import com.rentalapp.houserentalapp.util.ResponseObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminService {
    ResponseEntity<ResponseObject<List<Users>>> getAllUsers(Pageable pageable);
}
