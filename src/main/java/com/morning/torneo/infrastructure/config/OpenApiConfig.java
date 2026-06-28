package com.morning.torneo.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI torneoGalacticoOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Torneo Galactico API")
                        .description("API REST del Torneo Galactico (Morning Value). " +
                                "Gestiona especies, combates y ranking con autenticacion JWT.")
                        .version("1.0.0")
                        .license(new License().name("MIT")));
    }
}
