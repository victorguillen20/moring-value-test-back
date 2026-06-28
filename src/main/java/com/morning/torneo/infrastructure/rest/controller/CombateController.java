package com.morning.torneo.infrastructure.rest.controller;

import com.morning.torneo.application.dto.CombateRequest;
import com.morning.torneo.application.dto.CombateResponse;
import com.morning.torneo.application.mapper.CombateMapper;
import com.morning.torneo.domain.model.Combate;
import com.morning.torneo.domain.model.IniciarCombateCommand;
import com.morning.torneo.domain.port.in.CombateUseCase;
import jakarta.validation.Valid;
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
    public ResponseEntity<CombateResponse> iniciar(@RequestBody @Valid CombateRequest request) {
        IniciarCombateCommand cmd = CombateMapper.toCommand(request);
        Combate combate = useCase.iniciar(cmd);
        return ResponseEntity.ok(CombateMapper.toResponse(combate));
    }

    @PostMapping("/aleatorio")
    public ResponseEntity<CombateResponse> iniciarAleatorio() {
        Combate combate = useCase.iniciarAleatorio();
        return ResponseEntity.ok(CombateMapper.toResponse(combate));
    }

    @GetMapping
    public ResponseEntity<List<CombateResponse>> listar() {
        List<Combate> combates = useCase.listar();
        return ResponseEntity.ok(CombateMapper.toResponseList(combates));
    }
}
