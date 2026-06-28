package com.morning.torneo.infrastructure.rest.filter;

import com.morning.torneo.application.util.AuthenticatedUserHolder;
import com.morning.torneo.infrastructure.util.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(2)
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHENTICATED_USERNAME_ATTRIBUTE = "authenticatedUsername";

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String authHeader = request.getHeader(AUTH_HEADER);

            if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
                String token = authHeader.substring(BEARER_PREFIX.length());

                if (!token.isEmpty() && jwtTokenProvider.isValid(token)) {
                    String username = jwtTokenProvider.getUsernameFromToken(token);
                    request.setAttribute(AUTHENTICATED_USERNAME_ATTRIBUTE, username);
                    AuthenticatedUserHolder.set(username);
                }
            }

            filterChain.doFilter(request, response);
        } finally {
            AuthenticatedUserHolder.clear();
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return false;
    }
}
