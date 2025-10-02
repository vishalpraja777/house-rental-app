package com.rentalapp.houserentalapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HouseRentalAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(HouseRentalAppApplication.class, args);
    }

}
