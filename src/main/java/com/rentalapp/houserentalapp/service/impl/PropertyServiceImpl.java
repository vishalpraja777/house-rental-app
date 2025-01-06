package com.rentalapp.houserentalapp.service.impl;

import com.rentalapp.houserentalapp.dao.PropertyRepository;
import com.rentalapp.houserentalapp.dao.UserRepository;
import com.rentalapp.houserentalapp.model.entities.Property;
import com.rentalapp.houserentalapp.model.entities.Users;
import com.rentalapp.houserentalapp.service.PropertyService;
import com.rentalapp.houserentalapp.util.Constants;
import com.rentalapp.houserentalapp.util.CustomResponseUtil;
import com.rentalapp.houserentalapp.util.ResponseObject;
import com.rentalapp.houserentalapp.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
            return CustomResponseUtil.getFailureResponse(Constants.USER_UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

        Users user = userRepository.findByUsername(currentUser.getUsername());

        if(!SecurityUtil.isUserActive(user)) {
            return CustomResponseUtil.getFailureResponse(Constants.USER_UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

        try {
            property.setOwner(user);
            Property save = propertyRepository.save(property);
            return CustomResponseUtil.getSuccessResponse(save, HttpStatus.CREATED);
        } catch (Exception e) {
            return CustomResponseUtil.getFailureResponse(Constants.ERROR_CREATING_PROPERTY, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<ResponseObject<Property>> getPropertyById(Long propertyId) {

        Optional<Property> optionalProperty = propertyRepository.findById(propertyId);

        return optionalProperty.map(property -> CustomResponseUtil.getSuccessResponse(property, HttpStatus.OK)).orElseGet(() -> CustomResponseUtil.getFailureResponse(Constants.PROPERTY_NOT_FOUND, HttpStatus.NOT_FOUND));

    }

    @Override
    public ResponseEntity<ResponseObject<Property>> updatePropertyById(Long propertyId, Property oldProperty) {

        Optional<Property> optionalProperty = propertyRepository.findById(propertyId);
        if(optionalProperty.isEmpty()) {
            return CustomResponseUtil.getFailureResponse(Constants.PROPERTY_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        Property propertyToUpdate = optionalProperty.get();
        if(!SecurityUtil.isUserAuthorized(propertyToUpdate.getOwner())) {
            return CustomResponseUtil.getFailureResponse(Constants.USER_UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

//        Update the required fields
        updatePropertyFields(propertyToUpdate, oldProperty);

        try {
            Property savedProperty = propertyRepository.save(propertyToUpdate);
            return CustomResponseUtil.getSuccessResponse(savedProperty, HttpStatus.OK);
        } catch (Exception e) {
            return CustomResponseUtil.getFailureResponse(Constants.ERROR_UPDATING_PROPERTY, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseObject<String>> deletePropertyById(Long propertyId) {

        Optional<Property> optionalProperty = propertyRepository.findById(propertyId);
        if(optionalProperty.isEmpty()) {
            return CustomResponseUtil.getFailureResponse(Constants.PROPERTY_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        Property propertyToDelete = optionalProperty.get();
        if(!SecurityUtil.isUserAuthorized(propertyToDelete.getOwner())) {
            return CustomResponseUtil.getFailureResponse(Constants.USER_UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

        try {
            propertyRepository.delete(propertyToDelete);
            return CustomResponseUtil.getSuccessResponse(Constants.PROPERTY_DELETED_SUCCESSFULLY, HttpStatus.OK);
        } catch (Exception e) {
            return CustomResponseUtil.getFailureResponse(Constants.UNABLE_TO_DELETE_PROPERTY, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void updatePropertyFields(Property propertyToUpdate, Property oldProperty) {

        if (oldProperty.getTitle() != null)
            propertyToUpdate.setTitle(oldProperty.getTitle());
        if (oldProperty.getDescription() != null)
            propertyToUpdate.setDescription(oldProperty.getDescription());
        if (oldProperty.getAddress() != null)
            propertyToUpdate.setAddress(oldProperty.getAddress());
        if (oldProperty.getCity() != null)
            propertyToUpdate.setCity(oldProperty.getCity());
        if (oldProperty.getState() != null)
            propertyToUpdate.setState(oldProperty.getState());
        if (oldProperty.getCountry() != null)
            propertyToUpdate.setCountry(oldProperty.getCountry());
        if (oldProperty.getPincode() != null)
            propertyToUpdate.setPincode(oldProperty.getPincode());
        if (oldProperty.getPrice() != null)
            propertyToUpdate.setPrice(oldProperty.getPrice());
        if (oldProperty.getSizeSqft() != null)
            propertyToUpdate.setSizeSqft(oldProperty.getSizeSqft());
        if (oldProperty.getPropertyType() != null)
            propertyToUpdate.setPropertyType(oldProperty.getPropertyType());
        if (oldProperty.getStatus() != null)
            propertyToUpdate.setStatus(oldProperty.getStatus());
        if (oldProperty.getLatitude() != null)
            propertyToUpdate.setLatitude(oldProperty.getLatitude());
        if (oldProperty.getLongitude() != null)
            propertyToUpdate.setLongitude(oldProperty.getLongitude());
        if (oldProperty.getGoogleMapsUrl() != null)
            propertyToUpdate.setGoogleMapsUrl(oldProperty.getGoogleMapsUrl());
        // Update the timestamp
        propertyToUpdate.setUpdatedAt(LocalDateTime.now());

    }
}
