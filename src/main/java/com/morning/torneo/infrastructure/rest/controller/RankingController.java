package com.morning.torneo.infrastructure.rest.controller;

import com.morning.torneo.domain.model.EspecieRanking;
import com.morning.torneo.domain.port.in.RankingUseCase;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ranking")
public class RankingController {

    private final RankingUseCase useCase;

    public RankingController(RankingUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping
    public ResponseEntity<List<EspecieRanking>> obtener() {
        List<EspecieRanking> ranking = useCase.obtenerRanking();
        return ResponseEntity.ok(ranking);
    }
}
