package com.rentalapp.houserentalapp.dao;

import com.rentalapp.houserentalapp.model.entities.Favorite;
import com.rentalapp.houserentalapp.model.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUser(Users user);
}
