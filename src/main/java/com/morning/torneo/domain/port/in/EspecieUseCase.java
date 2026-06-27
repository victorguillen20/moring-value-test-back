package com.morning.torneo.domain.port.in;

import com.morning.torneo.domain.model.Especie;
import java.util.List;

public interface EspecieUseCase {

    Especie registrar(Especie especie);

    List<Especie> listar();
}
