package com.afgicafe.flight.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openApi () {
        return new OpenAPI()
                .info(
                        new Info().title("Blink Flight API")
                                .description("Blink Flight Booking Application API")
                                .version("1.0")
                )
                .tags(
                        List.of(
                                new Tag().name("Authentication").description("Login & Registration Routes"),
                                new Tag().name("Verification").description("User account verification")
//                                new Tag().name("Users").description("User management APIs"),
//                                new Tag().name("Roles").description("Role management APIs"),
//                                new Tag().name("Flights").description("Flight operations APIs")
                        )
                )
                .servers(
                        List.of(
                                new Server().url("https://flight-app-api.onrender.com/").description("PRODUCTION ENVIRONMENT"),
                                new Server().url("http://localhost:8080").description("LOCAL ENVIRONMENT")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth")
                );
    }
}
