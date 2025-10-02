package com.rentalapp.houserentalapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationInfoDto {
    private String version;
    private String buildTimestamp;
    //    private String environment;
    private String description;
    private String apiStatus;
}
