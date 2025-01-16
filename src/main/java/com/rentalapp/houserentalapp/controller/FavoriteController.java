package com.rentalapp.houserentalapp.controller;

import com.rentalapp.houserentalapp.model.entities.Favorite;
import com.rentalapp.houserentalapp.service.FavoriteService;
import com.rentalapp.houserentalapp.util.ResponseObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/favorites")
@Tag(name = "Favorite Properties APIs", description = "Add, Remove and Get Favorite Properties APIs")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/add")
    public ResponseEntity<ResponseObject<Favorite>> addFavorite(@RequestParam Long propertyId) {
        log.info("Add favorite Property API called for propertyId: " + propertyId);
        return favoriteService.addFavorite(propertyId);
    }

    @DeleteMapping("/remove/{favoriteId}")
    public ResponseEntity<ResponseObject<String>> removeFavorite(@PathVariable Long favoriteId) {
        log.info("Delete favorite Property API called for favoriteId: " + favoriteId);
        return favoriteService.removeFavorite(favoriteId);
    }

    @GetMapping("/get-by-user")
    public ResponseEntity<ResponseObject<List<Favorite>>> getFavoriteByCurrentUser() {
        log.info("Get All favorite Property API called");
        return favoriteService.getFavoriteByCurrentUser();
    }

}
