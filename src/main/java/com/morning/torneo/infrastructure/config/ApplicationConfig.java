package com.morning.torneo.infrastructure.config;

import com.morning.torneo.domain.port.out.CombateRepositoryPort;
import com.morning.torneo.domain.port.out.EspecieRepositoryPort;
import com.morning.torneo.domain.service.CombateService;
import com.morning.torneo.domain.service.EspecieService;
import com.morning.torneo.domain.service.RankingService;
import java.util.Random;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public EspecieService especieService(EspecieRepositoryPort repository) {
        return new EspecieService(repository);
    }

    @Bean
    public RankingService rankingService(EspecieRepositoryPort especieRepository,
                                         CombateRepositoryPort combateRepository) {
        return new RankingService(especieRepository, combateRepository);
    }

    @Bean
    public Random random() {
        return new Random();
    }

    @Bean
    public CombateService combateService(EspecieRepositoryPort especieRepository,
                                         CombateRepositoryPort combateRepository,
                                         RankingService rankingService,
                                         Random random) {
        return new CombateService(especieRepository, combateRepository, rankingService, random);
    }
}
