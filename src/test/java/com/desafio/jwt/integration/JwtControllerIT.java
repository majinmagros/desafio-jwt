package com.desafio.jwt.integration;

import com.desafio.jwt.JwtApplication;
import com.desafio.jwt.constants.ConstantsTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
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

    @Test
    @DisplayName("200 OK e válido quando o token possui claims esperados")
    void shouldReturnValidResponseWhenTokenIsValid() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("token", ConstantsTest.TOKEN_CASO1);

        mockMvc.perform(post("/jwt/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.justificativa").value("Abrindo o JWT, as informações contidas atendem a descrição."))
                .andExpect(jsonPath("$.claims.Role").value("Admin"));
    }

    @Test
    @DisplayName("200 OK e inválido quando o claim Name possui caracteres numéricos (caso 3)")
    void shouldReturnInvalidWhenNameContainsDigitsCase3() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("token",  ConstantsTest.TOKEN_CASO3);

        mockMvc.perform(post("/jwt/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // O endpoint retorna 200 mesmo para token inválido
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.justificativa").value("Abrindo o JWT, a Claim Name possui caracter de números."));

    }

    @Test
    @DisplayName("200 OK e inválido quando o JWT é malformado")
    void shouldReturnInvalidWhenJwtIsMalformed() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("token", ConstantsTest.TOKEN_CASO2);

        mockMvc.perform(post("/jwt/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // O endpoint retorna 200 mesmo JWT invalido
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.justificativa").value("JWT invalido."));
    }

    @Test
    @DisplayName("200 OK e inválido quando o token está vazio")
    void shouldReturnInvalidWhenTokenIsEmpty() throws Exception {
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
    @DisplayName("200 OK e inválido quando existem mais de 3 claims")
    void shouldReturnInvalidWhenMoreThanThreeClaims() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("token", ConstantsTest.TOKEN_CASO4);

        mockMvc.perform(post("/jwt/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // O endpoint retorna 200 mesmo JWT invalido
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.justificativa").value("Abrindo o JWT, foi encontrado mais de 3 claims."));

    }
    @Test
    @DisplayName("200 OK e inválido quando o número de claims é diferente de 3")
    void shouldReturnInvalidWhenClaimsCountIsDifferentFromThree() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("token", ConstantsTest.TOKEN_DIF_3);

        mockMvc.perform(post("/jwt/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // O endpoint retorna 200 mesmo Abrindo o JWT, o número de claims é diferente de 3
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.justificativa").value("Alguma claim obrigatória está ausente: Name, Role ou Seed."));
    }
    @Test
    @DisplayName("200 OK e inválido quando falta claim obrigatória (Name, Role ou Seed)")
    void shouldReturnInvalidWhenRequiredClaimMissing() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("token", ConstantsTest.TOKEN_MISSING_CLAIMS);

        mockMvc.perform(post("/jwt/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // O endpoint retorna 200 mesmo Alguma claim obrigatória está ausente: Name, Role ou Seed
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.justificativa").value("Alguma claim obrigatória está ausente: Name, Role ou Seed."));

    }
    @Test
    @DisplayName("200 OK e inválido quando o claim Name excede 256 caracteres")
    void shouldReturnInvalidWhenNameExceedsMaxLength() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("token", ConstantsTest.TOKEN_NAME_TOO_LONG);

        mockMvc.perform(post("/jwt/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // O endpoint retorna 200 mesmo Claim Name excede 256 caracteres.
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.justificativa").value("Claim Name excede 256 caracteres."));

    }
    @Test
    @DisplayName("200 OK e inválido quando o claim Name contém dígitos")
    void shouldReturnInvalidWhenNameContainsDigits() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("token", ConstantsTest.TOKEN_NAME_HAS_DIGITS);

        mockMvc.perform(post("/jwt/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // O endpoint retorna 200 mesmo Abrindo o JWT, a Claim Name possui caracter de números.
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.justificativa").value("Abrindo o JWT, a Claim Name possui caracter de números."));

    }
    @Test
    @DisplayName("200 OK e inválido quando o claim Role é inválido")
    void shouldReturnInvalidWhenRoleIsInvalid() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("token", ConstantsTest.TOKEN_INVALID_ROLE);

        mockMvc.perform(post("/jwt/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // O endpoint retorna 200 mesmo Claim Role inválida. Permitidos: Admin, Member, External.
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.justificativa").value("Claim Role inválida. Permitidos: Admin, Member, External."));

    }

    @Test
    @DisplayName("200 OK e inválido quando Seed não é um inteiro válido")
    void shouldReturnInvalidWhenSeedIsNotInteger() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("token", ConstantsTest.TOKEN_NOT_INTEGER);

        mockMvc.perform(post("/jwt/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // O endpoint retorna 200 mesmo Seed não é um número inteiro válido.
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.justificativa").value("Seed não é um número inteiro válido."));

    }

    @Test
    @DisplayName("200 OK e inválido quando Seed não é um número primo")
    void shouldReturnInvalidWhenSeedIsNotPrime() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("token", ConstantsTest.TOKEN_NOT_PRIME);

        mockMvc.perform(post("/jwt/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // O endpoint retorna 200 mesmo Seed não é um número inteiro válido.
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.justificativa").value("Seed não é um número primo."));

    }
}