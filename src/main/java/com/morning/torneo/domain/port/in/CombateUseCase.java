package com.morning.torneo.domain.port.in;

import com.morning.torneo.application.dto.CombateRequest;
import com.morning.torneo.domain.model.Combate;
import java.util.List;

public interface CombateUseCase {

    Combate iniciar(CombateRequest request);

    Combate iniciarAleatorio();

    List<Combate> listar();
}
