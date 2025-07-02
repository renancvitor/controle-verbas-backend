package com.api.controleverbasbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.controleverbasbackend.dto.usuario.DadosListagemUsuario;
import com.api.controleverbasbackend.service.UsuarioService;

@RestController
@RequestMapping("usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<Page<DadosListagemUsuario>> listar(
            @PageableDefault(size = 20, sort = ("nome")) Pageable pageable) {
        Page<DadosListagemUsuario> page = usuarioService.listar(pageable);
        return ResponseEntity.ok(page);
    }
}
