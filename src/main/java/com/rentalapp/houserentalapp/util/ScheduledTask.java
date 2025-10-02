package com.rentalapp.houserentalapp.util;

import com.rentalapp.houserentalapp.model.ApplicationInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Slf4j
@Component
public class ScheduledTask {

    private final int fixedRate = 1000 * 60; // 1 minute

    private static final String SERVER_URL = "https://attendance-tracker-backend-ufbe.onrender.com/info/version";

    @Autowired
    private RestTemplate restTemplate;

    @Scheduled(fixedRate = fixedRate)
    public void executeTask() {
        log.info("Task executed at: " + System.currentTimeMillis());
        try {
            log.info("Executing server ping task");
            HttpEntity<HttpHeaders> httpEntity = new HttpEntity<>(new HttpHeaders());
            ParameterizedTypeReference<ResponseObject<ApplicationInfoDto>> responseType = new ParameterizedTypeReference<>() {};
            ResponseEntity<ResponseObject<ApplicationInfoDto>> response = restTemplate.exchange(SERVER_URL, HttpMethod.GET, httpEntity, responseType);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Server ping successful. Status: {}", response.getStatusCode());
                log.info("Server ping successful. Application/Version: {}/{}", Objects.requireNonNull(response.getBody()).getData().getDescription(), response.getBody().getData().getVersion());
            } else {
                log.warn("Server ping failed. Status: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error pinging server: {}", e.getMessage());
        }
    }

}
