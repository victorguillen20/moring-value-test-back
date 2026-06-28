package com.morning.torneo.application.mapper;

import com.morning.torneo.application.dto.RegistroRequest;
import com.morning.torneo.domain.model.RegistroUsuarioCommand;

public final class RegistroUsuarioMapper {

    private RegistroUsuarioMapper() {
    }

    public static RegistroUsuarioCommand toCommand(RegistroRequest request) {
        return new RegistroUsuarioCommand(
            request.getUsername(),
            request.getEmail(),
            request.getPassword()
        );
    }
}
