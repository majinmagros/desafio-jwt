package com.desafio.jwt.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
public class JwtRequestDTO {

    @NotBlank(message = "O token JWT não pode ser vazio")
    private String token;
}
