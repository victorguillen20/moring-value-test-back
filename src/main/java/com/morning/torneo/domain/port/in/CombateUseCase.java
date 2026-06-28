package com.morning.torneo.domain.port.in;

import com.morning.torneo.domain.model.Combate;
import com.morning.torneo.domain.model.IniciarCombateCommand;
import java.util.List;

public interface CombateUseCase {

    Combate iniciar(IniciarCombateCommand cmd);

    Combate iniciarAleatorio();

    List<Combate> listar();
}
