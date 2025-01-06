package com.rentalapp.houserentalapp.service.impl;

import com.rentalapp.houserentalapp.auth.jwt.JWTService;
import com.rentalapp.houserentalapp.dao.UserRepository;
import com.rentalapp.houserentalapp.model.ChangePassword;
import com.rentalapp.houserentalapp.model.entities.Users;
import com.rentalapp.houserentalapp.service.UserService;
import com.rentalapp.houserentalapp.util.Constants;
import com.rentalapp.houserentalapp.util.ResponseObject;
import com.rentalapp.houserentalapp.util.CustomResponseUtil;
import com.rentalapp.houserentalapp.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl extends UserServiceBaseImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JWTService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public ResponseEntity<ResponseObject<Users>> register(Users user) {

        if((userRepository.findByUsername(user.getUsername()) != null) ||
                (userRepository.findByEmail(user.getEmail()) != null) ||
                (userRepository.findByPhone(user.getPhone()) != null)) {

            return CustomResponseUtil.getFailureResponse(Constants.USER_ALREADY_EXISTS, HttpStatus.CONFLICT);
        }

        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setStatus(Users.Status.ACTIVE);

            Users savedUser = userRepository.save(user);

            return CustomResponseUtil.getSuccessResponse(savedUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return CustomResponseUtil.getFailureResponse(Constants.ERROR_CREATING_USER, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseObject<String>> login(Users user) {

        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            if(authentication.isAuthenticated()) {
                String token = jwtService.generateToken(user.getUsername());

                return CustomResponseUtil.getSuccessResponse(token, HttpStatus.OK);
            } else {
                throw new BadCredentialsException(Constants.INVALID_CREDENTIALS);
            }
        } catch (BadCredentialsException e) {
            return CustomResponseUtil.getFailureResponse(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public ResponseEntity<ResponseObject<Users>> getUserById(Long userId) {
        Optional<Users> optioanlUser = userRepository.findById(userId);
        if(optioanlUser.isPresent()) {
            Users user = optioanlUser.get();
            return CustomResponseUtil.getSuccessResponse(user, HttpStatus.OK);
        }
        return CustomResponseUtil.getFailureResponse(Constants.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<ResponseObject<Users>> updateUserById(Long userId, Users oldUser) {

        Optional<Users> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) {
            return CustomResponseUtil.getFailureResponse(Constants.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        Users userToUpdate = optionalUser.get();
        if(!SecurityUtil.isUserAuthorized(userToUpdate)) {
            return CustomResponseUtil.getFailureResponse(Constants.USER_UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

//        Update the required fields
        updateUserFields(userToUpdate, oldUser);

        try {
            Users savedUser = userRepository.save(userToUpdate);
            return CustomResponseUtil.getSuccessResponse(savedUser, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return CustomResponseUtil.getFailureResponse(Constants.USER_ALREADY_EXISTS, HttpStatus.CONFLICT);
        } catch (Exception e) {
            return CustomResponseUtil.getFailureResponse(Constants.ERROR_UPDATING_USER, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseObject<String>> changeUserStatus(Long userId, Users user) {

        Optional<Users> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) {
            return CustomResponseUtil.getFailureResponse(Constants.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if(!SecurityUtil.isUserAuthorized(optionalUser.get())) {
            return CustomResponseUtil.getFailureResponse(Constants.USER_UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

        try {
            userRepository.updateStatus(user.getStatus().toString(), userId);
            return CustomResponseUtil.getSuccessResponse(Constants.USER_STATUS_CHANGED, HttpStatus.OK);
        } catch (Exception e) {
            return CustomResponseUtil.getFailureResponse(Constants.ERROR_UPDATING_USER, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseObject<String>> changePassword(Long userId, ChangePassword changePassword) {

        Optional<Users> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) {
            return CustomResponseUtil.getFailureResponse(Constants.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if(!SecurityUtil.isUserAuthorized(optionalUser.get())) {
            return CustomResponseUtil.getFailureResponse(Constants.USER_UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

        try {
//            if(encodedPassword.equals(optionalUser.get().getPassword())) {
            if(passwordEncoder.matches(changePassword.getOldPassword(), optionalUser.get().getPassword())) {
                userRepository.changePassword(passwordEncoder.encode(changePassword.getNewPassword()), userId);
                return CustomResponseUtil.getSuccessResponse(Constants.PASSWORD_CHANGED, HttpStatus.OK);
            }
            return CustomResponseUtil.getFailureResponse(Constants.OLD_PASSWORD_IS_INCORRECT, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return CustomResponseUtil.getFailureResponse(Constants.ERROR_CHANGING_PASSWORD, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
