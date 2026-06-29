package com.morning.torneo.infrastructure.config;

import com.morning.torneo.domain.model.Combate;
import com.morning.torneo.domain.model.Especie;
import com.morning.torneo.domain.model.Usuario;
import com.morning.torneo.domain.port.in.AuthUseCase;
import com.morning.torneo.domain.port.out.CombateRepositoryPort;
import com.morning.torneo.domain.port.out.EspecieRepositoryPort;
import com.morning.torneo.domain.port.out.UsuarioRepositoryPort;
import com.morning.torneo.domain.model.RegistroUsuarioCommand;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UsuarioRepositoryPort usuarioRepository;
    private final EspecieRepositoryPort especieRepository;
    private final CombateRepositoryPort combateRepository;
    private final AuthUseCase authUseCase;

    public DataInitializer(
            UsuarioRepositoryPort usuarioRepository,
            EspecieRepositoryPort especieRepository,
            CombateRepositoryPort combateRepository,
            AuthUseCase authUseCase) {
        this.usuarioRepository = usuarioRepository;
        this.especieRepository = especieRepository;
        this.combateRepository = combateRepository;
        this.authUseCase = authUseCase;
    }

    @Override
    public void run(String... args) {
        try {
            seedUsuario();
            seedEspecies();
            seedCombates();
            logger.info("Datos de prueba inicializados correctamente");
        } catch (Exception e) {
            logger.warn("No se pudieron inicializar datos de prueba (probablemente ya existen): {}", e.getMessage());
        }
    }

    private void seedUsuario() {
        if (usuarioRepository.findByUsername("victor").isPresent()) {
            logger.info("Usuario 'victor' ya existe, no se crea");
            return;
        }
        Usuario usuario = authUseCase.registrar(new RegistroUsuarioCommand(
                "victor",
                "victor@galaxia.com",
                "password123"
        ));
        logger.info("Usuario '{}' creado con id={}", usuario.getUsername(), usuario.getId());
    }

    private void seedEspecies() {
        if (!especieRepository.findAll().isEmpty()) {
            logger.info("Ya hay especies registradas, no se crean mas");
            return;
        }
        Especie alfa = new Especie(null, "Alfa Centauri", 500, "Vuelo Rapido");
        alfa.setFechaCreacion(java.time.LocalDateTime.now());
        Especie betelgeuse = new Especie(null, "Betelgeuse", 700, "Fuego");
        betelgeuse.setFechaCreacion(java.time.LocalDateTime.now());
        Especie cygnus = new Especie(null, "Cygnus X-1", 850, "Luz");
        cygnus.setFechaCreacion(java.time.LocalDateTime.now());
        Especie draco = new Especie(null, "Draco", 300, "Camuflaje");
        draco.setFechaCreacion(java.time.LocalDateTime.now());
        especieRepository.save(alfa);
        especieRepository.save(betelgeuse);
        especieRepository.save(cygnus);
        especieRepository.save(draco);
        logger.info("4 especies creadas: {}, {}, {}, {}",
                alfa.getNombre(), betelgeuse.getNombre(), cygnus.getNombre(), draco.getNombre());
    }

    private void seedCombates() {
        if (!combateRepository.findAll().isEmpty()) {
            logger.info("Ya hay combates registrados, no se crean mas");
            return;
        }
        List<Especie> especies = especieRepository.findAll();
        if (especies.size() < 2) {
            logger.warn("No hay suficientes especies para crear combates");
            return;
        }
        Especie cygnus = especies.stream().filter(e -> e.getNombre().equals("Cygnus X-1")).findFirst().orElse(especies.get(0));
        Especie betelgeuse = especies.stream().filter(e -> e.getNombre().equals("Betelgeuse")).findFirst().orElse(especies.get(1));

        Combate c1 = new Combate(cygnus, betelgeuse, cygnus, 870, 715, 20, 15, java.time.LocalDateTime.now());
        Combate c2 = new Combate(betelgeuse, cygnus, cygnus, 720, 880, 20, 30, java.time.LocalDateTime.now().minusDays(1));
        combateRepository.save(c1);
        combateRepository.save(c2);
        logger.info("2 combates creados");
    }
}
