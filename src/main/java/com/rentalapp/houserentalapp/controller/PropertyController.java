package com.rentalapp.houserentalapp.controller;

import com.rentalapp.houserentalapp.model.entities.Property;
import com.rentalapp.houserentalapp.service.PropertyService;
import com.rentalapp.houserentalapp.util.ResponseObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/properties")
@Tag(name = "Property APIs", description = "Adding, Updating, Getting and Deleting Property APIs")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @PostMapping("/add-property")
    @Operation(summary = "Add Property API")
    public ResponseEntity<ResponseObject<Property>> addProperty(@RequestBody Property property) {
        log.info("Add Property API Called for Property: {}", property.getTitle());
        return propertyService.addProperty(property);
    }

    @GetMapping("/get-property/{propertyId}")
    @Operation(summary = "Get Property By Id API")
    public ResponseEntity<ResponseObject<Property>> getPropertyById(@PathVariable Long propertyId) {
        log.info("Get Property API Called for Property: {}", propertyId);
        return propertyService.getPropertyById(propertyId);
    }

    @PutMapping("/update-property/{propertyId}")
    @Operation(summary = "Update Propert By Id API")
    public ResponseEntity<ResponseObject<Property>> updatePropertyById(@PathVariable Long propertyId, @RequestBody Property property) {
        log.info("Update Property API Called for Property: {}", propertyId);
        return propertyService.updatePropertyById(propertyId, property);
    }

    @DeleteMapping("/delete-property/{propertyId}")
    @Operation(summary = "Delete Property By Id API")
    public ResponseEntity<ResponseObject<String>> deletePropertyById(@PathVariable Long propertyId) {
        log.info("Delete Property API Called for Property: {}", propertyId);
        return propertyService.deletePropertyById(propertyId);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseObject<List<Property>>> searchProperties(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) BigDecimal priceMin,
            @RequestParam(required = false) BigDecimal priceMax,
            @RequestParam(required = false) Property.PropertyType type,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false, defaultValue = "5") Double radius) {

        log.info("Search Property API Called for city: {}, priceMin: {}, priceMax: {}, type: {}, latitude: {}, longitude: {}, radius: {}", city, priceMin, priceMax, type, latitude, longitude, radius);
        return propertyService.searchProperties(city, priceMin, priceMax, type, latitude, longitude, radius);

    }

}
