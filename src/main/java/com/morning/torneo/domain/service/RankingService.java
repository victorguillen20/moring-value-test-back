package com.morning.torneo.domain.service;

import com.morning.torneo.domain.model.Combate;
import com.morning.torneo.domain.model.Especie;
import com.morning.torneo.domain.model.EspecieRanking;
import com.morning.torneo.domain.port.in.RankingUseCase;
import com.morning.torneo.domain.port.out.CombateRepositoryPort;
import com.morning.torneo.domain.port.out.EspecieRepositoryPort;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RankingService implements RankingUseCase {

    private final EspecieRepositoryPort especieRepository;
    private final CombateRepositoryPort combateRepository;

    public RankingService(EspecieRepositoryPort especieRepository,
                          CombateRepositoryPort combateRepository) {
        this.especieRepository = especieRepository;
        this.combateRepository = combateRepository;
    }

    @Override
    public List<EspecieRanking> obtenerRanking() {
        List<Especie> especies = especieRepository.findAll();
        List<Combate> combates = combateRepository.findAll();

        Map<Long, Long> victoriasPorEspecie = combates.stream()
            .filter(c -> c.getGanador() != null)
            .collect(Collectors.groupingBy(
                c -> c.getGanador().getId(),
                Collectors.counting()
            ));

        List<EspecieRanking> ranking = new ArrayList<>();
        for (Especie especie : especies) {
            long victorias = victoriasPorEspecie.getOrDefault(especie.getId(), 0L);
            ranking.add(new EspecieRanking(especie, (int) victorias));
        }

        ranking.sort((r1, r2) -> {
            if (r1.getVictorias() != r2.getVictorias()) {
                return Integer.compare(r2.getVictorias(), r1.getVictorias());
            }
            return r1.getEspecie().getNombre()
                    .compareToIgnoreCase(r2.getEspecie().getNombre());
        });

        List<EspecieRanking> rankingConPosicion = new ArrayList<>();
        for (int i = 0; i < ranking.size(); i++) {
            EspecieRanking entry = ranking.get(i);
            if (i == 0) {
                entry.setPosicion(1);
            } else {
                EspecieRanking anterior = ranking.get(i - 1);
                if (entry.getVictorias() == anterior.getVictorias()) {
                    entry.setPosicion(anterior.getPosicion());
                } else {
                    entry.setPosicion(anterior.getPosicion() + 1);
                }
            }
            rankingConPosicion.add(entry);
        }
        return rankingConPosicion;
    }

    @Override
    public boolean esPrimerPuestoEmpatado(Long especieId) {
        List<EspecieRanking> ranking = this.obtenerRanking();
        if (ranking.isEmpty()) {
            return false;
        }

        int maxVictorias = ranking.get(0).getVictorias();
        if (maxVictorias == 0) {
            return false;
        }

        List<EspecieRanking> primeros = ranking.stream()
                .filter(r -> r.getVictorias() == maxVictorias)
                .toList();

        return primeros.size() > 1
                && primeros.stream().anyMatch(r -> r.getEspecie().getId().equals(especieId));
    }
}
