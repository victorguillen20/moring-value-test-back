package com.morning.torneo.domain.service;

import com.morning.torneo.application.dto.CombateRequest;
import com.morning.torneo.domain.exception.CombateInvalidoException;
import com.morning.torneo.domain.exception.DesempateNoPermitidoException;
import com.morning.torneo.domain.exception.EspecieNoEncontradaException;
import com.morning.torneo.domain.exception.EspecieVsSiMismaException;
import com.morning.torneo.domain.model.Combate;
import com.morning.torneo.domain.model.Especie;
import com.morning.torneo.domain.port.in.CombateUseCase;
import com.morning.torneo.domain.port.in.RankingUseCase;
import com.morning.torneo.domain.port.out.CombateRepositoryPort;
import com.morning.torneo.domain.port.out.EspecieRepositoryPort;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

public class CombateService implements CombateUseCase {

    private final EspecieRepositoryPort especieRepository;
    private final CombateRepositoryPort combateRepository;
    private final RankingUseCase rankingUseCase;
    private final Random random;

    public CombateService(EspecieRepositoryPort especieRepository,
                           CombateRepositoryPort combateRepository,
                           RankingUseCase rankingUseCase,
                           Random random) {
        this.especieRepository = especieRepository;
        this.combateRepository = combateRepository;
        this.rankingUseCase = rankingUseCase;
        this.random = random;
    }

    @Override
    public Combate iniciar(CombateRequest request) {
        if (request.getEspecie1Id() == null || request.getEspecie2Id() == null) {
            throw new CombateInvalidoException("Los IDs de las especies son obligatorios");
        }

        Especie especie1 = especieRepository.findById(request.getEspecie1Id())
                .orElseThrow(() -> new EspecieNoEncontradaException(
                        "La especie con id " + request.getEspecie1Id() + " no existe"));

        Especie especie2 = especieRepository.findById(request.getEspecie2Id())
                .orElseThrow(() -> new EspecieNoEncontradaException(
                        "La especie con id " + request.getEspecie2Id() + " no existe"));

        if (especie1.getId().equals(especie2.getId())) {
            if (request.isEsDesempate()) {
                if (!rankingUseCase.esPrimerPuestoEmpatado(especie1.getId())) {
                    throw new DesempateNoPermitidoException(
                            "La especie con id " + especie1.getId()
                            + " no esta en primer puesto empatado; no se permite combate de desempate");
                }
            } else {
                throw new EspecieVsSiMismaException(
                        "Las especies con IDs " + especie1.getId() + " y " + especie2.getId()
                        + " son iguales; combate no permitido fuera de desempate");
            }
        }

        int mod1 = random.nextInt(50) + 1;
        int mod2 = random.nextInt(50) + 1;

        int nivelEf1 = especie1.getNivelPoder() + mod1;
        int nivelEf2 = especie2.getNivelPoder() + mod2;

        Especie ganador;
        if (nivelEf1 > nivelEf2) {
            ganador = especie1;
        } else if (nivelEf2 > nivelEf1) {
            ganador = especie2;
        } else {
            if (especie1.getNombre().compareToIgnoreCase(especie2.getNombre()) < 0) {
                ganador = especie1;
            } else {
                ganador = especie2;
            }
        }

        Combate combate = new Combate(especie1, especie2, ganador,
                nivelEf1, nivelEf2, mod1, mod2, LocalDateTime.now());

        return combateRepository.save(combate);
    }

    @Override
    public Combate iniciarAleatorio() {
        List<Especie> especies = especieRepository.findAll();
        if (especies.size() < 2) {
            throw new CombateInvalidoException(
                    "No hay suficientes especies registradas para un combate aleatorio (minimo 2)");
        }

        int idx1 = random.nextInt(especies.size());
        int idx2;
        do {
            idx2 = random.nextInt(especies.size());
        } while (idx1 == idx2);

        Especie especie1 = especies.get(idx1);
        Especie especie2 = especies.get(idx2);

        CombateRequest request = new CombateRequest(especie1.getId(), especie2.getId(), false);
        return this.iniciar(request);
    }

    @Override
    public List<Combate> listar() {
        return combateRepository.findAll();
    }
}
