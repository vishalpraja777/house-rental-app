package com.rentalapp.houserentalapp.service;

import com.rentalapp.houserentalapp.model.entities.Property;
import com.rentalapp.houserentalapp.util.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface PropertyService {
    ResponseEntity<ResponseObject<Property>> addProperty(Property property);

    ResponseEntity<ResponseObject<Property>> getPropertyById(Long propertyId);

    ResponseEntity<ResponseObject<Property>> updatePropertyById(Long propertyId, Property property);

    ResponseEntity<ResponseObject<String>> deletePropertyById(Long propertyId);
}
