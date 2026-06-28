package com.morning.torneo.domain.service;

import com.morning.torneo.domain.exception.EspecieInvalidaException;
import com.morning.torneo.domain.exception.EspecieYaExisteException;
import com.morning.torneo.domain.model.Especie;
import com.morning.torneo.domain.port.out.EspecieRepositoryPort;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EspecieServiceTest {

    @Mock
    private EspecieRepositoryPort repository;

    private Clock clock;
    private EspecieService service;

    @BeforeEach
    void setUp() {
        clock = Clock.fixed(
            java.time.Instant.parse("2026-06-27T10:00:00Z"),
            ZoneId.of("UTC")
        );
        service = new EspecieService(repository, clock);
    }

    @Test
    @DisplayName("Registra una especie valida y devuelve la especie guardada")
    void registrarEspecieValida() {
        Especie input = new Especie(null, "Andromeda", 500, "Vuelo Rapido");
        Especie guardada = new Especie(1L, "Andromeda", 500, "Vuelo Rapido");
        guardada.setFechaCreacion(LocalDateTime.now(clock));
        when(repository.existsByNombre("Andromeda")).thenReturn(false);
        when(repository.save(any(Especie.class))).thenReturn(guardada);

        Especie resultado = service.registrar(input);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Andromeda");
        assertThat(resultado.getNivelPoder()).isEqualTo(500);
        assertThat(resultado.getHabilidadEspecial()).isEqualTo("Vuelo Rapido");
        assertThat(resultado.getFechaCreacion()).isNotNull();
        verify(repository).existsByNombre("Andromeda");
        verify(repository).save(any(Especie.class));
    }

    @Test
    @DisplayName("Lanza EspecieYaExisteException cuando el nombre ya esta registrado")
    void registrarEspecieConNombreDuplicado() {
        Especie input = new Especie(null, "Andromeda", 500, "Vuelo Rapido");
        when(repository.existsByNombre("Andromeda")).thenReturn(true);

        assertThatThrownBy(() -> service.registrar(input))
                .isInstanceOf(EspecieYaExisteException.class)
                .hasMessage("El nombre de la especie ya esta registrado");

        verify(repository, never()).save(any(Especie.class));
    }

    @Test
    @DisplayName("Lanza EspecieInvalidaException cuando el nombre es vacio")
    void registrarEspecieConNombreVacio() {
        Especie input = new Especie(null, "", 500, "Vuelo Rapido");

        assertThatThrownBy(() -> service.registrar(input))
                .isInstanceOf(EspecieInvalidaException.class)
                .hasMessage("El nombre de la especie es obligatorio");

        verify(repository, never()).save(any(Especie.class));
    }

    @Test
    @DisplayName("Lanza EspecieInvalidaException cuando el nombre es null")
    void registrarEspecieConNombreNull() {
        Especie input = new Especie(null, null, 500, "Vuelo Rapido");

        assertThatThrownBy(() -> service.registrar(input))
                .isInstanceOf(EspecieInvalidaException.class)
                .hasMessage("El nombre de la especie es obligatorio");

        verify(repository, never()).save(any(Especie.class));
    }

    @Test
    @DisplayName("Lanza EspecieInvalidaException cuando el nombre tiene mas de 50 caracteres")
    void registrarEspecieConNombreMuyLargo() {
        String nombreLargo = "a".repeat(51);
        Especie input = new Especie(null, nombreLargo, 500, "Vuelo Rapido");

        assertThatThrownBy(() -> service.registrar(input))
                .isInstanceOf(EspecieInvalidaException.class)
                .hasMessage("El nombre no puede tener mas de 50 caracteres");

        verify(repository, never()).save(any(Especie.class));
    }

    @Test
    @DisplayName("Lanza EspecieInvalidaException cuando el nivel de poder es menor a 1")
    void registrarEspecieConNivelPoderMenorAUno() {
        Especie input = new Especie(null, "Andromeda", 0, "Vuelo Rapido");

        assertThatThrownBy(() -> service.registrar(input))
                .isInstanceOf(EspecieInvalidaException.class)
                .hasMessage("El nivel de poder debe estar entre 1 y 1000");

        verify(repository, never()).save(any(Especie.class));
    }

    @Test
    @DisplayName("Lanza EspecieInvalidaException cuando el nivel de poder es mayor a 1000")
    void registrarEspecieConNivelPoderMayorA1000() {
        Especie input = new Especie(null, "Andromeda", 1001, "Vuelo Rapido");

        assertThatThrownBy(() -> service.registrar(input))
                .isInstanceOf(EspecieInvalidaException.class)
                .hasMessage("El nivel de poder debe estar entre 1 y 1000");

        verify(repository, never()).save(any(Especie.class));
    }

    @Test
    @DisplayName("Lanza EspecieInvalidaException cuando la habilidad es vacia")
    void registrarEspecieConHabilidadVacia() {
        Especie input = new Especie(null, "Andromeda", 500, "");

        assertThatThrownBy(() -> service.registrar(input))
                .isInstanceOf(EspecieInvalidaException.class)
                .hasMessage("La habilidad especial no puede estar vacia");

        verify(repository, never()).save(any(Especie.class));
    }

    @Test
    @DisplayName("Lanza EspecieInvalidaException cuando la habilidad tiene mas de 100 caracteres")
    void registrarEspecieConHabilidadMuyLarga() {
        String habilidadLarga = "a".repeat(101);
        Especie input = new Especie(null, "Andromeda", 500, habilidadLarga);

        assertThatThrownBy(() -> service.registrar(input))
                .isInstanceOf(EspecieInvalidaException.class)
                .hasMessage("La habilidad especial no puede tener mas de 100 caracteres");

        verify(repository, never()).save(any(Especie.class));
    }

    @Test
    @DisplayName("Trimea espacios en el nombre y la habilidad antes de guardar")
    void trimeaEspaciosEnNombreYHabilidad() {
        Especie input = new Especie(null, "  Andromeda  ", 500, "  Vuelo Rapido  ");
        Especie guardada = new Especie(1L, "Andromeda", 500, "Vuelo Rapido");
        guardada.setFechaCreacion(LocalDateTime.now(clock));
        when(repository.existsByNombre("Andromeda")).thenReturn(false);
        when(repository.save(any(Especie.class))).thenReturn(guardada);

        Especie resultado = service.registrar(input);

        assertThat(resultado.getNombre()).isEqualTo("Andromeda");
        assertThat(resultado.getHabilidadEspecial()).isEqualTo("Vuelo Rapido");
    }

    @Test
    @DisplayName("Listar devuelve todas las especies del repositorio")
    void listarDevuelveTodasLasEspecies() {
        Especie e1 = new Especie(1L, "Andromeda", 500, "Vuelo");
        e1.setFechaCreacion(LocalDateTime.now(clock));
        Especie e2 = new Especie(2L, "Betelgeuse", 700, "Fuego");
        e2.setFechaCreacion(LocalDateTime.now(clock));
        when(repository.findAll()).thenReturn(List.of(e1, e2));

        List<Especie> resultado = service.listar();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Andromeda");
        assertThat(resultado.get(1).getNombre()).isEqualTo("Betelgeuse");
    }
}
