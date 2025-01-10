package com.rentalapp.houserentalapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myCustomConfig() {
        return new OpenAPI().info(
                new Info().title("Houser Rental App").version("1.0")
                        .description("Houser Rental App"))
                .servers(Arrays.asList(new Server().url("http://localhost:8080/").description("Local")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
//                .components(new Components().addSecuritySchemes(
//                        "bearerAuth", new SecurityScheme()
//                                .type(SecurityScheme.Type.HTTP)
//                                .scheme("bearer")
//                                .bearerFormat("JWT")
//                                .in(SecurityScheme.In.HEADER)
//                                .name("Authorization")
//                ))
                ;
    }

}
