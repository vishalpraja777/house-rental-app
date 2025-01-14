package com.rentalapp.houserentalapp.dao;

import com.rentalapp.houserentalapp.model.entities.PropertyMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyMediaRepository extends JpaRepository<PropertyMedia, Long> {
}
