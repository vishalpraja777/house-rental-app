package com.rentalapp.houserentalapp.service.impl;

import com.rentalapp.houserentalapp.dao.PropertyRepository;
import com.rentalapp.houserentalapp.dao.UserRepository;
import com.rentalapp.houserentalapp.model.entities.Property;
import com.rentalapp.houserentalapp.model.entities.Users;
import com.rentalapp.houserentalapp.service.PropertyService;
import com.rentalapp.houserentalapp.util.CustomResponseUtil;
import com.rentalapp.houserentalapp.util.ResponseObject;
import com.rentalapp.houserentalapp.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PropertyServiceImpl implements PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<ResponseObject<Property>> addProperty(Property property) {
        UserDetails currentUser = SecurityUtil.getCurrentUser();
        if(currentUser == null) {
            return CustomResponseUtil.getFailureResponse("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        Users user = userRepository.findByUsername(currentUser.getUsername());

        if(!SecurityUtil.isUserActive(user)) {
            return CustomResponseUtil.getFailureResponse("User is Inactive", HttpStatus.UNAUTHORIZED);
        }

        try {
            property.setOwner(user);
            Property save = propertyRepository.save(property);
            return CustomResponseUtil.getSuccessResponse(save, HttpStatus.CREATED);
        } catch (Exception e) {
            return CustomResponseUtil.getFailureResponse("Error Creating Property", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<ResponseObject<Property>> getProperty(Long propertyId) {

        Optional<Property> optionalProperty = propertyRepository.findById(propertyId);

        return optionalProperty.map(property -> CustomResponseUtil.getSuccessResponse(property, HttpStatus.OK)).orElseGet(() -> CustomResponseUtil.getFailureResponse("Property Not Found", HttpStatus.NOT_FOUND));

    }
}
