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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = JwtApplication.class)
@AutoConfigureMockMvc
class JwtControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String TOKEN_CASO1 = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05sIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg";
    private final String TOKEN_CASO2 = "eyJhbGciOiJzI1NiJ9.dfsdfsfryJSr2xrIjoiQWRtaW4iLCJTZrkIjoiNzg0MSIsIk5hbrUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05fsdfsIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg";
    private final String TOKEN_CASO3 = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiRXh0ZXJuYWwiLCJTZWVkIjoiODgwMzciLCJOYW1lIjoiTTRyaWEgT2xpdmlhIn0.6YD73XWZYQSSMDf6H0i3-kylz1-TY_Yt6h1cV2Ku-Qs";
    private final String TOKEN_CASO4 = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiTWVtYmVyIiwiT3JnIjoiQlIiLCJTZWVkIjoiMTQ2MjciLCJOYW1lIjoiVmFsZGlyIEFyYW5oYSJ9.cmrXV_Flm5mfdpfNUVopY_I2zeJUy4EZ4i3Fea98zvY";
   // Tokens adicionais
    private final String TOKEN_DIF_3 =  "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiRXh0ZXJuYWwiLCJOYW1lIjoiSm9yZWwifQ.XXX";
    private final String TOKEN_MISSING_CLAIMS = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjpudWxsLCJOYW1lIjoiSm9hbyBkYSBTaWx2YSJ9.pY3w2qLmX8rB6zV9nT0yS1R4U5I7O2P9Q1W4E6A8C3B";
    private final String TOKEN_NAME_TOO_LONG = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJSb2xlIjoiRXh0ZXJuYWwiLCJTZWVkIjoiNTg5MSIsIk5hbWUiOiJNYXJpYSBPbGl2aWEgZGEgU2lsdmEgU2FudG9zIE1hY2hhZG8gZSBPbGl2ZWlyYSBkYSBDb3N0YSBQb250ZXMgUGVyZWlyYSBkZSBBbG1laWRhIEN1bmhhIFJvZHJpZ3VlcyBkZSBTb3VzYSBGb250ZXMgTmFzY2ltZW50byBkYSBTaWx2YSBlIFNpbHZhIGRlIE9saXZlaXJhIGRlIFNhbnRhIENydXogZGUgU2FudG8gQW50w7RuaW8gZGUgUOFkdWEgZGUgQXNzaXMgZGUgU2lxdWVpcmEgZGUgTW91cmEgZGUgQWxjw6JudGFyYSBkZSBCYXJyb3MgZGUgTGltYSBkZSBBcmHDuqpvIGRlIENhcnZhbGhvIGRlIEZlcnJlaXJhIGRlIFJpYmFzIGRlIENhc3RybyBkZSBNb250ZWlybyBkZSBGZXJuYW5kZXMgZGUgR29tZXMgZGUgTWFydGlucyBkZSBSYW1vcyBkZSBUZWl4ZWlyYSBkZSBDb3JyZWlhIGRlIE1lbG8gZGUgTG9wZXMgZGUgQ2FydmFsaG8gZGUgTW91cmEgZGEgU2lsdmEgU2FudG9zIE1hY2hhZG8gZSBPbGl2ZWlyYSBkYSBDb3N0YSBQb250ZXMgUGVyZWlyYSBkZSBBbG1laWRhIEN1bmhhIFJvZHJpZ3VlcyBkZSBTb3VzYSBGb250ZXMgTmFzY2ltZW50byBkYSBTaWx2YSBlIFNpbHZhIGRlIE9saXZlaXJhIGRlIFNhbnRhIENydXogZGUgU2FudG8gQW50w7RuaW8gZGUgUOFkdWEgZGUgQXNzaXMgZGUgU2lxdWVpcmEgZGUgTW91cmEgZGUgQWxjw6JudGFyYSBkZSBCYXJyb3MgZGUgTGltYSBkZSBBcmHDuqpvIGRlIENhcnZhbGhvIGRlIEZlcnJlaXJhIGRlIFJpYmFzIGRlIENhc3RybyBkZSBNb250ZWlybyBkZSBGZXJuYW5kZXMgZGUgR29tZXMgZGUgTWFydGlucyBkZSBSYW1vcyBkZSBUZWl4ZWlyYSBkZSBDb3JyZWlhIGRlIE1lbG8gZGUgTG9wZXMgZGUgQ2FydmFsaG8gZGUgTW91cmEifQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
    private final String TOKEN_NAME_HAS_DIGITS = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiRXh0ZXJuYWwiLCJTZWVkIjoiODgwMzciLCJOYW1lIjoiTTRyaWEgT2xpdmlhIn0.6YD73XWZYQSSMDf6H0i3-kylz1-TY_Yt6h1cV2Ku-Qs";
    private final String TOKEN_INVALID_ROLE = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiVXNlciIsIlNlZWQiOiIxNDYyNyIsIk5hbWUiOiJJcm1hbyBkbyBKb3JlbCJ9.ZHVtbXk";
    private final String TOKEN_NOT_INTEGER = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiMTQ2MjcuOCIsIk5hbWUiOiJTdGV2ZSBNYWdhbCJ9.ZHVtbXk";
    private final String TOKEN_NOT_PRIME = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJSb2xlIjoiRXh0ZXJuYWwiLCJTZWVkIjoiNzg0MCIsIk5hbWUiOiJKb3JlbCJ9.KmM7nP2qR5sV8wB1yC4zF6gH9jL3oI0xT2uW5aY7d";
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
    void testEndpointJWTInvalido() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("token", TOKEN_CASO2);

        mockMvc.perform(post("/jwt/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // O endpoint retorna 200 mesmo JWT invalido
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.justificativa").value("JWT invalido."));
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

    @Test
    void testEndpointMore3Claims() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("token", TOKEN_CASO4);

        mockMvc.perform(post("/jwt/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // O endpoint retorna 200 mesmo JWT invalido
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.justificativa").value("Abrindo o JWT, foi encontrado mais de 3 claims."));

    }
    @Test
    void testEndpointDif3() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("token", TOKEN_DIF_3);

        mockMvc.perform(post("/jwt/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // O endpoint retorna 200 mesmo Abrindo o JWT, o número de claims é diferente de 3
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.justificativa").value("Alguma claim obrigatória está ausente: Name, Role ou Seed."));
    }
    @Test
    void testEndpointMissing_Claims() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("token", TOKEN_MISSING_CLAIMS);

        mockMvc.perform(post("/jwt/validate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk()) // O endpoint retorna 200 mesmo Alguma claim obrigatória está ausente: Name, Role ou Seed
                    .andExpect(jsonPath("$.valid").value(false))
                    .andExpect(jsonPath("$.justificativa").value("Alguma claim obrigatória está ausente: Name, Role ou Seed."));

    }
    @Test
    void testEndpointToo_Long() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("token", TOKEN_NAME_TOO_LONG);

        mockMvc.perform(post("/jwt/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // O endpoint retorna 200 mesmo Claim Name excede 256 caracteres.
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.justificativa").value("Claim Name excede 256 caracteres."));

    }
    @Test
    void testName_Has_Digit() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("token", TOKEN_NAME_HAS_DIGITS);

        mockMvc.perform(post("/jwt/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // O endpoint retorna 200 mesmo Abrindo o JWT, a Claim Name possui caracter de números.
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.justificativa").value("Abrindo o JWT, a Claim Name possui caracter de números."));

    }
    @Test
    void testInvalid_Role() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("token", TOKEN_INVALID_ROLE);

        mockMvc.perform(post("/jwt/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // O endpoint retorna 200 mesmo Claim Role inválida. Permitidos: Admin, Member, External.
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.justificativa").value("Claim Role inválida. Permitidos: Admin, Member, External."));

    }

    @Test
    void testInvalidNotInteger() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("token", TOKEN_NOT_INTEGER);

        mockMvc.perform(post("/jwt/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // O endpoint retorna 200 mesmo Seed não é um número inteiro válido.
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.justificativa").value("Seed não é um número inteiro válido."));

    }

    @Test
    void testInvalidNotPrime() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("token", TOKEN_NOT_PRIME);

        mockMvc.perform(post("/jwt/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // O endpoint retorna 200 mesmo Seed não é um número inteiro válido.
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.justificativa").value("Seed não é um número primo."));

    }
}