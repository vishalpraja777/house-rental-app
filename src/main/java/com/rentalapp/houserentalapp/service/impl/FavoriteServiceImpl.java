package com.rentalapp.houserentalapp.service.impl;

import com.rentalapp.houserentalapp.dao.FavoriteRepository;
import com.rentalapp.houserentalapp.dao.PropertyRepository;
import com.rentalapp.houserentalapp.dao.UserRepository;
import com.rentalapp.houserentalapp.model.entities.Favorite;
import com.rentalapp.houserentalapp.model.entities.Property;
import com.rentalapp.houserentalapp.model.entities.Users;
import com.rentalapp.houserentalapp.service.FavoriteService;
import com.rentalapp.houserentalapp.util.Constants;
import com.rentalapp.houserentalapp.util.CustomResponseUtil;
import com.rentalapp.houserentalapp.util.ResponseObject;
import com.rentalapp.houserentalapp.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FavoriteServiceImpl implements FavoriteService {

    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final FavoriteRepository favoriteRepository;

    public FavoriteServiceImpl(UserRepository userRepository, PropertyRepository propertyRepository, FavoriteRepository favoriteRepository) {
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    public ResponseEntity<ResponseObject<Favorite>> addFavorite(Long propertyId) {

        UserDetails currentUser = SecurityUtil.getCurrentUser();
        if (currentUser == null) {
            return CustomResponseUtil.getFailureResponse(Constants.USER_UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

        Users user = userRepository.findByUsername(currentUser.getUsername());
        if(!SecurityUtil.isUserActive(user)) {
            return CustomResponseUtil.getFailureResponse(Constants.USER_INACTIVE, HttpStatus.UNAUTHORIZED);
        }

        Optional<Property> optionalProperty = propertyRepository.findById(propertyId);
        if(optionalProperty.isEmpty()) {
            return CustomResponseUtil.getFailureResponse(Constants.PROPERTY_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setProperty(optionalProperty.get());

        try {
            Favorite savedFavorite = favoriteRepository.save(favorite);
            return CustomResponseUtil.getSuccessResponse(savedFavorite, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            log.error(Constants.PROPERTY_ALREADY_IN_FAVORITES);
            return CustomResponseUtil.getFailureResponse(Constants.PROPERTY_ALREADY_IN_FAVORITES, HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(Constants.ERROR_ADDING_PROPERTY_TO_FAVORITE + ", possible cause: " + e.getMessage());
            return CustomResponseUtil.getFailureResponse(Constants.ERROR_ADDING_PROPERTY_TO_FAVORITE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseObject<String>> removeFavorite(Long favoriteId) {
        try {
            Optional<Favorite> optionalFavorite = favoriteRepository.findById(favoriteId);
            if(optionalFavorite.isEmpty()) {
                return CustomResponseUtil.getFailureResponse(Constants.FAVORITE_NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            Favorite favorite = optionalFavorite.get();
            if(!SecurityUtil.isUserAuthorized(favorite.getUser())) {
                return CustomResponseUtil.getFailureResponse(Constants.USER_UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
            }
            favoriteRepository.delete(favorite);
            return CustomResponseUtil.getSuccessResponse(Constants.FAVORITE_DELETED_SUCCESSFULLY, HttpStatus.OK);
        } catch (Exception e) {
            log.error(Constants.ERROR_DELETING_FAVORITE + ", possible cause: " + e.getMessage());
            return CustomResponseUtil.getFailureResponse(Constants.ERROR_DELETING_FAVORITE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseObject<List<Favorite>>> getFavoriteByCurrentUser() {

        UserDetails currentUser = SecurityUtil.getCurrentUser();
        if (currentUser == null) {
            return CustomResponseUtil.getFailureResponse(Constants.USER_UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

        Users user = userRepository.findByUsername(currentUser.getUsername());
        if(!SecurityUtil.isUserActive(user)) {
            return CustomResponseUtil.getFailureResponse(Constants.USER_INACTIVE, HttpStatus.UNAUTHORIZED);
        }

        List<Favorite> favorites = favoriteRepository.findByUser(user);

        return CustomResponseUtil.getSuccessResponse(favorites, HttpStatus.OK);

    }
}
