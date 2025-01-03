package com.rentalapp.houserentalapp.service.impl;

import com.rentalapp.houserentalapp.auth.jwt.JWTService;
import com.rentalapp.houserentalapp.dao.UserRepository;
import com.rentalapp.houserentalapp.model.Users;
import com.rentalapp.houserentalapp.service.UserService;
import com.rentalapp.houserentalapp.util.ResponseObject;
import com.rentalapp.houserentalapp.util.CustomResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
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

            return CustomResponseUtil.getFailureResponse("User already exists", HttpStatus.CONFLICT);
        }

        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setStatus(Users.Status.ACTIVE);

            Users savedUser = userRepository.save(user);

            return CustomResponseUtil.getSuccessResponse(savedUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return CustomResponseUtil.getFailureResponse("Error creating user", HttpStatus.INTERNAL_SERVER_ERROR);
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
                throw new BadCredentialsException("Invalid Credentials");
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
        return CustomResponseUtil.getFailureResponse("User not found", HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<ResponseObject<Users>> updateUserById(Long userId, Users oldUser) {

        if(!isUserAuthorized(oldUser)) {
            return CustomResponseUtil.getFailureResponse("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        Optional<Users> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) {
            return CustomResponseUtil.getFailureResponse("User not found", HttpStatus.NOT_FOUND);
        }

//        Update the required fields
        Users userToUpdate = updateUserFields(optionalUser.get(), oldUser);

        try {
            Users savedUser = userRepository.save(userToUpdate);

            return CustomResponseUtil.getSuccessResponse(savedUser, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return CustomResponseUtil.getFailureResponse("Email or Phone already exists", HttpStatus.CONFLICT);
        } catch (Exception e) {
            return CustomResponseUtil.getFailureResponse("Error updating user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
