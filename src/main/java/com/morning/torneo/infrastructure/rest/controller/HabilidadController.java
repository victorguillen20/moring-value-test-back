package com.morning.torneo.infrastructure.rest.controller;

import com.morning.torneo.application.dto.HabilidadResponse;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/habilidades")
public class HabilidadController {

    private static final List<HabilidadResponse> HABILIDADES = List.of(
        new HabilidadResponse("Vuelo Rapido", 20),
        new HabilidadResponse("Magnetismo", -10),
        new HabilidadResponse("Fuego", 15),
        new HabilidadResponse("Hielo", 5),
        new HabilidadResponse("Veneno", -5),
        new HabilidadResponse("Regeneracion", 25),
        new HabilidadResponse("Fuerza Bruta", 30),
        new HabilidadResponse("Camuflaje", -15)
    );

    @GetMapping
    public List<HabilidadResponse> listar() {
        return HABILIDADES;
    }
}
