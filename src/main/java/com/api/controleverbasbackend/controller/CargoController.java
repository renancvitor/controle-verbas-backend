package com.api.controleverbasbackend.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.api.controleverbasbackend.domain.usuario.Usuario;
import com.api.controleverbasbackend.dto.cargo.DadosCadastroCargo;
import com.api.controleverbasbackend.dto.cargo.DadosDetalhamentoCargo;
import com.api.controleverbasbackend.dto.cargo.DadosListagemCargo;
import com.api.controleverbasbackend.dto.cargo.DadosatualizacaoCargo;
import com.api.controleverbasbackend.service.CargoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cargos")
public class CargoController {

    @Autowired
    private CargoService cargoService;

    @GetMapping
    public ResponseEntity<Page<DadosListagemCargo>> listar(
            @PageableDefault(size = 20, sort = ("nome")) Pageable pageable, @AuthenticationPrincipal Usuario usuario) {

        Page<DadosListagemCargo> page = cargoService.listar(pageable, usuario);
        return ResponseEntity.ok(page);
    }

    @PostMapping
    public ResponseEntity<DadosDetalhamentoCargo> cadastrar(@RequestBody @Valid DadosCadastroCargo dados,
            UriComponentsBuilder uriComponentsBuilder, @AuthenticationPrincipal Usuario usuario) {

        DadosDetalhamentoCargo cargo = cargoService.cadastrar(dados, usuario);
        URI uri = uriComponentsBuilder.path("/cargos/{id}").buildAndExpand(cargo.id()).toUri();

        return ResponseEntity.created(uri).body(cargo);
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody @Valid DadosatualizacaoCargo dados,
            @AuthenticationPrincipal Usuario usuario) {
        DadosDetalhamentoCargo dadosCargo = cargoService.atualizar(id, dados, usuario);
        return ResponseEntity.ok(dadosCargo);
    }
}
