package com.api.controleverbasbackend.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.api.controleverbasbackend.dto.pessoa.DadosCadastroPessoa;
import com.api.controleverbasbackend.dto.pessoa.DadosDetalhamentoPessoa;
import com.api.controleverbasbackend.dto.pessoa.DadosListagemPessoa;
import com.api.controleverbasbackend.service.PessoaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;

    @GetMapping
    public ResponseEntity<Page<DadosListagemPessoa>> listar(
            @PageableDefault(size = 20, sort = ("nome")) Pageable pageable) {
        Page<DadosListagemPessoa> page = pessoaService.listar(pageable);
        return ResponseEntity.ok(page);
    }

    @PostMapping
    public ResponseEntity<DadosDetalhamentoPessoa> cadastrar(@RequestBody @Valid DadosCadastroPessoa dados,
            UriComponentsBuilder uriComponentsBuilder) {
        DadosDetalhamentoPessoa pessoa = pessoaService.cadastrar(dados);
        URI uri = uriComponentsBuilder.path("/pessoas/{id}")
                .buildAndExpand(pessoa.id())
                .toUri();

        return ResponseEntity.created(uri).body(pessoa);
    }

}
