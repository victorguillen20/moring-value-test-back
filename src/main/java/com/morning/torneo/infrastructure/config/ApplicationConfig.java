package com.morning.torneo.infrastructure.config;

import com.morning.torneo.domain.port.in.RankingUseCase;
import com.morning.torneo.domain.port.out.CombateRepositoryPort;
import com.morning.torneo.domain.port.out.EspecieRepositoryPort;
import com.morning.torneo.domain.port.out.UsuarioRepositoryPort;
import com.morning.torneo.domain.service.AuthService;
import com.morning.torneo.domain.service.CombateService;
import com.morning.torneo.domain.service.EspecieService;
import com.morning.torneo.domain.service.RankingService;
import com.morning.torneo.infrastructure.util.JwtTokenProvider;
import java.time.Clock;
import java.util.Random;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfig {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider(@Value("${jwt.secret}") String secret,
                                              @Value("${jwt.expiration-ms}") long expirationMs) {
        return new JwtTokenProvider(secret, expirationMs);
    }

    @Bean
    public EspecieService especieService(EspecieRepositoryPort repository, Clock clock) {
        return new EspecieService(repository, clock);
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
                                         RankingUseCase rankingUseCase,
                                         Random random,
                                         Clock clock) {
        return new CombateService(especieRepository, combateRepository, rankingUseCase, random, clock);
    }

    @Bean
    public AuthService authService(UsuarioRepositoryPort usuarioRepository,
                                  PasswordEncoder passwordEncoder,
                                  Clock clock) {
        return new AuthService(usuarioRepository, passwordEncoder, clock);
    }
}
