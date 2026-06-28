package com.morning.torneo.domain.service;

import com.morning.torneo.domain.model.Combate;
import com.morning.torneo.domain.model.Especie;
import com.morning.torneo.domain.model.EspecieRanking;
import com.morning.torneo.domain.port.out.CombateRepositoryPort;
import com.morning.torneo.domain.port.out.EspecieRepositoryPort;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RankingServiceTest {

    @Mock
    private EspecieRepositoryPort especieRepository;

    @Mock
    private CombateRepositoryPort combateRepository;

    private RankingService service;

    private Especie alfa;
    private Especie beta;
    private Especie gamma;
    private Especie delta;

    @BeforeEach
    void setUp() {
        service = new RankingService(especieRepository, combateRepository);
        LocalDateTime now = LocalDateTime.now();
        alfa = new Especie(1L, "Alfa", 500, "X");
        alfa.setFechaCreacion(now);
        beta = new Especie(2L, "Beta", 600, "X");
        beta.setFechaCreacion(now);
        gamma = new Especie(3L, "Gamma", 700, "X");
        gamma.setFechaCreacion(now);
        delta = new Especie(4L, "Delta", 800, "X");
        delta.setFechaCreacion(now);
    }

    private Combate combate(Especie ganador, Especie perdedor) {
        return new Combate(ganador, perdedor, ganador, 0, 0, 0, 0, LocalDateTime.now());
    }

    @Test
    @DisplayName("Ranking vacio: no hay especies")
    void rankingVacio() {
        when(especieRepository.findAll()).thenReturn(List.of());

        List<EspecieRanking> ranking = service.obtenerRanking();

        assertThat(ranking).isEmpty();
    }

    @Test
    @DisplayName("Ordena por victorias descendente")
    void ordenPorVictoriasDescendente() {
        when(especieRepository.findAll()).thenReturn(List.of(alfa, beta, gamma, delta));
        when(combateRepository.findAll()).thenReturn(List.of(
            combate(beta, alfa),
            combate(beta, alfa),
            combate(gamma, alfa),
            combate(gamma, beta),
            combate(delta, alfa)
        ));

        List<EspecieRanking> ranking = service.obtenerRanking();

        assertThat(ranking).hasSize(4);
        assertThat(ranking.get(0).getEspecie().getNombre()).isEqualTo("Beta");
        assertThat(ranking.get(0).getVictorias()).isEqualTo(2);
        assertThat(ranking.get(1).getEspecie().getNombre()).isEqualTo("Gamma");
        assertThat(ranking.get(1).getVictorias()).isEqualTo(2);
        assertThat(ranking.get(2).getEspecie().getNombre()).isEqualTo("Delta");
        assertThat(ranking.get(2).getVictorias()).isEqualTo(1);
        assertThat(ranking.get(3).getEspecie().getNombre()).isEqualTo("Alfa");
        assertThat(ranking.get(3).getVictorias()).isEqualTo(0);
    }

    @Test
    @DisplayName("Empate de victorias: desempata por nombre alfabeticamente ascendente")
    void empateDeVictorias() {
        when(especieRepository.findAll()).thenReturn(List.of(gamma, alfa, delta, beta));
        when(combateRepository.findAll()).thenReturn(List.of(
            combate(alfa, beta),
            combate(gamma, delta),
            combate(beta, alfa)
        ));

        List<EspecieRanking> ranking = service.obtenerRanking();

        assertThat(ranking).hasSize(4);
        assertThat(ranking.get(0).getEspecie().getNombre()).isEqualTo("Alfa");
        assertThat(ranking.get(0).getVictorias()).isEqualTo(1);
        assertThat(ranking.get(1).getEspecie().getNombre()).isEqualTo("Beta");
        assertThat(ranking.get(1).getVictorias()).isEqualTo(1);
        assertThat(ranking.get(2).getEspecie().getNombre()).isEqualTo("Gamma");
        assertThat(ranking.get(2).getVictorias()).isEqualTo(1);
        assertThat(ranking.get(3).getEspecie().getNombre()).isEqualTo("Delta");
        assertThat(ranking.get(3).getVictorias()).isEqualTo(0);
    }

    @Test
    @DisplayName("Especies con 0 victorias: aparecen al final del ranking")
    void especiesCon0Victorias() {
        when(especieRepository.findAll()).thenReturn(List.of(alfa, beta, gamma));
        when(combateRepository.findAll()).thenReturn(List.of(
            combate(alfa, beta),
            combate(beta, gamma)
        ));

        List<EspecieRanking> ranking = service.obtenerRanking();

        assertThat(ranking).hasSize(3);
        assertThat(ranking.get(0).getEspecie().getNombre()).isEqualTo("Alfa");
        assertThat(ranking.get(0).getVictorias()).isEqualTo(1);
        assertThat(ranking.get(1).getEspecie().getNombre()).isEqualTo("Beta");
        assertThat(ranking.get(1).getVictorias()).isEqualTo(1);
        assertThat(ranking.get(2).getEspecie().getNombre()).isEqualTo("Gamma");
        assertThat(ranking.get(2).getVictorias()).isEqualTo(0);
    }

    @Test
    @DisplayName("esPrimerPuestoEmpatado devuelve true cuando especie esta en empate en primer puesto con otra")
    void esPrimerPuestoEmpatadoTrue() {
        when(especieRepository.findAll()).thenReturn(List.of(alfa, beta, gamma));
        when(combateRepository.findAll()).thenReturn(List.of(
            combate(alfa, beta),
            combate(gamma, beta)
        ));

        boolean resultado = service.esPrimerPuestoEmpatado(1L);

        assertThat(resultado).isTrue();
    }

    @Test
    @DisplayName("esPrimerPuestoEmpatado devuelve false cuando especie no esta en primer puesto")
    void esPrimerPuestoEmpatadoFalseNoEstaEnPrimero() {
        when(especieRepository.findAll()).thenReturn(List.of(alfa, beta, gamma));
        when(combateRepository.findAll()).thenReturn(List.of(
            combate(alfa, beta),
            combate(alfa, gamma)
        ));

        boolean resultado = service.esPrimerPuestoEmpatado(2L);

        assertThat(resultado).isFalse();
    }

    @Test
    @DisplayName("esPrimerPuestoEmpatado devuelve false cuando todas las especies tienen 0 victorias")
    void esPrimerPuestoEmpatadoFalseCon0Victorias() {
        when(especieRepository.findAll()).thenReturn(List.of(alfa, beta, gamma));
        when(combateRepository.findAll()).thenReturn(List.of());

        boolean resultado = service.esPrimerPuestoEmpatado(1L);

        assertThat(resultado).isFalse();
    }

    @Test
    @DisplayName("esPrimerPuestoEmpatado devuelve false cuando especie es la unica en primer puesto")
    void esPrimerPuestoEmpatadoFalseUnicaEnPrimero() {
        when(especieRepository.findAll()).thenReturn(List.of(alfa, beta, gamma));
        when(combateRepository.findAll()).thenReturn(List.of(
            combate(alfa, beta),
            combate(alfa, gamma),
            combate(beta, gamma)
        ));

        boolean resultado = service.esPrimerPuestoEmpatado(1L);

        assertThat(resultado).isFalse();
    }
}
