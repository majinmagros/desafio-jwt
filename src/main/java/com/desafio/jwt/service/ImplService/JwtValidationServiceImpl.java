package com.desafio.jwt.service.ImplService;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.desafio.jwt.dto.JwtResponseDTO;
import com.desafio.jwt.exception.InvalidJwtException;
import com.desafio.jwt.exception.MessagesCode;
import com.desafio.jwt.service.IJwtValidationService;
import com.desafio.jwt.util.PrimeNumberValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JwtValidationServiceImpl implements IJwtValidationService {

    private static final Logger log = LoggerFactory.getLogger(JwtValidationServiceImpl.class);

    @Autowired
    private MessageSource messageSource;

    @Override
    public JwtResponseDTO validateToken(String token) {

        try {

            DecodedJWT jwt = validate(token);

            Map<String, String> claims = jwt.getClaims().entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> e.getValue().asString()
                    ));

            log.info("Token válido. Name={}, Role={}, Seed={}",
                    jwt.getClaim("Name").asString(),
                    jwt.getClaim("Role").asString(),
                    jwt.getClaim("Seed").asString());

            String justificativa = "Abrindo o JWT, as informações contidas atendem a descrição.";

            return new JwtResponseDTO(true, justificativa, claims);

        } catch (InvalidJwtException e) {

            Map<String, String> claims = extractClaimsSafely(token);

            log.warn("Token inválido. Motivo={}", e.getMessage());
            log.info("Claims recebidos (mesmo com erro): {}", claims);

            return new JwtResponseDTO(false, e.getMessage(), claims);
        }
    }

    private DecodedJWT validate(String token) {

        if (token == null || token.isBlank()) {
            throw error(MessagesCode.EMPTY_TOKEN);
        }

        DecodedJWT jwt;
        try {
            jwt = JWT.decode(token);
        } catch (Exception e) {
            throw error(MessagesCode.INVALID_FORMAT);
        }

        Map<String, ?> claims = jwt.getClaims();
        if (claims.size() != 3) {
            throw error(MessagesCode.INVALID_CLAIM_COUNT);
        }

        String name = jwt.getClaim("Name").asString();
        String role = jwt.getClaim("Role").asString();
        String seedStr = jwt.getClaim("Seed").asString();

        if (name == null || role == null || seedStr == null) {
            throw error(MessagesCode.MISSING_CLAIMS);
        }

        if (name.length() > 256) {
            throw error(MessagesCode.NAME_TOO_LONG);
        }

        if (name.matches(".*\\d.*")) {
            throw error(MessagesCode.NAME_HAS_DIGITS);
        }

        if (!role.equals("Admin") && !role.equals("Member") && !role.equals("External")) {
            throw error(MessagesCode.INVALID_ROLE);
        }

        int seed;
        try {
            seed = Integer.parseInt(seedStr);
        } catch (Exception e) {
            throw error(MessagesCode.SEED_NOT_INTEGER);
        }

        if (!PrimeNumberValidator.isPrime(seed)) {
            throw error(MessagesCode.SEED_NOT_PRIME);
        }

        return jwt;
    }

    private Map<String, String> extractClaimsSafely(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaims().entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> e.getValue().asString()
                    ));
        } catch (Exception e) {
            return Map.of();
        }
    }

    private InvalidJwtException error(MessagesCode code) {
        return new InvalidJwtException(
                messageSource.getMessage(code.key(), null, Locale.getDefault())
        );
    }
}
