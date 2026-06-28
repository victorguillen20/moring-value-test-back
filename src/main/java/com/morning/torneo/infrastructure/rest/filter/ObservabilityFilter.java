package com.morning.torneo.infrastructure.rest.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.UUID;

@Component
public class ObservabilityFilter extends OncePerRequestFilter {

    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
    private static final String CORRELATION_ID_ATTRIBUTE = "correlationId";
    private static final String CORRELATION_ID_MDC_KEY = "correlationId";

    private final Logger logger = LoggerFactory.getLogger(ObservabilityFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        long startTime = System.currentTimeMillis();

        String correlationId = request.getHeader(CORRELATION_ID_HEADER);
        if (correlationId == null || correlationId.isEmpty()) {
            correlationId = UUID.randomUUID().toString();
        }

        request.setAttribute(CORRELATION_ID_ATTRIBUTE, correlationId);
        response.setHeader(CORRELATION_ID_HEADER, correlationId);
        MDC.put(CORRELATION_ID_MDC_KEY, correlationId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            long timestamp = System.currentTimeMillis();
            String method = request.getMethod();
            String path = request.getRequestURI();
            int status = response.getStatus();
            String ip = request.getRemoteAddr();
            String username = (String) request.getAttribute("authenticatedUsername");

            String logJson = String.format(
                "{\"timestamp\":%d,\"correlationId\":\"%s\",\"method\":\"%s\",\"path\":\"%s\",\"status\":%d,\"durationMs\":%d,\"ip\":\"%s\",\"username\":%s}",
                timestamp, correlationId, method, path, status, duration, ip,
                username == null ? "null" : "\"" + username + "\""
            );
            logger.info(logJson);

            MDC.remove(CORRELATION_ID_MDC_KEY);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/actuator/health")
            || uri.startsWith("/actuator/info")
            || uri.startsWith("/v3/api-docs")
            || uri.startsWith("/swagger-ui.html")
            || uri.startsWith("/swagger-ui/")
            || uri.startsWith("/h2-console");
    }

}
