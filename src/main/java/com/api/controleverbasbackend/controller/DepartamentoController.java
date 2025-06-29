package com.api.controleverbasbackend.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.api.controleverbasbackend.dto.departamento.DadosCadastroDepartamento;
import com.api.controleverbasbackend.dto.departamento.DadosDetalhamentoDepartamento;
import com.api.controleverbasbackend.dto.departamento.DadosListagemDepartamento;
import com.api.controleverbasbackend.service.DepartamentoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/departamentos")
public class DepartamentoController {

    @Autowired
    private DepartamentoService departamentoService;

    @GetMapping
    public ResponseEntity<Page<DadosListagemDepartamento>> listar(
            @PageableDefault(size = 20, sort = ("nome")) Pageable pageable) {
        Page<DadosListagemDepartamento> page = departamentoService.listar(pageable);
        return ResponseEntity.ok(page);
    }

    @PostMapping
    public ResponseEntity<DadosDetalhamentoDepartamento> cadastrar(@RequestBody @Valid DadosCadastroDepartamento dados,
            UriComponentsBuilder uriComponentsBuilder) {
        DadosDetalhamentoDepartamento departamento = departamentoService.cadastrar(dados);
        URI uri = uriComponentsBuilder.path("/departamentos/{id}")
                .buildAndExpand(departamento.id())
                .toUri();

        return ResponseEntity.created(uri).body(departamento);
    }
}
