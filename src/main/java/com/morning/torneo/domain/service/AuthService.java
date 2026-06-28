package com.morning.torneo.domain.service;

import com.morning.torneo.domain.exception.CredencialesInvalidasException;
import com.morning.torneo.domain.exception.UsuarioYaExisteException;
import com.morning.torneo.domain.model.LoginCommand;
import com.morning.torneo.domain.model.RegistroUsuarioCommand;
import com.morning.torneo.domain.model.Usuario;
import com.morning.torneo.domain.port.in.AuthUseCase;
import com.morning.torneo.domain.port.out.UsuarioRepositoryPort;
import java.time.Clock;
import java.time.LocalDateTime;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class AuthService implements AuthUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;

    public AuthService(UsuarioRepositoryPort usuarioRepository, PasswordEncoder passwordEncoder, Clock clock) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.clock = clock;
    }

    @Override
    public Usuario registrar(RegistroUsuarioCommand cmd) {
        String username = cmd.getUsername();
        String email = cmd.getEmail();
        String password = cmd.getPassword();

        if (username == null || username.trim().isEmpty()) {
            throw new CredencialesInvalidasException("El nombre de usuario es obligatorio");
        }
        if (username.length() < 3 || username.length() > 30) {
            throw new CredencialesInvalidasException("El nombre de usuario debe tener entre 3 y 30 caracteres");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new CredencialesInvalidasException("El email es obligatorio");
        }
        if (!email.contains("@")) {
            throw new CredencialesInvalidasException("El email no tiene formato valido");
        }
        if (password == null || password.isEmpty()) {
            throw new CredencialesInvalidasException("La contrasena es obligatoria");
        }
        if (password.length() < 8) {
            throw new CredencialesInvalidasException("La contrasena debe tener al menos 8 caracteres");
        }
        if (usuarioRepository.existsByUsername(username.trim())) {
            throw new UsuarioYaExisteException("El nombre de usuario ya esta registrado");
        }
        if (usuarioRepository.existsByEmail(email.trim())) {
            throw new UsuarioYaExisteException("El email ya esta registrado");
        }

        Usuario usuario = new Usuario(
                username.trim(),
                email.trim(),
                passwordEncoder.encode(password),
                null
        );
        usuario.setFechaRegistro(LocalDateTime.now(clock));

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario login(LoginCommand cmd) {
        if (cmd.getUsername() == null || cmd.getPassword() == null) {
            throw new CredencialesInvalidasException("Las credenciales son obligatorias");
        }

        Usuario usuario = usuarioRepository.findByUsername(cmd.getUsername())
                .orElseThrow(() -> new CredencialesInvalidasException("Credenciales invalidas"));

        if (!passwordEncoder.matches(cmd.getPassword(), usuario.getPasswordHash())) {
            throw new CredencialesInvalidasException("Credenciales invalidas");
        }

        return usuario;
    }
}
