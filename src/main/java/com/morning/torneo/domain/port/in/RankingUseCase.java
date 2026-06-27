package com.morning.torneo.domain.port.in;

import com.morning.torneo.domain.model.EspecieRanking;
import java.util.List;

public interface RankingUseCase {

    List<EspecieRanking> obtenerRanking();

    boolean esPrimerPuestoEmpatado(Long especieId);
}
