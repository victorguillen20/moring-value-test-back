package com.morning.torneo.domain.port.in;

import com.morning.torneo.domain.model.LoginCommand;
import com.morning.torneo.domain.model.RegistroUsuarioCommand;
import com.morning.torneo.domain.model.Usuario;

public interface AuthUseCase {

    Usuario registrar(RegistroUsuarioCommand cmd);

    Usuario login(LoginCommand cmd);
}
