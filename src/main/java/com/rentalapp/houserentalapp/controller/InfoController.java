package com.rentalapp.houserentalapp.controller;

import com.rentalapp.houserentalapp.model.ApplicationInfoDto;
import com.rentalapp.houserentalapp.util.CustomResponseUtil;
import com.rentalapp.houserentalapp.util.ResponseObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequestMapping("/info")
public class InfoController {

    @Value("${application.version:1.0.0}")
    private String applicationVersion;

    @GetMapping("/version")
    public ResponseEntity<ResponseObject<ApplicationInfoDto>> getVersion() {
        log.info("Version API Called");
        ApplicationInfoDto info = new ApplicationInfoDto(
                applicationVersion,
                LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
//                activeProfile,
                "House Rental Application",
                "UP"
        );

        return CustomResponseUtil.getSuccessResponse(info, HttpStatus.OK);
    }

}
