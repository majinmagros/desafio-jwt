package com.desafio.jwt.controller;

import com.desafio.jwt.dto.JwtRequestDTO;
import com.desafio.jwt.dto.JwtResponseDTO;
import com.desafio.jwt.service.IJwtValidationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/jwt")
public class JwtController {

    @Autowired
    private IJwtValidationService service;

    @PostMapping("/validate")
    public JwtResponseDTO validate(@RequestBody JwtRequestDTO request) {
        return service.validateToken(request.getToken());
    }
}
