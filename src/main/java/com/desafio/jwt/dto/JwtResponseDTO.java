package com.desafio.jwt.dto;

import lombok.Data;

import java.util.Map;

@Data
public class JwtResponseDTO {

    private boolean valid;
    private String justificativa;
    private Map<String, String> claims;

    public JwtResponseDTO(boolean valid, String justificativa, Map<String, String> claims) {
        this.valid = valid;
        this.justificativa = justificativa;
        this.claims = claims;
    }
}
