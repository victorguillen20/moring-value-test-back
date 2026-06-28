package com.morning.torneo.domain.service;

import com.morning.torneo.domain.exception.CombateInvalidoException;
import com.morning.torneo.domain.exception.DesempateNoPermitidoException;
import com.morning.torneo.domain.exception.EspecieNoEncontradaException;
import com.morning.torneo.domain.exception.EspecieVsSiMismaException;
import com.morning.torneo.domain.model.Combate;
import com.morning.torneo.domain.model.Especie;
import com.morning.torneo.domain.model.IniciarCombateCommand;
import com.morning.torneo.domain.port.in.RankingUseCase;
import com.morning.torneo.domain.port.out.CombateRepositoryPort;
import com.morning.torneo.domain.port.out.EspecieRepositoryPort;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CombateServiceTest {

    private static final long SEMILLA = 42L;

    @Mock
    private EspecieRepositoryPort especieRepository;

    @Mock
    private CombateRepositoryPort combateRepository;

    @Mock
    private RankingUseCase rankingUseCase;

    private Clock clock;
    private Random random;
    private CombateService service;

    private Especie especie1;
    private Especie especie2;

    @BeforeEach
    void setUp() {
        clock = Clock.fixed(Instant.parse("2026-06-27T10:00:00Z"), ZoneId.of("UTC"));
        random = new Random(SEMILLA);
        service = new CombateService(especieRepository, combateRepository, rankingUseCase, random, clock);
        especie1 = new Especie(1L, "Andromeda", 500, "Vuelo Rapido");
        especie1.setFechaCreacion(LocalDateTime.now(clock));
        especie2 = new Especie(2L, "Betelgeuse", 700, "Fuego");
        especie2.setFechaCreacion(LocalDateTime.now(clock));
    }

    private IniciarCombateCommand comandoBasico() {
        return new IniciarCombateCommand(1L, 2L, false);
    }

    @Test
    @DisplayName("Combate normal: especie con mayor nivel efectivo gana, modificadores random 1-50")
    void combateNormal() {
        when(especieRepository.findById(1L)).thenReturn(Optional.of(especie1));
        when(especieRepository.findById(2L)).thenReturn(Optional.of(especie2));
        when(combateRepository.save(any(Combate.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Combate combate = service.iniciar(comandoBasico());

        ArgumentCaptor<Combate> captor = ArgumentCaptor.forClass(Combate.class);
        verify(combateRepository).save(captor.capture());
        Combate guardado = captor.getValue();

        assertThat(guardado.getEspecie1()).isEqualTo(especie1);
        assertThat(guardado.getEspecie2()).isEqualTo(especie2);
        assertThat(guardado.getModificadorEspecie1()).isBetween(1, 50);
        assertThat(guardado.getModificadorEspecie2()).isBetween(1, 50);
        assertThat(guardado.getNivelEfectivoEspecie1()).isEqualTo(500 + guardado.getModificadorEspecie1());
        assertThat(guardado.getNivelEfectivoEspecie2()).isEqualTo(700 + guardado.getModificadorEspecie2());
        assertThat(guardado.getGanador()).isEqualTo(especie2);
        assertThat(guardado.getFecha()).isNotNull();
    }

    @Test
    @DisplayName("Empate de nivel efectivo: gana la especie con nombre alfabeticamente menor")
    void empateDeNivelEfectivo() {
        Especie alfa = new Especie(10L, "Alfa", 500, "X");
        alfa.setFechaCreacion(LocalDateTime.now(clock));
        Especie zeta = new Especie(20L, "Zeta", 500, "X");
        zeta.setFechaCreacion(LocalDateTime.now(clock));
        when(especieRepository.findById(10L)).thenReturn(Optional.of(alfa));
        when(especieRepository.findById(20L)).thenReturn(Optional.of(zeta));
        when(combateRepository.save(any(Combate.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Random randomMockeado = org.mockito.Mockito.mock(Random.class);
        org.mockito.Mockito.when(randomMockeado.nextInt(50)).thenReturn(25);
        CombateService serviceConRandomMockeado = new CombateService(
            especieRepository, combateRepository, rankingUseCase, randomMockeado, clock);
        IniciarCombateCommand cmd = new IniciarCombateCommand(10L, 20L, false);
        Combate combate = serviceConRandomMockeado.iniciar(cmd);

        assertThat(combate.getNivelEfectivoEspecie1()).isEqualTo(500 + 25);
        assertThat(combate.getNivelEfectivoEspecie2()).isEqualTo(500 + 25);
        assertThat(combate.getNivelEfectivoEspecie1()).isEqualTo(combate.getNivelEfectivoEspecie2());
        assertThat(combate.getGanador().getNombre()).isEqualTo("Alfa");
    }

    @Test
    @DisplayName("Lanza EspecieVsSiMismaException cuando especie1 == especie2 sin flag desempate")
    void mismaEspecieSinDesempate() {
        when(especieRepository.findById(1L)).thenReturn(Optional.of(especie1));
        when(especieRepository.findById(2L)).thenReturn(Optional.of(especie1));

        IniciarCombateCommand cmd = new IniciarCombateCommand(1L, 1L, false);

        assertThatThrownBy(() -> service.iniciar(cmd))
                .isInstanceOf(EspecieVsSiMismaException.class)
                .hasMessageContaining("son iguales");

        verify(combateRepository, never()).save(any(Combate.class));
    }

    @Test
    @DisplayName("Lanza DesempateNoPermitidoException cuando flag desempate=true pero especie NO esta en primer puesto empatado")
    void desempateNoPermitido() {
        when(especieRepository.findById(1L)).thenReturn(Optional.of(especie1));
        when(especieRepository.findById(2L)).thenReturn(Optional.of(especie1));
        when(rankingUseCase.esPrimerPuestoEmpatado(1L)).thenReturn(false);

        IniciarCombateCommand cmd = new IniciarCombateCommand(1L, 1L, true);

        assertThatThrownBy(() -> service.iniciar(cmd))
                .isInstanceOf(DesempateNoPermitidoException.class)
                .hasMessageContaining("no esta en primer puesto empatado");

        verify(combateRepository, never()).save(any(Combate.class));
    }

    @Test
    @DisplayName("Permite Especie vs si misma cuando flag desempate=true y especie SI esta en primer puesto empatado")
    void desempatePermitido() {
        when(especieRepository.findById(1L)).thenReturn(Optional.of(especie1));
        when(especieRepository.findById(2L)).thenReturn(Optional.of(especie1));
        when(rankingUseCase.esPrimerPuestoEmpatado(1L)).thenReturn(true);
        when(combateRepository.save(any(Combate.class))).thenAnswer(invocation -> invocation.getArgument(0));

        IniciarCombateCommand cmd = new IniciarCombateCommand(1L, 1L, true);

        Combate combate = service.iniciar(cmd);

        assertThat(combate.getEspecie1().getId()).isEqualTo(1L);
        assertThat(combate.getEspecie2().getId()).isEqualTo(1L);
        assertThat(combate.getGanador().getId()).isEqualTo(1L);
        verify(combateRepository).save(any(Combate.class));
    }

    @Test
    @DisplayName("Lanza EspecieNoEncontradaException cuando especie1 no existe")
    void especie1NoExiste() {
        when(especieRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.iniciar(comandoBasico()))
                .isInstanceOf(EspecieNoEncontradaException.class)
                .hasMessageContaining("id 1");

        verify(combateRepository, never()).save(any(Combate.class));
    }

    @Test
    @DisplayName("Lanza EspecieNoEncontradaException cuando especie2 no existe")
    void especie2NoExiste() {
        when(especieRepository.findById(1L)).thenReturn(Optional.of(especie1));
        when(especieRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.iniciar(comandoBasico()))
                .isInstanceOf(EspecieNoEncontradaException.class)
                .hasMessageContaining("id 2");

        verify(combateRepository, never()).save(any(Combate.class));
    }

    @Test
    @DisplayName("Lanza CombateInvalidoException cuando los IDs son nulos")
    void idsNulos() {
        IniciarCombateCommand cmd = new IniciarCombateCommand(null, 2L, false);

        assertThatThrownBy(() -> service.iniciar(cmd))
                .isInstanceOf(CombateInvalidoException.class)
                .hasMessageContaining("obligatorios");

        verify(combateRepository, never()).save(any(Combate.class));
    }

    @Test
    @DisplayName("Lanza CombateInvalidoException cuando hay menos de 2 especies para combate aleatorio")
    void aleatorioConMenosDe2Especies() {
        when(especieRepository.findAll()).thenReturn(List.of(especie1));

        assertThatThrownBy(() -> service.iniciarAleatorio())
                .isInstanceOf(CombateInvalidoException.class)
                .hasMessageContaining("minimo 2");

        verify(combateRepository, never()).save(any(Combate.class));
    }

    @Test
    @DisplayName("Combate aleatorio: elige 2 especies distintas al azar")
    void combateAleatorio() {
        Especie otraEspecie = new Especie(3L, "Cygnus", 600, "Luz");
        otraEspecie.setFechaCreacion(LocalDateTime.now(clock));
        when(especieRepository.findAll()).thenReturn(List.of(especie1, especie2, otraEspecie));
        when(especieRepository.findById(any())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            if (id.equals(1L)) return Optional.of(especie1);
            if (id.equals(2L)) return Optional.of(especie2);
            if (id.equals(3L)) return Optional.of(otraEspecie);
            return Optional.empty();
        });
        when(combateRepository.save(any(Combate.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Combate combate = service.iniciarAleatorio();

        assertThat(combate.getEspecie1().getId()).isNotEqualTo(combate.getEspecie2().getId());
        assertThat(combate.getEspecie1().getId()).isIn(1L, 2L, 3L);
        assertThat(combate.getEspecie2().getId()).isIn(1L, 2L, 3L);
        verify(combateRepository).save(any(Combate.class));
    }
}
