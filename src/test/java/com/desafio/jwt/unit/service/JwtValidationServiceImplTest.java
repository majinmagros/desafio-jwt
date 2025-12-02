package com.desafio.jwt.unit.service;

import com.desafio.jwt.constants.ConstantsTest;
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
        JwtResponseDTO response = jwtValidationService.validateToken(ConstantsTest.TOKEN_CASO1);

        assertTrue(response.isValid());
        assertEquals("Abrindo o JWT, as informações contidas atendem a descrição.",
                response.getJustificativa());
        assertNotNull(response.getClaims());
        assertEquals("Admin", response.getClaims().get("Role"));
    }

    @Test
    void testCaso2_TokenInvalidoFormato() {
        JwtResponseDTO response = jwtValidationService.validateToken(ConstantsTest.TOKEN_CASO2);

        assertFalse(response.isValid());
        assertEquals("JWT invalido.", response.getJustificativa());
    }

    @Test
    void testCaso3_TokenComNomeInvalido() {
        JwtResponseDTO response = jwtValidationService.validateToken(ConstantsTest.TOKEN_CASO3);

        assertFalse(response.isValid());
        assertEquals("Abrindo o JWT, a Claim Name possui caracter de números.", response.getJustificativa());
        // Verifica se ainda retorna as claims mesmo sendo inválido
        assertTrue(response.getClaims().containsKey("Name"));
    }

    @Test
    void testCaso4_TokenComMaisDe3Claims() {
        JwtResponseDTO response = jwtValidationService.validateToken(ConstantsTest.TOKEN_CASO4);

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
        JwtResponseDTO response = jwtValidationService.validateToken(ConstantsTest.TOKEN_DIF_3);

        assertFalse(response.isValid());
        assertEquals("Abrindo o JWT, o número de claims é diferente de 3.", response.getJustificativa());
    }

    @Test
    void testMissingClaims() {
        JwtResponseDTO response = jwtValidationService.validateToken(ConstantsTest.TOKEN_MISSING_CLAIMS);

        assertFalse(response.isValid());
        assertEquals("Alguma claim obrigatória está ausente: Name, Role ou Seed.", response.getJustificativa());
    }

    @Test
    void testTooLong() {
        JwtResponseDTO response = jwtValidationService.validateToken(ConstantsTest.TOKEN_NAME_TOO_LONG);

        assertFalse(response.isValid());
        assertEquals("Claim Name excede 256 caracteres.", response.getJustificativa());
    }

    @Test
    void testNameHasDigit() {
        JwtResponseDTO response = jwtValidationService.validateToken(ConstantsTest.TOKEN_NAME_HAS_DIGITS);

        assertFalse(response.isValid());
        assertEquals("Abrindo o JWT, a Claim Name possui caracter de números.", response.getJustificativa());
    }

    @Test
    void testInvalidRole() {
        JwtResponseDTO response = jwtValidationService.validateToken(ConstantsTest.TOKEN_INVALID_ROLE);

        assertFalse(response.isValid());
        assertEquals("Claim Role inválida. Permitidos: Admin, Member, External.", response.getJustificativa());
    }

    @Test
    void testNotPrime() {
        JwtResponseDTO response = jwtValidationService.validateToken(ConstantsTest.TOKEN_NOT_PRIME);

        assertFalse(response.isValid());
        assertEquals("Seed não é um número primo.", response.getJustificativa());
    }

    @Test
    void testNotInteger() {
        JwtResponseDTO response = jwtValidationService.validateToken(ConstantsTest.TOKEN_NOT_INTEGER);

        assertFalse(response.isValid());
        assertEquals("Seed não é um número inteiro válido.", response.getJustificativa());
    }

}