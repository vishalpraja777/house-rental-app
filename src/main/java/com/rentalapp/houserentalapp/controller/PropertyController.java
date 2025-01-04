package com.rentalapp.houserentalapp.controller;

import com.rentalapp.houserentalapp.model.entities.Property;
import com.rentalapp.houserentalapp.service.PropertyService;
import com.rentalapp.houserentalapp.util.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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

}
