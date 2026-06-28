package com.morning.torneo.infrastructure.rest.filter;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class ObservabilityFilterTest {

    private ObservabilityFilter filter;

    @BeforeEach
    void setUp() {
        filter = new ObservabilityFilter();
        MDC.clear();
    }

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    @DisplayName("Lee X-Correlation-Id del header si esta presente")
    void leeCorrelationIdDelHeader() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/especies");
        request.addHeader("X-Correlation-Id", "test-correlation-id-12345");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertThat(request.getAttribute("correlationId")).isEqualTo("test-correlation-id-12345");
        assertThat(response.getHeader("X-Correlation-Id")).isEqualTo("test-correlation-id-12345");
    }

    @Test
    @DisplayName("Genera UUID si no hay X-Correlation-Id en el header")
    void generaCorrelationIdSiNoHayHeader() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/especies");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        String correlationId = (String) request.getAttribute("correlationId");
        assertThat(correlationId).isNotNull();
        assertThat(correlationId).matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
        assertThat(response.getHeader("X-Correlation-Id")).isEqualTo(correlationId);
    }

    @Test
    @DisplayName("Setea correlationId en MDC durante el request y limpia en finally")
    void seteaYlimpiaMDC() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/especies");
        request.addHeader("X-Correlation-Id", "test-mdc-id");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertThat(MDC.get("correlationId")).isNull();
    }

    @Test
    @DisplayName("Continua con el filter chain despues de procesar")
    void continuaConFilterChain() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/especies");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertThat(chain.getRequest()).isEqualTo(request);
    }

    @Test
    @DisplayName("shouldNotFilter devuelve true para /actuator/")
    void shouldNotFilterParaActuator() {
        assertThat(filter.shouldNotFilter(
            new MockHttpServletRequest("GET", "/actuator/health"))).isTrue();
        assertThat(filter.shouldNotFilter(
            new MockHttpServletRequest("GET", "/actuator/info"))).isTrue();
    }

    @Test
    @DisplayName("shouldNotFilter devuelve true para /v3/api-docs y /swagger-ui/")
    void shouldNotFilterParaSwaggerYOpenApi() {
        assertThat(filter.shouldNotFilter(
            new MockHttpServletRequest("GET", "/v3/api-docs"))).isTrue();
        assertThat(filter.shouldNotFilter(
            new MockHttpServletRequest("GET", "/swagger-ui.html"))).isTrue();
        assertThat(filter.shouldNotFilter(
            new MockHttpServletRequest("GET", "/swagger-ui/index.html"))).isTrue();
    }

    @Test
    @DisplayName("shouldNotFilter devuelve true para /h2-console")
    void shouldNotFilterParaH2Console() {
        assertThat(filter.shouldNotFilter(
            new MockHttpServletRequest("GET", "/h2-console"))).isTrue();
    }

    @Test
    @DisplayName("shouldNotFilter devuelve false para paths normales")
    void shouldNotFilterParaPathsNormales() {
        assertThat(filter.shouldNotFilter(
            new MockHttpServletRequest("GET", "/api/especies"))).isFalse();
        assertThat(filter.shouldNotFilter(
            new MockHttpServletRequest("POST", "/api/combates"))).isFalse();
    }

    @Test
    @DisplayName("Continua con el filter chain aunque la cadena lance excepcion")
    void continuaFilterChainAunqueExcepcion() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/especies");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = (req, res) -> {
            throw new RuntimeException("test exception");
        };

        try {
            filter.doFilter(request, response, chain);
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("test exception");
        }

        assertThat(MDC.get("correlationId")).isNull();
    }
}
