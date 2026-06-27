package com.morning.torneo.infrastructure.rest.controller;

import com.morning.torneo.domain.model.Especie;
import com.morning.torneo.domain.port.in.EspecieUseCase;
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
public class EspecieController {

    private final EspecieUseCase useCase;

    public EspecieController(EspecieUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping
    public ResponseEntity<List<Especie>> listar() {
        List<Especie> especies = useCase.listar();
        return ResponseEntity.ok(especies);
    }

    @PostMapping
    public ResponseEntity<Especie> registrar(@RequestBody Especie especie) {
        Especie especieGuardada = useCase.registrar(especie);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(especieGuardada.getId())
                .toUri();
        return ResponseEntity.created(location).body(especieGuardada);
    }
}
