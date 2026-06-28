package com.morning.torneo.application.mapper;

import com.morning.torneo.application.dto.LoginRequest;
import com.morning.torneo.domain.model.LoginCommand;

public final class LoginMapper {

    private LoginMapper() {
    }

    public static LoginCommand toCommand(LoginRequest request) {
        return new LoginCommand(
            request.getUsername(),
            request.getPassword()
        );
    }
}
