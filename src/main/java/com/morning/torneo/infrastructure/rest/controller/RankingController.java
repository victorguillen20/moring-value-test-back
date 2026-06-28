package com.morning.torneo.infrastructure.rest.controller;

import com.morning.torneo.application.dto.RankingResponse;
import com.morning.torneo.application.mapper.RankingMapper;
import com.morning.torneo.domain.model.EspecieRanking;
import com.morning.torneo.domain.port.in.RankingUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ranking")
@Tag(name = "Ranking", description = "Ranking de especies por victorias")
public class RankingController {

    private final RankingUseCase useCase;

    public RankingController(RankingUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping
    @Operation(summary = "Obtener ranking", description = "Devuelve el ranking de especies ordenado por victorias (publico)")
    public ResponseEntity<List<RankingResponse>> obtener() {
        List<EspecieRanking> ranking = useCase.obtenerRanking();
        return ResponseEntity.ok(RankingMapper.toResponseList(ranking));
    }
}
