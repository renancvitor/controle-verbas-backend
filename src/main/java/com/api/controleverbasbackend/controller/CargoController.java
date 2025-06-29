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

import com.api.controleverbasbackend.dto.cargo.DadosCadastroCargo;
import com.api.controleverbasbackend.dto.cargo.DadosDetalhamentoCargo;
import com.api.controleverbasbackend.dto.cargo.DadosListagemCargo;
import com.api.controleverbasbackend.service.CargoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cargos")
public class CargoController {

    @Autowired
    private CargoService cargoService;

    @GetMapping
    public ResponseEntity<Page<DadosListagemCargo>> listar(
            @PageableDefault(size = 20, sort = ("nome")) Pageable pageable) {

        Page<DadosListagemCargo> page = cargoService.listar(pageable);
        return ResponseEntity.ok(page);
    }

    @PostMapping
    public ResponseEntity<DadosDetalhamentoCargo> cadastrar(@RequestBody @Valid DadosCadastroCargo dados,
            UriComponentsBuilder uriComponentsBuilder) {

        DadosDetalhamentoCargo cargo = cargoService.cadastrar(dados);
        URI uri = uriComponentsBuilder.path("/cargos/{id}").buildAndExpand(cargo.id()).toUri();

        return ResponseEntity.created(uri).body(cargo);
    }
}
