package com.morning.torneo.domain.port.out;

import com.morning.torneo.domain.model.Combate;
import java.util.List;

public interface CombateRepositoryPort {

    Combate save(Combate combate);

    List<Combate> findAll();
}
