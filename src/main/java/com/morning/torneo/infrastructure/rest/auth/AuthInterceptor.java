package com.morning.torneo.infrastructure.rest.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.morning.torneo.application.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final String AUTHENTICATED_USERNAME_ATTRIBUTE = "authenticatedUsername";
    private static final String CORRELATION_ID_ATTRIBUTE = "correlationId";

    private final ObjectMapper objectMapper;

    public AuthInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        if (!handlerMethod.hasMethodAnnotation(RequiresAuth.class)) {
            return true;
        }

        Object authenticatedUsername = request.getAttribute(AUTHENTICATED_USERNAME_ATTRIBUTE);

        if (authenticatedUsername == null) {
            String correlationId = request.getAttribute(CORRELATION_ID_ATTRIBUTE) != null
                    ? request.getAttribute(CORRELATION_ID_ATTRIBUTE).toString()
                    : "N/A";

            ErrorResponse error = new ErrorResponse(
                    LocalDateTime.now(),
                    401,
                    "Unauthorized",
                    "UNAUTHORIZED",
                    "Autenticacion requerida",
                    correlationId,
                    request.getRequestURI()
            );

            response.setStatus(401);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(error));
            return false;
        }

        return true;
    }
}
