package com.morning.torneo.infrastructure.rest.controller;

import com.morning.torneo.application.dto.AuthResponse;
import com.morning.torneo.application.dto.LoginRequest;
import com.morning.torneo.application.dto.RegistroRequest;
import com.morning.torneo.application.mapper.LoginMapper;
import com.morning.torneo.application.mapper.RegistroUsuarioMapper;
import com.morning.torneo.domain.model.LoginCommand;
import com.morning.torneo.domain.model.RegistroUsuarioCommand;
import com.morning.torneo.domain.model.Usuario;
import com.morning.torneo.domain.port.in.AuthUseCase;
import com.morning.torneo.infrastructure.util.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Endpoints de autenticacion: registro y login")
public class AuthController {

    private final AuthUseCase authUseCase;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(AuthUseCase authUseCase, JwtTokenProvider jwtTokenProvider) {
        this.authUseCase = authUseCase;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar nuevo usuario", description = "Crea una cuenta de usuario y devuelve un token JWT")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegistroRequest request) {
        RegistroUsuarioCommand cmd = RegistroUsuarioMapper.toCommand(request);
        Usuario usuario = authUseCase.registrar(cmd);
        String token = jwtTokenProvider.generateToken(usuario.getUsername());
        AuthResponse response = new AuthResponse(token, "Bearer", usuario.getUsername(), jwtTokenProvider.getExpirationMs());
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesion", description = "Autentica un usuario y devuelve un token JWT")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginCommand cmd = LoginMapper.toCommand(request);
        Usuario usuario = authUseCase.login(cmd);
        String token = jwtTokenProvider.generateToken(usuario.getUsername());
        AuthResponse response = new AuthResponse(token, "Bearer", usuario.getUsername(), jwtTokenProvider.getExpirationMs());
        return ResponseEntity.ok(response);
    }
}
