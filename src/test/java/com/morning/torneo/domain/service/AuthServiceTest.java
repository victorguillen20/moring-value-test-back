package com.morning.torneo.domain.service;

import com.morning.torneo.domain.exception.CredencialesInvalidasException;
import com.morning.torneo.domain.exception.UsuarioYaExisteException;
import com.morning.torneo.domain.model.LoginCommand;
import com.morning.torneo.domain.model.RegistroUsuarioCommand;
import com.morning.torneo.domain.model.Usuario;
import com.morning.torneo.domain.port.out.UsuarioRepositoryPort;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Clock clock;
    private AuthService service;

    @BeforeEach
    void setUp() {
        clock = Clock.fixed(Instant.parse("2026-06-27T10:00:00Z"), ZoneId.of("UTC"));
        service = new AuthService(usuarioRepository, passwordEncoder, clock);
    }

    @Test
    @DisplayName("Registro exitoso: hashea password, setea fechaRegistro, devuelve usuario")
    void registroExitoso() {
        RegistroUsuarioCommand cmd = new RegistroUsuarioCommand("victor", "victor@example.com", "password123");
        when(usuarioRepository.existsByUsername("victor")).thenReturn(false);
        when(usuarioRepository.existsByEmail("victor@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("$2a$10$hashedpassword");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario u = invocation.getArgument(0);
            u.setId(1L);
            return u;
        });

        Usuario resultado = service.registrar(cmd);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getUsername()).isEqualTo("victor");
        assertThat(resultado.getEmail()).isEqualTo("victor@example.com");
        assertThat(resultado.getPasswordHash()).isEqualTo("$2a$10$hashedpassword");
        assertThat(resultado.getFechaRegistro()).isNotNull();
        verify(passwordEncoder).encode("password123");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Registro: lanza UsuarioYaExisteException cuando username ya esta registrado")
    void registroUsernameDuplicado() {
        RegistroUsuarioCommand cmd = new RegistroUsuarioCommand("victor", "victor@example.com", "password123");
        when(usuarioRepository.existsByUsername("victor")).thenReturn(true);

        assertThatThrownBy(() -> service.registrar(cmd))
                .isInstanceOf(UsuarioYaExisteException.class)
                .hasMessage("El nombre de usuario ya esta registrado");

        verify(usuarioRepository, never()).save(any(Usuario.class));
        verify(passwordEncoder, never()).encode(any(String.class));
    }

    @Test
    @DisplayName("Registro: lanza UsuarioYaExisteException cuando email ya esta registrado")
    void registroEmailDuplicado() {
        RegistroUsuarioCommand cmd = new RegistroUsuarioCommand("victor", "victor@example.com", "password123");
        when(usuarioRepository.existsByUsername("victor")).thenReturn(false);
        when(usuarioRepository.existsByEmail("victor@example.com")).thenReturn(true);

        assertThatThrownBy(() -> service.registrar(cmd))
                .isInstanceOf(UsuarioYaExisteException.class)
                .hasMessage("El email ya esta registrado");

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Registro: lanza CredencialesInvalidasException con username muy corto")
    void registroUsernameCorto() {
        RegistroUsuarioCommand cmd = new RegistroUsuarioCommand("vi", "victor@example.com", "password123");

        assertThatThrownBy(() -> service.registrar(cmd))
                .isInstanceOf(CredencialesInvalidasException.class)
                .hasMessageContaining("3 y 30 caracteres");

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Registro: lanza CredencialesInvalidasException con password muy corto")
    void registroPasswordCorto() {
        RegistroUsuarioCommand cmd = new RegistroUsuarioCommand("victor", "victor@example.com", "short");

        assertThatThrownBy(() -> service.registrar(cmd))
                .isInstanceOf(CredencialesInvalidasException.class)
                .hasMessageContaining("al menos 8 caracteres");

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Registro: lanza CredencialesInvalidasException con email invalido (sin @)")
    void registroEmailInvalido() {
        RegistroUsuarioCommand cmd = new RegistroUsuarioCommand("victor", "emailsinformatocom", "password123");

        assertThatThrownBy(() -> service.registrar(cmd))
                .isInstanceOf(CredencialesInvalidasException.class)
                .hasMessageContaining("formato valido");

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Login exitoso: valida password con BCrypt y devuelve usuario")
    void loginExitoso() {
        Usuario usuario = new Usuario("victor", "victor@example.com", "$2a$10$hashedpassword", null);
        usuario.setId(1L);
        usuario.setFechaRegistro(LocalDateTime.now(clock));
        LoginCommand cmd = new LoginCommand("victor", "password123");
        when(usuarioRepository.findByUsername("victor")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("password123", "$2a$10$hashedpassword")).thenReturn(true);

        Usuario resultado = service.login(cmd);

        assertThat(resultado).isEqualTo(usuario);
        verify(passwordEncoder).matches("password123", "$2a$10$hashedpassword");
    }

    @Test
    @DisplayName("Login: lanza CredencialesInvalidasException cuando username no existe")
    void loginUsernameInexistente() {
        LoginCommand cmd = new LoginCommand("noexiste", "password123");
        when(usuarioRepository.findByUsername("noexiste")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.login(cmd))
                .isInstanceOf(CredencialesInvalidasException.class)
                .hasMessage("Credenciales invalidas");

        verify(passwordEncoder, never()).matches(any(String.class), any(String.class));
    }

    @Test
    @DisplayName("Login: lanza CredencialesInvalidasException cuando password es incorrecto")
    void loginPasswordIncorrecto() {
        Usuario usuario = new Usuario("victor", "victor@example.com", "$2a$10$hashedpassword", null);
        usuario.setId(1L);
        LoginCommand cmd = new LoginCommand("victor", "wrongpassword");
        when(usuarioRepository.findByUsername("victor")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("wrongpassword", "$2a$10$hashedpassword")).thenReturn(false);

        assertThatThrownBy(() -> service.login(cmd))
                .isInstanceOf(CredencialesInvalidasException.class)
                .hasMessage("Credenciales invalidas");
    }

    @Test
    @DisplayName("Login: lanza CredencialesInvalidasException con credenciales nulas")
    void loginCredencialesNulas() {
        LoginCommand cmd = new LoginCommand(null, null);

        assertThatThrownBy(() -> service.login(cmd))
                .isInstanceOf(CredencialesInvalidasException.class)
                .hasMessageContaining("obligatorias");
    }
}
