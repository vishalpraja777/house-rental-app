package com.rentalapp.houserentalapp.service.impl;

import com.rentalapp.houserentalapp.dao.PropertyRepository;
import com.rentalapp.houserentalapp.dao.UserRepository;
import com.rentalapp.houserentalapp.model.entities.Property;
import com.rentalapp.houserentalapp.model.entities.Users;
import com.rentalapp.houserentalapp.service.AdminService;
import com.rentalapp.houserentalapp.util.Constants;
import com.rentalapp.houserentalapp.util.CustomResponseUtil;
import com.rentalapp.houserentalapp.util.ResponseObject;
import com.rentalapp.houserentalapp.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;

    public AdminServiceImpl(UserRepository userRepository, PropertyRepository propertyRepository) {
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
    }

    @Override
    public ResponseEntity<ResponseObject<List<Users>>> getAllUsers(Pageable pageable) {
        if(!SecurityUtil.isAdmin()) {
            return CustomResponseUtil.getFailureResponse(Constants.USER_UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

        List<Users> users = new ArrayList<>();
        userRepository.findAll(pageable).forEach(users::add);
        return CustomResponseUtil.getSuccessResponse(users, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseObject<String>> changeUserStatusRole(Long userId, Users.Status status, Users.Role role) {
        if(!SecurityUtil.isAdmin()) {
            return CustomResponseUtil.getFailureResponse(Constants.USER_UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

        Optional<Users> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) {
            log.error(Constants.USER_NOT_FOUND + " : " + userId);
            return CustomResponseUtil.getFailureResponse(Constants.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        try {
            if(status != null) {
                userRepository.updateStatus(status, userId);
            }
            if(role != null) {
                userRepository.updateRole(role, userId);
            }
            return CustomResponseUtil.getSuccessResponse(Constants.USER_STATUS_OR_ROLE_CHANGED, HttpStatus.OK);

        } catch (Exception e) {
            log.error(Constants.ERROR_UPDATING_USER + ", possible cause: " + e.getMessage());
            return CustomResponseUtil.getFailureResponse(Constants.ERROR_UPDATING_USER, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseObject<String>> deleteUser(Long userId) {
        if(!SecurityUtil.isAdmin()) {
            return CustomResponseUtil.getFailureResponse(Constants.USER_UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

        Optional<Users> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) {
            log.error(Constants.USER_NOT_FOUND + " : " + userId);
            return CustomResponseUtil.getFailureResponse(Constants.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        try {
            userRepository.delete(optionalUser.get());
            return CustomResponseUtil.getSuccessResponse(Constants.USER_DELETED_SUCCESSFULLY, HttpStatus.OK);

        } catch (Exception e) {
            log.error(Constants.ERROR_DELETING_USER + ", possible cause: " + e.getMessage());
            return CustomResponseUtil.getFailureResponse(Constants.ERROR_DELETING_USER, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<ResponseObject<List<Property>>> getAllProperties(Pageable pageable) {
        if (!SecurityUtil.isAdmin()) {
            return CustomResponseUtil.getFailureResponse(Constants.USER_UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }
        List<Property> properties = new ArrayList<>();
        propertyRepository.findAll(pageable).forEach(properties::add);
        return CustomResponseUtil.getSuccessResponse(properties, HttpStatus.OK);
    }
}
