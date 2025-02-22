package com.rentalapp.houserentalapp.dao;

import com.rentalapp.houserentalapp.model.entities.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query("SELECT p FROM Property p " +
            "WHERE (:city IS NULL OR p.city = :city) " +
            "AND (:priceMin IS NULL OR p.price >= :priceMin) " +
            "AND (:priceMax IS NULL OR p.price <= :priceMax) " +
            "AND (:type IS NULL OR p.propertyType = :type) " +
            "AND (6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) * " +
            "cos(radians(p.longitude) - radians(:longitude)) + sin(radians(:latitude)) * " +
            "sin(radians(p.latitude)))) <= :radius")
    List<Property> searchProperties(String city, BigDecimal priceMin, BigDecimal priceMax, Property.PropertyType type, Double latitude, Double longitude, Double radius);

    List<Property> findByOwnerUserId(Long userId);
}
