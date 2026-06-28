package com.morning.torneo.infrastructure.rest.controller;

import com.morning.torneo.application.dto.EspecieRequest;
import com.morning.torneo.application.dto.EspecieResponse;
import com.morning.torneo.application.mapper.EspecieMapper;
import com.morning.torneo.domain.model.Especie;
import com.morning.torneo.domain.port.in.EspecieUseCase;
import com.morning.torneo.infrastructure.rest.auth.RequiresAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/especies")
@Tag(name = "Especies", description = "Gestion de especies del torneo")
public class EspecieController {

    private final EspecieUseCase useCase;

    public EspecieController(EspecieUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping
    @Operation(summary = "Listar todas las especies", description = "Devuelve la lista completa de especies registradas (publico)")
    public ResponseEntity<List<EspecieResponse>> listar() {
        List<Especie> especies = useCase.listar();
        return ResponseEntity.ok(EspecieMapper.toResponseList(especies));
    }

    @PostMapping
    @RequiresAuth
    @Operation(summary = "Registrar nueva especie", description = "Crea una nueva especie (requiere autenticacion)")
    public ResponseEntity<EspecieResponse> registrar(@RequestBody @Valid EspecieRequest request) {
        Especie especie = EspecieMapper.toDomain(request);
        Especie especieGuardada = useCase.registrar(especie);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(especieGuardada.getId())
                .toUri();
        return ResponseEntity.created(location).body(EspecieMapper.toResponse(especieGuardada));
    }
}
