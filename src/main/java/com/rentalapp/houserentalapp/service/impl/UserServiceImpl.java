package com.rentalapp.houserentalapp.service.impl;

import com.rentalapp.houserentalapp.auth.jwt.JWTService;
import com.rentalapp.houserentalapp.dao.UserRepository;
import com.rentalapp.houserentalapp.model.ChangePassword;
import com.rentalapp.houserentalapp.model.LoginUserDto;
import com.rentalapp.houserentalapp.model.RegisterUserDto;
import com.rentalapp.houserentalapp.model.entities.Users;
import com.rentalapp.houserentalapp.service.UserService;
import com.rentalapp.houserentalapp.util.Constants;
import com.rentalapp.houserentalapp.util.ResponseObject;
import com.rentalapp.houserentalapp.util.CustomResponseUtil;
import com.rentalapp.houserentalapp.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Slf4j
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
    public ResponseEntity<ResponseObject<Users>> getLoggedInUser() {
        UserDetails currentUser = SecurityUtil.getCurrentUser();
        if(currentUser == null || currentUser.getUsername() == null) {
            return CustomResponseUtil.getFailureResponse(Constants.USER_UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }
        try {
            Users user = userRepository.findByUsername(currentUser.getUsername());

            if(!SecurityUtil.isUserActive(user)) {
                return CustomResponseUtil.getFailureResponse(Constants.USER_INACTIVE, HttpStatus.UNAUTHORIZED);
            }

            return CustomResponseUtil.getSuccessResponse(user, HttpStatus.OK);
        } catch (Exception e) {
            log.error(Constants.ERROR_GETTING_USER + ", possible cause: " + e.getMessage());
            return CustomResponseUtil.getFailureResponse(Constants.ERROR_GETTING_USER, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseObject<Users>> register(RegisterUserDto user) {

        if((userRepository.findByUsername(user.getUsername()) != null) ||
                (userRepository.findByEmail(user.getEmail()) != null) ||
                (userRepository.findByPhone(user.getPhone()) != null)) {
            log.error(Constants.USER_ALREADY_EXISTS + " : " + user.getUsername());
            return CustomResponseUtil.getFailureResponse(Constants.USER_ALREADY_EXISTS, HttpStatus.CONFLICT);
        }


        try {
//            Admin cannot be registered by default
            if(user.getRole().equals(Users.Role.ADMIN)) {
                throw new DataIntegrityViolationException("Admin cannot be registered by default");
            }

            Users userToSave = new Users();
            addUserFiledsToRegister(userToSave, user);
            userToSave.setPassword(passwordEncoder.encode(user.getPassword()));

            Users savedUser = userRepository.save(userToSave);

            return CustomResponseUtil.getSuccessResponse(savedUser, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            log.error(Constants.ERROR_CREATING_USER + ", possible cause: " + Constants.ADMIN_CANNOT_BE_REGISTERED_BY_DEFAULT);
            return CustomResponseUtil.getFailureResponse(Constants.ADMIN_CANNOT_BE_REGISTERED_BY_DEFAULT, HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            log.error(Constants.ERROR_CREATING_USER + ", possible cause: " + e.getMessage());
            return CustomResponseUtil.getFailureResponse(Constants.ERROR_CREATING_USER, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public ResponseEntity<ResponseObject<String>> login(LoginUserDto user) {

        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            if(authentication.isAuthenticated()) {
                Users databaseUser = userRepository.findByUsername(user.getUsername());
                String token = jwtService.generateToken(databaseUser);

                return CustomResponseUtil.getSuccessResponse(token, HttpStatus.OK);
            } else {
                throw new BadCredentialsException(Constants.INVALID_CREDENTIALS);
            }
        } catch (BadCredentialsException e) {
            log.error(Constants.INVALID_CREDENTIALS + ", possible cause: " + e.getMessage());
            return CustomResponseUtil.getFailureResponse(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error(Constants.ERROR_LOGGING_IN_USER + ", possible cause: " + e.getMessage());
            return CustomResponseUtil.getFailureResponse(Constants.ERROR_LOGGING_IN_USER, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseObject<Users>> getUserById(Long userId) {
        try {
            Optional<Users> optioanlUser = userRepository.findById(userId);
            if (optioanlUser.isPresent()) {
                Users user = optioanlUser.get();
                return CustomResponseUtil.getSuccessResponse(user, HttpStatus.OK);
            }
            log.error(Constants.USER_NOT_FOUND + " : " + userId);
            return CustomResponseUtil.getFailureResponse(Constants.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(Constants.ERROR_GETTING_USER + ", possible cause: " + e.getMessage());
            return CustomResponseUtil.getFailureResponse(Constants.ERROR_GETTING_USER, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseObject<Users>> updateUserById(Long userId, Users oldUser) {

        Optional<Users> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) {
            log.error(Constants.USER_NOT_FOUND + " : " + userId);
            return CustomResponseUtil.getFailureResponse(Constants.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        Users userToUpdate = optionalUser.get();
        if(!SecurityUtil.isUserAuthorized(userToUpdate)) {
            log.error(Constants.USER_UNAUTHORIZED + " : " + Objects.requireNonNull(SecurityUtil.getCurrentUser()).getUsername());
            return CustomResponseUtil.getFailureResponse(Constants.USER_UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

//        Update the required fields
        updateUserFields(userToUpdate, oldUser);

        try {
            Users savedUser = userRepository.save(userToUpdate);
            return CustomResponseUtil.getSuccessResponse(savedUser, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            log.error(Constants.USER_ALREADY_EXISTS + " : Username: " + oldUser.getUsername() + ", Email: " + oldUser.getEmail() + ", Phone: " + oldUser.getPhone());
            return CustomResponseUtil.getFailureResponse(Constants.USER_ALREADY_EXISTS, HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(Constants.ERROR_UPDATING_USER + ", possible cause: " + e.getMessage());
            return CustomResponseUtil.getFailureResponse(Constants.ERROR_UPDATING_USER, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseObject<String>> changeUserStatus(Long userId, Users.Status status) {

        Optional<Users> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) {
            log.error(Constants.USER_NOT_FOUND + " : " + userId);
            return CustomResponseUtil.getFailureResponse(Constants.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if(!SecurityUtil.isUserAuthorized(optionalUser.get())) {
            log.error(Constants.USER_UNAUTHORIZED + " : " + Objects.requireNonNull(SecurityUtil.getCurrentUser()).getUsername());
            return CustomResponseUtil.getFailureResponse(Constants.USER_UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

        try {
            userRepository.updateStatus(status, userId);
            return CustomResponseUtil.getSuccessResponse(Constants.USER_STATUS_CHANGED, HttpStatus.OK);
        } catch (Exception e) {
            log.error(Constants.ERROR_UPDATING_USER + ", possible cause: " + e.getMessage());
            return CustomResponseUtil.getFailureResponse(Constants.ERROR_UPDATING_USER, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseObject<String>> changePassword(Long userId, ChangePassword changePassword) {

        Optional<Users> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) {log.error(Constants.USER_NOT_FOUND + " : " + userId);
            return CustomResponseUtil.getFailureResponse(Constants.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if(!SecurityUtil.isUserAuthorized(optionalUser.get())) {
            log.error(Constants.USER_UNAUTHORIZED + " : " + Objects.requireNonNull(SecurityUtil.getCurrentUser()).getUsername());
            return CustomResponseUtil.getFailureResponse(Constants.USER_UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

        try {
//            if(encodedPassword.equals(optionalUser.get().getPassword())) {
            if(passwordEncoder.matches(changePassword.getOldPassword(), optionalUser.get().getPassword())) {
                userRepository.changePassword(passwordEncoder.encode(changePassword.getNewPassword()), userId);
                return CustomResponseUtil.getSuccessResponse(Constants.PASSWORD_CHANGED, HttpStatus.OK);
            }
            log.error(Constants.OLD_PASSWORD_IS_INCORRECT + ", for User : " + userId);
            return CustomResponseUtil.getFailureResponse(Constants.OLD_PASSWORD_IS_INCORRECT, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error(Constants.ERROR_CHANGING_PASSWORD + ", possible cause: " + e.getMessage());
            return CustomResponseUtil.getFailureResponse(Constants.ERROR_CHANGING_PASSWORD, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
