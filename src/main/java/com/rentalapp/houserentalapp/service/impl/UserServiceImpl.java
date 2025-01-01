package com.rentalapp.houserentalapp.service.impl;

import com.rentalapp.houserentalapp.auth.jwt.JWTService;
import com.rentalapp.houserentalapp.dao.UserRepository;
import com.rentalapp.houserentalapp.model.Users;
import com.rentalapp.houserentalapp.service.UserService;
import com.rentalapp.houserentalapp.util.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

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

        ResponseObject<Users> responseObject = new ResponseObject<>();

        if((userRepository.findByUsername(user.getUsername()) != null) ||
                (userRepository.findByEmail(user.getEmail()) != null) ||
                (userRepository.findByPhone(user.getPhone()) != null)) {
            responseObject.setError("User already exists");
            responseObject.setOperation(ResponseObject.Operation.FAILURE);
            return new ResponseEntity<>(responseObject, HttpStatus.CONFLICT);
        }

        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            responseObject.setData(userRepository.save(user));
            responseObject.setOperation(ResponseObject.Operation.SUCCESS);
            return new ResponseEntity<>(responseObject, HttpStatus.CREATED);
        } catch (Exception e) {
            responseObject.setError("Error creating user");
            responseObject.setOperation(ResponseObject.Operation.FAILURE);
            return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseObject<String>> login(Users user) {

        ResponseObject<String> responseObject = new ResponseObject<>();
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            if(authentication.isAuthenticated()) {
                String token = jwtService.generateToken(user.getUsername());

                responseObject.setData(token);
                responseObject.setOperation(ResponseObject.Operation.SUCCESS);
                return new ResponseEntity<>(responseObject, HttpStatus.OK);
            } else {
                throw new BadCredentialsException("Invalid Credentials");
            }
        } catch (BadCredentialsException e) {
            responseObject.setError(e.getMessage());
            responseObject.setOperation(ResponseObject.Operation.FAILURE);
            return new ResponseEntity<>(responseObject, HttpStatus.UNAUTHORIZED);
        }
    }
}
