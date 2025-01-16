package com.rentalapp.houserentalapp.service;

import com.rentalapp.houserentalapp.model.entities.Favorite;
import com.rentalapp.houserentalapp.util.ResponseObject;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FavoriteService {
    ResponseEntity<ResponseObject<Favorite>> addFavorite(Long propertyId);

    ResponseEntity<ResponseObject<String>> removeFavorite(Long favoriteId);

    ResponseEntity<ResponseObject<List<Favorite>>> getFavoriteByCurrentUser();
}
