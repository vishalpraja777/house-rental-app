package com.rentalapp.houserentalapp.service.impl;

import com.rentalapp.houserentalapp.dao.PropertyMediaRepository;
import com.rentalapp.houserentalapp.dao.PropertyRepository;
import com.rentalapp.houserentalapp.model.entities.Property;
import com.rentalapp.houserentalapp.model.entities.PropertyMedia;
import com.rentalapp.houserentalapp.service.FileService;
import com.rentalapp.houserentalapp.util.Constants;
import com.rentalapp.houserentalapp.util.CustomResponseUtil;
import com.rentalapp.houserentalapp.util.ResponseObject;
import com.rentalapp.houserentalapp.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private PropertyMediaRepository propertyMediaRepository;

    @Override
    public ResponseEntity<ResponseObject<List<PropertyMedia>>> uploadPropertyMedia(Long propertyId, List<MultipartFile> files) {

        List<PropertyMedia> propertyMediaList = new ArrayList<>();
        List<MultipartFile> validFiles = new ArrayList<>();

        for(MultipartFile file : files) {
            if (!(file.isEmpty() || (file.getContentType() == null
                    || !(file.getContentType().startsWith("image") || file.getContentType().startsWith("video"))))) {
                validFiles.add(file);
//                return CustomResponseUtil.getFailureResponse(Constants.INCORRECT_IMAGE_OR_VIDEO_FILE, HttpStatus.BAD_REQUEST);
            }
        }

        Optional<Property> optionalProperty = propertyRepository.findById(propertyId);
        if(optionalProperty.isEmpty()) {
            return CustomResponseUtil.getFailureResponse(Constants.PROPERTY_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        if(!SecurityUtil.isUserAuthorized(optionalProperty.get().getOwner())) {
            log.error(Constants.USER_UNAUTHORIZED + " : " + Objects.requireNonNull(SecurityUtil.getCurrentUser()).getUsername());
            return CustomResponseUtil.getFailureResponse(Constants.USER_UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }
        for(MultipartFile file : validFiles) {
            try {
//            TODO: Upload file to cloud
                String mediaUrl = "URL"; // cloudService.uploadFile(file);

//            Get Media Type
                PropertyMedia.MediaType mediaType = file.getContentType().startsWith("video") ? PropertyMedia.MediaType.VIDEO : PropertyMedia.MediaType.IMAGE;

                PropertyMedia propertyMedia = new PropertyMedia();
                propertyMedia.setProperty(optionalProperty.get());
                propertyMedia.setMediaUrl(mediaUrl);
                propertyMedia.setMediaType(mediaType);

                PropertyMedia savedPropertyMedia = propertyMediaRepository.save(propertyMedia);
                propertyMediaList.add(savedPropertyMedia);
            } catch (Exception e) {
                log.error(Constants.ERROR_UPLOADING_PROPERTY_MEDIA + " " + file.getName() + ", possible cause: " + e.getMessage());
            }
        }
            return CustomResponseUtil.getSuccessResponse(propertyMediaList, HttpStatus.CREATED);
//        } catch (Exception e) {
//            log.error(Constants.ERROR_UPLOADING_PROPERTY_MEDIA + ", possible cause: " + e.getMessage());
//            return CustomResponseUtil.getFailureResponse(Constants.ERROR_UPLOADING_PROPERTY_MEDIA, HttpStatus.INTERNAL_SERVER_ERROR);
//        }

    }

    @Override
    public ResponseEntity<ResponseObject<String>> deletePropertyMedia(Long mediaId) {

        Optional<PropertyMedia> optionalPropertyMedia = propertyMediaRepository.findById(mediaId);

        if (optionalPropertyMedia.isEmpty()) {
            return CustomResponseUtil.getFailureResponse(Constants.PROPERTY_MEDIA_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        PropertyMedia propertyMedia = optionalPropertyMedia.get();
        Optional<Property> optionalProperty = propertyRepository.findById(propertyMedia.getProperty().getPropertyId());
        if (optionalProperty.isEmpty()) {
            return CustomResponseUtil.getFailureResponse(Constants.PROPERTY_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if(!SecurityUtil.isUserAuthorized(optionalProperty.get().getOwner())) {
            log.error(Constants.USER_UNAUTHORIZED + " : " + Objects.requireNonNull(SecurityUtil.getCurrentUser()).getUsername());
            return CustomResponseUtil.getFailureResponse(Constants.USER_UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

        try {

//            TODO: Delete file from cloud
//            cloudService.deleteFile(propertyMedia.getMediaUrl());

            propertyMediaRepository.delete(propertyMedia);
            return CustomResponseUtil.getSuccessResponse(Constants.PROPERTY_MEDIA_DELETED_SUCCESSFULLY, HttpStatus.OK);
        } catch (Exception e) {
            log.error(Constants.ERROR_DELETING_PROPERTY_MEDIA + ", possible cause: " + e.getMessage());
            return CustomResponseUtil.getFailureResponse(Constants.ERROR_DELETING_PROPERTY_MEDIA, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
