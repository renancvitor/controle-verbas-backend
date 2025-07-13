package com.api.controleverbasbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.controleverbasbackend.dto.autenticacao.DadosLogin;
import com.api.controleverbasbackend.dto.autenticacao.DadosTokenJWT;
import com.api.controleverbasbackend.service.AutenticacaoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private AutenticacaoService autenticacaoService;

    @PostMapping
    public ResponseEntity autenticacao(@RequestBody @Valid DadosLogin dados) {
        DadosTokenJWT resposta = autenticacaoService.autenticacao(dados, authenticationManager);
        return ResponseEntity.ok(resposta);
    }
}
