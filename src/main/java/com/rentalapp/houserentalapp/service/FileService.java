package com.rentalapp.houserentalapp.service;

import com.rentalapp.houserentalapp.model.entities.PropertyMedia;
import com.rentalapp.houserentalapp.util.ResponseObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    ResponseEntity<ResponseObject<List<PropertyMedia>>> uploadPropertyMedia(Long propertyId, List<MultipartFile> file);

    ResponseEntity<ResponseObject<String>> deletePropertyMedia(Long mediaId);
}
