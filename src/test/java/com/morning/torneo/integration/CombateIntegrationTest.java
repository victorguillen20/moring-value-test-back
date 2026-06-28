package com.morning.torneo.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.morning.torneo.application.dto.CombateRequest;
import com.morning.torneo.application.dto.EspecieRequest;
import com.morning.torneo.infrastructure.persistence.repository.CombateJpaRepository;
import com.morning.torneo.infrastructure.persistence.repository.EspecieJpaRepository;
import com.morning.torneo.infrastructure.persistence.repository.UsuarioJpaRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CombateIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EspecieJpaRepository especieJpaRepository;

    @Autowired
    private CombateJpaRepository combateJpaRepository;

    @Autowired
    private UsuarioJpaRepository usuarioJpaRepository;

    @BeforeEach
    void limpiarBaseDeDatos() {
        combateJpaRepository.deleteAll();
        especieJpaRepository.deleteAll();
        usuarioJpaRepository.deleteAll();
    }

    private long registrarEspecie(String nombre, int nivelPoder, String habilidadEspecial) throws Exception {
        EspecieRequest request = new EspecieRequest(nombre, nivelPoder, habilidadEspecial);
        MvcResult result = mockMvc.perform(post("/api/especies")
                .requestAttr("authenticatedUsername", "testUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        return objectMapper.readTree(responseBody).get("id").asLong();
    }

    @Test
    @DisplayName("End-to-end: registrar 2 especies, iniciar combate, verificar ganador en la BD")
    void flujoCompletoCombate() throws Exception {
        long id1 = registrarEspecie("Andromeda", 500, "Vuelo");
        long id2 = registrarEspecie("Betelgeuse", 700, "Fuego");

        CombateRequest request = new CombateRequest(id1, id2, false);
        mockMvc.perform(post("/api/combates")
                .requestAttr("authenticatedUsername", "testUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.especie1.nombre").value("Andromeda"))
                .andExpect(jsonPath("$.especie2.nombre").value("Betelgeuse"))
                .andExpect(jsonPath("$.ganador.nombre").exists())
                .andExpect(jsonPath("$.modificadorEspecie1").exists())
                .andExpect(jsonPath("$.modificadorEspecie2").exists());

        assertThat(combateJpaRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("End-to-end: registrar 2 especies, hacer 2 combates, ranking refleja victorias")
    void flujoCompletoConRanking() throws Exception {
        long id1 = registrarEspecie("Andromeda", 500, "Vuelo");
        long id2 = registrarEspecie("Betelgeuse", 700, "Fuego");

        CombateRequest combate1 = new CombateRequest(id1, id2, false);
        mockMvc.perform(post("/api/combates")
                .requestAttr("authenticatedUsername", "testUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(combate1)))
                .andExpect(status().isOk());

        CombateRequest combate2 = new CombateRequest(id2, id1, false);
        mockMvc.perform(post("/api/combates")
                .requestAttr("authenticatedUsername", "testUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(combate2)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/ranking"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].especie.nombre").value("Betelgeuse"))
                .andExpect(jsonPath("$[0].victorias").value(2))
                .andExpect(jsonPath("$[1].especie.nombre").value("Andromeda"))
                .andExpect(jsonPath("$[1].victorias").value(0));
    }

    @Test
    @DisplayName("End-to-end: combate aleatorio funciona con 2+ especies")
    void combateAleatorioEndToEnd() throws Exception {
        long id1 = registrarEspecie("Cygnus", 600, "Luz");
        long id2 = registrarEspecie("Draco", 800, "Fuego");

        mockMvc.perform(post("/api/combates/aleatorio")
                .requestAttr("authenticatedUsername", "testUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.especie1").exists())
                .andExpect(jsonPath("$.especie2").exists())
                .andExpect(jsonPath("$.ganador").exists());

        assertThat(combateJpaRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("End-to-end: combate aleatorio falla con menos de 2 especies (400)")
    void combateAleatorioConMenosDe2Especies() throws Exception {
        registrarEspecie("SoloUna", 500, "X");

        mockMvc.perform(post("/api/combates/aleatorio")
                .requestAttr("authenticatedUsername", "testUser"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("End-to-end: combate vs especie inexistente devuelve 404")
    void combateEspecieInexistente() throws Exception {
        long id1 = registrarEspecie("Andromeda", 500, "Vuelo");

        CombateRequest request = new CombateRequest(id1, 999L, false);
        mockMvc.perform(post("/api/combates")
                .requestAttr("authenticatedUsername", "testUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("End-to-end: especie con datos invalidos devuelve 400 con mensajes en espanol")
    void especieConDatosInvalidos() throws Exception {
        EspecieRequest request = new EspecieRequest("", 0, "");

        mockMvc.perform(post("/api/especies")
                .requestAttr("authenticatedUsername", "testUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(Matchers.containsString("obligatorio")));
    }
}
