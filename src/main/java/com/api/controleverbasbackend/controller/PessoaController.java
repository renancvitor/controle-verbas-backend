package com.api.controleverbasbackend.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.api.controleverbasbackend.domain.usuario.Usuario;
import com.api.controleverbasbackend.dto.DadosCadastroPessoaUsuario;
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
            @PageableDefault(size = 20, sort = ("nome")) Pageable pageable, @AuthenticationPrincipal Usuario usuario) {
        Page<DadosListagemPessoa> page = pessoaService.listar(pageable, usuario);
        return ResponseEntity.ok(page);
    }

    @PostMapping
    public ResponseEntity<DadosDetalhamentoPessoa> cadastrar(
            @RequestBody @Valid DadosCadastroPessoaUsuario dadosCadastro,
            UriComponentsBuilder uriComponentsBuilder) {
        DadosDetalhamentoPessoa pessoa = pessoaService.cadastrar(dadosCadastro.pessoa(), dadosCadastro.usuario());
        URI uri = uriComponentsBuilder.path("/pessoas/{id}")
                .buildAndExpand(pessoa.id())
                .toUri();

        return ResponseEntity.created(uri).body(pessoa);
    }
}
