package com.rentalapp.houserentalapp.service;

import com.rentalapp.houserentalapp.model.entities.PropertyMedia;
import com.rentalapp.houserentalapp.util.ResponseObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    ResponseEntity<ResponseObject<PropertyMedia>> uploadPropertyMedia(Long propertyId, MultipartFile file);

    ResponseEntity<ResponseObject<String>> deletePropertyMedia(Long mediaId);
}
