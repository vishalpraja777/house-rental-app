package com.rentalapp.houserentalapp.controller;

import com.rentalapp.houserentalapp.model.entities.Property;
import com.rentalapp.houserentalapp.service.PropertyService;
import com.rentalapp.houserentalapp.util.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @PostMapping("/add-property")
    public ResponseEntity<ResponseObject<Property>> addProperty(@RequestBody Property property) {
        return propertyService.addProperty(property);
    }

    @GetMapping("/get-property/{propertyId}")
    public ResponseEntity<ResponseObject<Property>> getPropertyById(@PathVariable Long propertyId) {
        return propertyService.getPropertyById(propertyId);
    }

    @PutMapping("/update-property/{propertyId}")
    public ResponseEntity<ResponseObject<Property>> updatePropertyById(@PathVariable Long propertyId, @RequestBody Property property) {
        return propertyService.updatePropertyById(propertyId, property);
    }

    @DeleteMapping("/delete-property/{propertyId}")
    public ResponseEntity<ResponseObject<String>> deletePropertyById(@PathVariable Long propertyId) {
        return propertyService.deletePropertyById(propertyId);
    }

}
