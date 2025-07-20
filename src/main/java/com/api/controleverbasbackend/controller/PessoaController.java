package com.api.controleverbasbackend.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.api.controleverbasbackend.domain.usuario.Usuario;
import com.api.controleverbasbackend.dto.pessoa.DadosAtualizacaoPessoa;
import com.api.controleverbasbackend.dto.pessoa.DadosCadastroPessoaUsuario;
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
    public ResponseEntity<Page<DadosListagemPessoa>> listar(@RequestParam(required = false) Boolean ativo,
            @PageableDefault(size = 5000, sort = ("nome")) Pageable pageable,
            @AuthenticationPrincipal Usuario usuario) {
        Page<DadosListagemPessoa> page = pessoaService.listar(pageable, usuario, ativo);
        return ResponseEntity.ok(page);
    }

    @PostMapping
    public ResponseEntity<DadosDetalhamentoPessoa> cadastrar(
            @RequestBody @Valid DadosCadastroPessoaUsuario dadosCadastro,
            UriComponentsBuilder uriComponentsBuilder, @AuthenticationPrincipal Usuario usuario) {
        DadosDetalhamentoPessoa pessoa = pessoaService.cadastrar(dadosCadastro.pessoa(), dadosCadastro.usuario(),
                usuario);
        URI uri = uriComponentsBuilder.path("/pessoas/{id}")
                .buildAndExpand(pessoa.id())
                .toUri();

        return ResponseEntity.created(uri).body(pessoa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoPessoa> atualizar(@PathVariable Long id,
            @RequestBody @Valid DadosAtualizacaoPessoa dados,
            @AuthenticationPrincipal Usuario usuario) {
        DadosDetalhamentoPessoa dadosPessoa = pessoaService.atualizar(id, dados, usuario);
        return ResponseEntity.ok(dadosPessoa);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
        pessoaService.deletar(id, usuario);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/ativar")
    public ResponseEntity<Void> ativar(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
        pessoaService.ativar(id, usuario);
        return ResponseEntity.noContent().build();
    }
}
