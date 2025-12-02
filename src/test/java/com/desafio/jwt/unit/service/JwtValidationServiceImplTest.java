package com.desafio.jwt.unit.service;

import com.desafio.jwt.dto.JwtResponseDTO;
import com.desafio.jwt.service.ImplService.JwtValidationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.MessageSource;



import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class JwtValidationServiceImplTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private JwtValidationServiceImpl jwtValidationService;

    // Tokens dos 4 casos do desafio
    private final String TOKEN_CASO1 = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05sIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg";
    private final String TOKEN_CASO2 = "eyJhbGciOiJzI1NiJ9.dfsdfsfryJSr2xrIjoiQWRtaW4iLCJTZrkIjoiNzg0MSIsIk5hbrUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05fsdfsIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg";
    private final String TOKEN_CASO3 = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiRXh0ZXJuYWwiLCJTZWVkIjoiODgwMzciLCJOYW1lIjoiTTRyaWEgT2xpdmlhIn0.6YD73XWZYQSSMDf6H0i3-kylz1-TY_Yt6h1cV2Ku-Qs";
    private final String TOKEN_CASO4 = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiTWVtYmVyIiwiT3JnIjoiQlIiLCJTZWVkIjoiMTQ2MjciLCJOYW1lIjoiVmFsZGlyIEFyYW5oYSJ9.cmrXV_Flm5mfdpfNUVopY_I2zeJUy4EZ4i3Fea98zvY";

    @BeforeEach
    void setup() {
        // Configura o MessageSource mockado para retornar as mensagens do arquivo .properties
        when(messageSource.getMessage(eq("jwt.error.empty-token"), any(), any(Locale.class)))
                .thenReturn("Token vazio ou nulo.");
        when(messageSource.getMessage(eq("jwt.error.invalid-format"), any(), any(Locale.class)))
                .thenReturn("JWT invalido.");
        when(messageSource.getMessage(eq("jwt.error.invalid-claim-count"), any(), any(Locale.class)))
                .thenReturn("Abrindo o JWT, o número de claims é diferente de 3.");
        when(messageSource.getMessage(eq("jwt.error.name-has-digits"), any(), any(Locale.class)))
                .thenReturn("Abrindo o JWT, a Claim Name possui caracter de números.");
        when(messageSource.getMessage(eq("jwt.valid"), any(), any(Locale.class)))
                .thenReturn("Token válido.");

    }

    @Test
    void testCaso1_TokenValido() {
        JwtResponseDTO response = jwtValidationService.validateToken(TOKEN_CASO1);

        assertTrue(response.isValid());
        assertEquals("Abrindo o JWT, as informações contidas atendem a descrição.",
                response.getJustificativa());
        assertNotNull(response.getClaims());
        assertEquals("Admin", response.getClaims().get("Role"));
    }

    @Test
    void testCaso2_TokenInvalidoFormato() {
        JwtResponseDTO response = jwtValidationService.validateToken(TOKEN_CASO2);

        assertFalse(response.isValid());
        assertEquals("JWT invalido.", response.getJustificativa());
    }

    @Test
    void testCaso3_TokenComNomeInvalido() {
        JwtResponseDTO response = jwtValidationService.validateToken(TOKEN_CASO3);

        assertFalse(response.isValid());
        assertEquals("Abrindo o JWT, a Claim Name possui caracter de números.", response.getJustificativa());
        // Verifica se ainda retorna as claims mesmo sendo inválido
        assertTrue(response.getClaims().containsKey("Name"));
    }

    @Test
    void testCaso4_TokenComMaisDe3Claims() {
        JwtResponseDTO response = jwtValidationService.validateToken(TOKEN_CASO4);

        assertFalse(response.isValid());
        assertEquals("Abrindo o JWT, o número de claims é diferente de 3.", response.getJustificativa());
        assertTrue(response.getClaims().size() > 3);
    }

    @Test
    void testTokenVazio() {
        JwtResponseDTO response = jwtValidationService.validateToken("");

        assertFalse(response.isValid());
        assertEquals("Token vazio ou nulo.", response.getJustificativa());
    }

    @Test
    void testTokenNulo() {
        JwtResponseDTO response = jwtValidationService.validateToken(null);

        assertFalse(response.isValid());
        assertEquals("Token vazio ou nulo.", response.getJustificativa());
    }
}