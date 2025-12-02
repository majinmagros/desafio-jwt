package com.desafio.jwt.integration;

import com.desafio.jwt.JwtApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = JwtApplication.class)
@AutoConfigureMockMvc
class JwtControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String TOKEN_CASO1 = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05sIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg";
    private final String TOKEN_CASO3 = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiRXh0ZXJuYWwiLCJTZWVkIjoiODgwMzciLCJOYW1lIjoiTTRyaWEgT2xpdmlhIn0.6YD73XWZYQSSMDf6H0i3-kylz1-TY_Yt6h1cV2Ku-Qs";

    @Test
    void testEndpointValido() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("token", TOKEN_CASO1);

        mockMvc.perform(post("/jwt/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.justificativa").value("Abrindo o JWT, as informações contidas atendem a descrição."))
                .andExpect(jsonPath("$.claims.Role").value("Admin"));
    }

    @Test
    void testEndpointInvalido() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("token", TOKEN_CASO3);

        mockMvc.perform(post("/jwt/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // O endpoint retorna 200 mesmo para token inválido
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.justificativa").value("Abrindo o JWT, a Claim Name possui caracter de números."));
    }

    @Test
    void testEndpointTokenVazio() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("token", "");

        mockMvc.perform(post("/jwt/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.justificativa").value("Token vazio ou nulo."));
    }
}