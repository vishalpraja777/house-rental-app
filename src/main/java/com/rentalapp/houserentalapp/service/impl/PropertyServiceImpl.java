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
        if(currentUser == null || SecurityUtil.isRenter()) {
            return CustomResponseUtil.getFailureResponse(Constants.USER_UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

        Users user = userRepository.findByUsername(currentUser.getUsername());

        if(!SecurityUtil.isUserActive(user)) {
            return CustomResponseUtil.getFailureResponse(Constants.USER_INACTIVE, HttpStatus.UNAUTHORIZED);
        }

        try {
            property.setOwner(user);
            property.setCity(property.getCity().toUpperCase());
            property.setState(property.getState().toUpperCase());

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

    @Override
    public ResponseEntity<ResponseObject<List<Property>>> getCurrentUserProperties() {

        UserDetails currentUser = SecurityUtil.getCurrentUser();
        if (currentUser == null) {
            return CustomResponseUtil.getFailureResponse(Constants.USER_UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

        Users user = userRepository.findByUsername(currentUser.getUsername());
        if(!SecurityUtil.isUserActive(user)) {
            return CustomResponseUtil.getFailureResponse(Constants.USER_INACTIVE, HttpStatus.UNAUTHORIZED);
        }

        log.info("Getting Current User Properties for User: {}", user.getUsername());

        List<Property> properties = propertyRepository.findByOwnerUserId(user.getUserId());

        return CustomResponseUtil.getSuccessResponse(properties, HttpStatus.OK);
    }

    private void updatePropertyFields(Property propertyToUpdate, Property oldProperty) {

        if (oldProperty.getStatus() != null)
            propertyToUpdate.setStatus(oldProperty.getStatus());
        if (oldProperty.getLatitude() != null)
            propertyToUpdate.setLatitude(oldProperty.getLatitude());
        if (oldProperty.getLongitude() != null)
            propertyToUpdate.setLongitude(oldProperty.getLongitude());
        if (oldProperty.getGoogleMapsUrl() != null)
            propertyToUpdate.setGoogleMapsUrl(oldProperty.getGoogleMapsUrl());

// Property Details
        if (oldProperty.getPropertyType() != null)
            propertyToUpdate.setPropertyType(oldProperty.getPropertyType());
        if (oldProperty.getBhkType() != null)
            propertyToUpdate.setBhkType(oldProperty.getBhkType());
        if (oldProperty.getSizeSqft() != null)
            propertyToUpdate.setSizeSqft(oldProperty.getSizeSqft());
        if (oldProperty.getFacing() != null)
            propertyToUpdate.setFacing(oldProperty.getFacing());
        if (oldProperty.getPropertyAge() != null)
            propertyToUpdate.setPropertyAge(oldProperty.getPropertyAge());
        if (oldProperty.getFloorType() != null)
            propertyToUpdate.setFloorType(oldProperty.getFloorType());
        if (oldProperty.getFloorAt() != null)
            propertyToUpdate.setFloorAt(oldProperty.getFloorAt());
        if (oldProperty.getTotalFloors() != null)
            propertyToUpdate.setTotalFloors(oldProperty.getTotalFloors());

// Address Details
        if (oldProperty.getState() != null)
            propertyToUpdate.setState(oldProperty.getState());
        if (oldProperty.getCity() != null)
            propertyToUpdate.setCity(oldProperty.getCity());
        if (oldProperty.getPincode() != null)
            propertyToUpdate.setPincode(oldProperty.getPincode());
        if (oldProperty.getArea() != null)
            propertyToUpdate.setArea(oldProperty.getArea());
        if (oldProperty.getStreetAddress() != null)
            propertyToUpdate.setStreetAddress(oldProperty.getStreetAddress());

// Price Details
        if (oldProperty.getPrice() != null)
            propertyToUpdate.setPrice(oldProperty.getPrice());
        if (oldProperty.getNegotiable() != null)
            propertyToUpdate.setNegotiable(oldProperty.getNegotiable());
        if (oldProperty.getWaterBillIncluded() != null)
            propertyToUpdate.setWaterBillIncluded(oldProperty.getWaterBillIncluded());
        if (oldProperty.getElectricityBillIncluded() != null)
            propertyToUpdate.setElectricityBillIncluded(oldProperty.getElectricityBillIncluded());
        if (oldProperty.getAvailableFrom() != null)
            propertyToUpdate.setAvailableFrom(oldProperty.getAvailableFrom());
        if (oldProperty.getFurnishing() != null)
            propertyToUpdate.setFurnishing(oldProperty.getFurnishing());
        if (oldProperty.getKitchen() != null)
            propertyToUpdate.setKitchen(oldProperty.getKitchen());
        if (oldProperty.getPropertyDescription() != null)
            propertyToUpdate.setPropertyDescription(oldProperty.getPropertyDescription());

// Amenities Details
//        if (oldProperty.getAvailableFor() != null)
//            propertyToUpdate.setAvailableFor(oldProperty.getAvailableFor());
        if (oldProperty.getAvailableForMen() != null)
            propertyToUpdate.setAvailableForMen(oldProperty.getAvailableForMen());
        if (oldProperty.getAvailableForWomen() != null)
            propertyToUpdate.setAvailableForWomen(oldProperty.getAvailableForWomen());
        if (oldProperty.getAvailableForFamily() != null)
            propertyToUpdate.setAvailableForFamily(oldProperty.getAvailableForFamily());
        if (oldProperty.getNoOfBathrooms() != null)
            propertyToUpdate.setNoOfBathrooms(oldProperty.getNoOfBathrooms());
        if (oldProperty.getParking() != null)
            propertyToUpdate.setParking(oldProperty.getParking());

// Other Amenities
        if (oldProperty.getGatedSociety() != null)
            propertyToUpdate.setGatedSociety(oldProperty.getGatedSociety());
//        if (oldProperty.getNonVegAllowed() != null)
//            propertyToUpdate.setNonVegAllowed(oldProperty.getNonVegAllowed());
        if (oldProperty.getGym() != null)
            propertyToUpdate.setGym(oldProperty.getGym());
        if (oldProperty.getLift() != null)
            propertyToUpdate.setLift(oldProperty.getLift());
        if (oldProperty.getPark() != null)
            propertyToUpdate.setPark(oldProperty.getPark());
        if (oldProperty.getSwimmingPool() != null)
            propertyToUpdate.setSwimmingPool(oldProperty.getSwimmingPool());
        if (oldProperty.getPowerBackup() != null)
            propertyToUpdate.setPowerBackup(oldProperty.getPowerBackup());
//        if (oldProperty.getGasPipeline() != null)
//            propertyToUpdate.setGasPipeline(oldProperty.getGasPipeline());
        if (oldProperty.getAirConditioner() != null)
            propertyToUpdate.setAirConditioner(oldProperty.getAirConditioner());
        if (oldProperty.getWaterSupply() != null)
            propertyToUpdate.setWaterSupply(oldProperty.getWaterSupply());
        if (oldProperty.getWaterFilter() != null)
            propertyToUpdate.setWaterFilter(oldProperty.getWaterFilter());
        if (oldProperty.getGeyser() != null)
            propertyToUpdate.setGeyser(oldProperty.getGeyser());

//         Update the timestamp
        propertyToUpdate.setUpdatedAt(LocalDateTime.now());

    }
}
