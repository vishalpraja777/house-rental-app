package com.rentalapp.houserentalapp.dao;

import com.rentalapp.houserentalapp.model.entities.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
}
