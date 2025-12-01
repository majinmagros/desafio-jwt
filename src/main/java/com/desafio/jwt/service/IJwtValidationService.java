package com.desafio.jwt.service;

import com.desafio.jwt.dto.JwtResponseDTO;
import jakarta.validation.constraints.NotBlank;

public interface IJwtValidationService {

    JwtResponseDTO validateToken(String token);


}
