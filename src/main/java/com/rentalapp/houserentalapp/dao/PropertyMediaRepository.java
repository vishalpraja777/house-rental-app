package com.rentalapp.houserentalapp.dao;

import com.rentalapp.houserentalapp.model.entities.PropertyMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyMediaRepository extends JpaRepository<PropertyMedia, Long> {

    List<PropertyMedia> findByPropertyPropertyId(Long propertyId);

    List<PropertyMedia> findByPropertyPropertyIdIn(List<Long> propertyIds);

}
