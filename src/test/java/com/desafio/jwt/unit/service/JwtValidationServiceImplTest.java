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
  // Tokens adicionais
    private final String TOKEN_DIF_3 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyIsIkFkZHJlc3MiOiJSdWEgRG9uYSBBbmEgTmVyeSJ9.nRc_0jW0zKzQJ7dX4l2t9v8qPwLm1sB3gT6yH5vN7xZ9c";
    private final String TOKEN_MISSING_CLAIMS = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjpudWxsLCJOYW1lIjoiSm9hbyBkYSBTaWx2YSJ9.pY3w2qLmX8rB6zV9nT0yS1R4U5I7O2P9Q1W4E6A8C3B";
    private final String TOKEN_TOO_LONG = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJSb2xlIjoiRXh0ZXJuYWwiLCJTZWVkIjoiNTg5MSIsIk5hbWUiOiJNYXJpYSBPbGl2aWEgZGEgU2lsdmEgU2FudG9zIE1hY2hhZG8gZSBPbGl2ZWlyYSBkYSBDb3N0YSBQb250ZXMgUGVyZWlyYSBkZSBBbG1laWRhIEN1bmhhIFJvZHJpZ3VlcyBkZSBTb3VzYSBGb250ZXMgTmFzY2ltZW50byBkYSBTaWx2YSBlIFNpbHZhIGRlIE9saXZlaXJhIGRlIFNhbnRhIENydXogZGUgU2FudG8gQW50w7RuaW8gZGUgUOFkdWEgZGUgQXNzaXMgZGUgU2lxdWVpcmEgZGUgTW91cmEgZGUgQWxjw6JudGFyYSBkZSBCYXJyb3MgZGUgTGltYSBkZSBBcmHDuqpvIGRlIENhcnZhbGhvIGRlIEZlcnJlaXJhIGRlIFJpYmFzIGRlIENhc3RybyBkZSBNb250ZWlybyBkZSBGZXJuYW5kZXMgZGUgR29tZXMgZGUgTWFydGlucyBkZSBSYW1vcyBkZSBUZWl4ZWlyYSBkZSBDb3JyZWlhIGRlIE1lbG8gZGUgTG9wZXMgZGUgQ2FydmFsaG8gZGUgTW91cmEgZGEgU2lsdmEgU2FudG9zIE1hY2hhZG8gZSBPbGl2ZWlyYSBkYSBDb3N0YSBQb250ZXMgUGVyZWlyYSBkZSBBbG1laWRhIEN1bmhhIFJvZHJpZ3VlcyBkZSBTb3VzYSBGb250ZXMgTmFzY2ltZW50byBkYSBTaWx2YSBlIFNpbHZhIGRlIE9saXZlaXJhIGRlIFNhbnRhIENydXogZGUgU2FudG8gQW50w7RuaW8gZGUgUOFkdWEgZGUgQXNzaXMgZGUgU2lxdWVpcmEgZGUgTW91cmEgZGUgQWxjw6JudGFyYSBkZSBCYXJyb3MgZGUgTGltYSBkZSBBcmHDuqpvIGRlIENhcnZhbGhvIGRlIEZlcnJlaXJhIGRlIFJpYmFzIGRlIENhc3RybyBkZSBNb250ZWlybyBkZSBGZXJuYW5kZXMgZGUgR29tZXMgZGUgTWFydGlucyBkZSBSYW1vcyBkZSBUZWl4ZWlyYSBkZSBDb3JyZWlhIGRlIE1lbG8gZGUgTG9wZXMgZGUgQ2FydmFsaG8gZGUgTW91cmEifQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
    private final String TOKEN_NAME_HAS_DIGITS = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiRXh0ZXJuYWwiLCJTZWVkIjoiODgwMzciLCJOYW1lIjoiTTRyaWEgT2xpdmlhIn0.6YD73XWZYQSSMDf6H0i3-kylz1-TY_Yt6h1cV2Ku-Qs";
    private final String TOKEN_INVALID_ROLE = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiVXNlciIsIlNlZWQiOiIxNDYyNyIsIk5hbWUiOiJJcm1hbyBkbyBKb3JlbCJ9.ZHVtbXk";
    private final String TOKEN_NOT_PRIME = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJSb2xlIjoiRXh0ZXJuYWwiLCJTZWVkIjoiNzg0MCIsIk5hbWUiOiJKb3JlbCJ9.KmM7nP2qR5sV8wB1yC4zF6gH9jL3oI0xT2uW5aY7d";
    private final String TOKEN_NOT_INTEGER = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiMTQ2MjcuOCIsIk5hbWUiOiJTdGV2ZSBNYWdhbCJ9.ZHVtbXk";

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
        when(messageSource.getMessage(eq("jwt.error.missing-claims"), any(), any(Locale.class)))
                .thenReturn("Alguma claim obrigatória está ausente: Name, Role ou Seed.");
        when(messageSource.getMessage(eq("jwt.error.name-too-long"), any(), any(Locale.class)))
                .thenReturn("Claim Name excede 256 caracteres.");
        when(messageSource.getMessage(eq("jwt.error.invalid-role"), any(), any(Locale.class)))
                .thenReturn("Claim Role inválida. Permitidos: Admin, Member, External.");
        when(messageSource.getMessage(eq("jwt.error.seed-not-integer"), any(), any(Locale.class)))
                .thenReturn("Seed não é um número inteiro válido.");
        when(messageSource.getMessage(eq("jwt.error.seed-not-prime"), any(), any(Locale.class)))
                .thenReturn("Seed não é um número primo.");

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

    @Test
    void testMore3() {
        JwtResponseDTO response = jwtValidationService.validateToken(TOKEN_DIF_3);

        assertFalse(response.isValid());
        assertEquals("Abrindo o JWT, o número de claims é diferente de 3.", response.getJustificativa());
    }

    @Test
    void testMissingClaims() {
        JwtResponseDTO response = jwtValidationService.validateToken(TOKEN_MISSING_CLAIMS);

        assertFalse(response.isValid());
        assertEquals("Alguma claim obrigatória está ausente: Name, Role ou Seed.", response.getJustificativa());
    }

    @Test
    void testTooLong() {
        JwtResponseDTO response = jwtValidationService.validateToken(TOKEN_TOO_LONG);

        assertFalse(response.isValid());
        assertEquals("Claim Name excede 256 caracteres.", response.getJustificativa());
    }

    @Test
    void testNameHasDigit() {
        JwtResponseDTO response = jwtValidationService.validateToken(TOKEN_NAME_HAS_DIGITS);

        assertFalse(response.isValid());
        assertEquals("Abrindo o JWT, a Claim Name possui caracter de números.", response.getJustificativa());
    }

    @Test
    void testInvalidRole() {
        JwtResponseDTO response = jwtValidationService.validateToken(TOKEN_INVALID_ROLE);

        assertFalse(response.isValid());
        assertEquals("Claim Role inválida. Permitidos: Admin, Member, External.", response.getJustificativa());
    }

    @Test
    void testNotPrime() {
        JwtResponseDTO response = jwtValidationService.validateToken(TOKEN_NOT_PRIME);

        assertFalse(response.isValid());
        assertEquals("Seed não é um número primo.", response.getJustificativa());
    }

    @Test
    void testNotInteger() {
        JwtResponseDTO response = jwtValidationService.validateToken(TOKEN_NOT_INTEGER);

        assertFalse(response.isValid());
        assertEquals("Seed não é um número inteiro válido.", response.getJustificativa());
    }

}