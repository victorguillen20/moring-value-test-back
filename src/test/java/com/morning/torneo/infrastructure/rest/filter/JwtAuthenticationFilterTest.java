package com.morning.torneo.infrastructure.rest.filter;

import com.morning.torneo.application.util.AuthenticatedUserHolder;
import com.morning.torneo.infrastructure.util.JwtTokenProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private JwtAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        filter = new JwtAuthenticationFilter(jwtTokenProvider);
    }

    @AfterEach
    void tearDown() {
        AuthenticatedUserHolder.clear();
    }

    @Test
    @DisplayName("Token valido: setea authenticatedUsername en request y en AuthenticatedUserHolder")
    void tokenValido() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/especies");
        request.addHeader("Authorization", "Bearer valid-token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        when(jwtTokenProvider.isValid("valid-token")).thenReturn(true);
        when(jwtTokenProvider.getUsernameFromToken("valid-token")).thenReturn("victor");

        filter.doFilter(request, response, chain);

        assertThat(request.getAttribute("authenticatedUsername")).isEqualTo("victor");
        assertThat(AuthenticatedUserHolder.get()).isNull();
        verify(jwtTokenProvider).isValid("valid-token");
        verify(jwtTokenProvider).getUsernameFromToken("valid-token");
    }

    @Test
    @DisplayName("Sin Authorization header: no setea atributo, deja pasar")
    void sinAuthorizationHeader() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/especies");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertThat(request.getAttribute("authenticatedUsername")).isNull();
        verify(jwtTokenProvider, never()).isValid(org.mockito.ArgumentMatchers.anyString());
    }

    @Test
    @DisplayName("Token invalido: no setea atributo, deja pasar (AuthInterceptor rechaza despues)")
    void tokenInvalido() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/especies");
        request.addHeader("Authorization", "Bearer invalid-token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        when(jwtTokenProvider.isValid("invalid-token")).thenReturn(false);

        filter.doFilter(request, response, chain);

        assertThat(request.getAttribute("authenticatedUsername")).isNull();
        verify(jwtTokenProvider).isValid("invalid-token");
        verify(jwtTokenProvider, never()).getUsernameFromToken(org.mockito.ArgumentMatchers.anyString());
    }

    @Test
    @DisplayName("Authorization con prefijo incorrecto: no procesa el token, deja pasar")
    void authorizationSinPrefijoBearer() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/especies");
        request.addHeader("Authorization", "Basic dXNlcjpwYXNz");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertThat(request.getAttribute("authenticatedUsername")).isNull();
        verify(jwtTokenProvider, never()).isValid(org.mockito.ArgumentMatchers.anyString());
    }

    @Test
    @DisplayName("AuthenticatedUserHolder se limpia en el finally del filtro")
    void authenticatedUserHolderSeLimpia() throws Exception {
        AuthenticatedUserHolder.set("otroUsuario");

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/especies");
        request.addHeader("Authorization", "Bearer valid-token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        when(jwtTokenProvider.isValid("valid-token")).thenReturn(true);
        when(jwtTokenProvider.getUsernameFromToken("valid-token")).thenReturn("victor");

        filter.doFilter(request, response, chain);

        assertThat(AuthenticatedUserHolder.get()).isNull();
    }

    @Test
    @DisplayName("shouldNotFilter devuelve true para paths /api/auth/")
    void shouldNotFilterParaAuthPaths() {
        assertThat(filter.shouldNotFilter(
            new MockHttpServletRequest("POST", "/api/auth/login"))).isTrue();
        assertThat(filter.shouldNotFilter(
            new MockHttpServletRequest("POST", "/api/auth/register"))).isTrue();
    }

    @Test
    @DisplayName("shouldNotFilter devuelve false para paths no auth")
    void shouldNotFilterParaOtrosPaths() {
        assertThat(filter.shouldNotFilter(
            new MockHttpServletRequest("GET", "/api/especies"))).isFalse();
        assertThat(filter.shouldNotFilter(
            new MockHttpServletRequest("POST", "/api/combates"))).isFalse();
    }
}
