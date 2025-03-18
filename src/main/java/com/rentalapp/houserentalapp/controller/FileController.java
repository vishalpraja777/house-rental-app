package com.rentalapp.houserentalapp.controller;

import com.rentalapp.houserentalapp.model.entities.PropertyMedia;
import com.rentalapp.houserentalapp.service.FileService;
import com.rentalapp.houserentalapp.util.ResponseObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/file")
@Tag(name = "Media APIs", description = "Upload and Delete media APIs")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload-property-media")
    @Operation(summary = "Upload Property Media API")
    public ResponseEntity<ResponseObject<List<PropertyMedia>>> uploadPropertyMedia(@RequestParam Long propertyId,
                                                                             @RequestParam("file") List<MultipartFile> file) {
        log.info("Upload property media API called for propertyId: " + propertyId);
        return fileService.uploadPropertyMedia(propertyId, file);
    }

    @DeleteMapping("/delete-property-media/{mediaId}")
    @Operation(summary = "Delete Property Media API")
    public ResponseEntity<ResponseObject<String>> deletePropertyMedia(@PathVariable Long mediaId) {
        log.info("Delete property media API called for mediaId: " + mediaId);
        return fileService.deletePropertyMedia(mediaId);
    }

}
