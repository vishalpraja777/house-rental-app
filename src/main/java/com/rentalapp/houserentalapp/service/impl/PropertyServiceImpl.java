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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
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
            property.setCity(property.getCity().toUpperCase());
            property.setState(property.getState().toUpperCase());
            property.setCountry(property.getCountry().toUpperCase());

            Property save = propertyRepository.save(property);
            return CustomResponseUtil.getSuccessResponse(save, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error(Constants.ERROR_CREATING_PROPERTY + ", possible cause: " + e.getMessage());
            return CustomResponseUtil.getFailureResponse(Constants.ERROR_CREATING_PROPERTY, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<ResponseObject<Property>> getPropertyById(Long propertyId) {

        try {
            Optional<Property> optionalProperty = propertyRepository.findById(propertyId);

            return optionalProperty.map(property -> CustomResponseUtil.getSuccessResponse(property, HttpStatus.OK)).orElseGet(() -> CustomResponseUtil.getFailureResponse(Constants.PROPERTY_NOT_FOUND, HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            log.error(Constants.ERROR_GETTING_PROPERTY + ", possible cause: " + e.getMessage());
            return CustomResponseUtil.getFailureResponse(Constants.ERROR_GETTING_PROPERTY, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
            log.error(Constants.ERROR_UPDATING_PROPERTY + ", possible cause: " + e.getMessage());
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
            log.error(Constants.ERROR_DELETING_PROPERTY + ", possible cause: " + e.getMessage());
            return CustomResponseUtil.getFailureResponse(Constants.ERROR_DELETING_PROPERTY, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseObject<List<Property>>> searchProperties(String city, BigDecimal priceMin, BigDecimal priceMax, Property.PropertyType type, Double latitude, Double longitude, Double radius) {
        city = (city == null || city.isEmpty()) ? null : city.toUpperCase();
        try {
            List<Property> properties = propertyRepository.searchProperties(city, priceMin, priceMax, type, latitude, longitude, radius);

            return CustomResponseUtil.getSuccessResponse(properties, HttpStatus.OK);
        } catch (Exception e) {
            log.error(Constants.ERROR_SEARCHING_PROPERTY + ", possible cause: " + e.getMessage());
            return CustomResponseUtil.getFailureResponse(Constants.ERROR_SEARCHING_PROPERTY, HttpStatus.INTERNAL_SERVER_ERROR);
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
            propertyToUpdate.setCity(oldProperty.getCity().toUpperCase());
        if (oldProperty.getState() != null)
            propertyToUpdate.setState(oldProperty.getState().toUpperCase());
        if (oldProperty.getCountry() != null)
            propertyToUpdate.setCountry(oldProperty.getCountry().toUpperCase());
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
