package com.morning.torneo.infrastructure.config;

import com.morning.torneo.domain.port.out.EspecieRepositoryPort;
import com.morning.torneo.domain.service.EspecieService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public EspecieService especieService(EspecieRepositoryPort repository) {
        return new EspecieService(repository);
    }
}
