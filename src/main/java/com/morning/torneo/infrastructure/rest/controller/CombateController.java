package com.morning.torneo.infrastructure.rest.controller;

import com.morning.torneo.domain.model.Combate;
import com.morning.torneo.domain.model.CombateRequest;
import com.morning.torneo.domain.port.in.CombateUseCase;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/combates")
public class CombateController {

    private final CombateUseCase useCase;

    public CombateController(CombateUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<Combate> iniciar(@RequestBody CombateRequest request) {
        Combate combate = useCase.iniciar(request);
        return ResponseEntity.ok(combate);
    }

    @PostMapping("/aleatorio")
    public ResponseEntity<Combate> iniciarAleatorio() {
        Combate combate = useCase.iniciarAleatorio();
        return ResponseEntity.ok(combate);
    }

    @GetMapping
    public ResponseEntity<List<Combate>> listar() {
        List<Combate> combates = useCase.listar();
        return ResponseEntity.ok(combates);
    }
}
