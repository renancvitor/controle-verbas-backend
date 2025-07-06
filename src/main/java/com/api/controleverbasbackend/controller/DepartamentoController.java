package com.api.controleverbasbackend.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.api.controleverbasbackend.domain.usuario.Usuario;
import com.api.controleverbasbackend.dto.cargo.DadosDetalhamentoCargo;
import com.api.controleverbasbackend.dto.departamento.DadosAtualizacaoDepartamento;
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
            @PageableDefault(size = 20, sort = ("nome")) Pageable pageable, @AuthenticationPrincipal Usuario usuario) {
        Page<DadosListagemDepartamento> page = departamentoService.listar(pageable, usuario);
        return ResponseEntity.ok(page);
    }

    @PostMapping
    public ResponseEntity<DadosDetalhamentoDepartamento> cadastrar(@RequestBody @Valid DadosCadastroDepartamento dados,
            UriComponentsBuilder uriComponentsBuilder, @AuthenticationPrincipal Usuario usuario) {
        DadosDetalhamentoDepartamento departamento = departamentoService.cadastrar(dados, usuario);
        URI uri = uriComponentsBuilder.path("/departamentos/{id}")
                .buildAndExpand(departamento.id())
                .toUri();

        return ResponseEntity.created(uri).body(departamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody @Valid DadosAtualizacaoDepartamento dados,
            @AuthenticationPrincipal Usuario usuario) {
        DadosDetalhamentoDepartamento dadosDepartamento = departamentoService.atualizar(id, dados, usuario);
        return ResponseEntity.ok(dadosDepartamento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
        departamentoService.deletar(id, usuario);
        return ResponseEntity.noContent().build();
    }
}
