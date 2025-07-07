package com.api.controleverbasbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.controleverbasbackend.domain.usuario.Usuario;
import com.api.controleverbasbackend.dto.usuario.DadosAtualizacaoUsuarioSenha;
import com.api.controleverbasbackend.dto.usuario.DadosAtualizacaoUsuarioTipo;
import com.api.controleverbasbackend.dto.usuario.DadosDetalhamentoUsuario;
import com.api.controleverbasbackend.dto.usuario.DadosListagemUsuario;
import com.api.controleverbasbackend.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<Page<DadosListagemUsuario>> listar(@RequestParam(required = false) Boolean ativo,
            @PageableDefault(size = 20, sort = ("pessoa.email")) Pageable pageable,
            @AuthenticationPrincipal Usuario usuario) {
        Page<DadosListagemUsuario> page = usuarioService.listar(pageable, usuario, ativo);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/senha/{id}")
    public ResponseEntity atualizarSenha(@PathVariable Long id,
            @RequestBody @Valid DadosAtualizacaoUsuarioSenha dados, @AuthenticationPrincipal Usuario usuario) {
        DadosDetalhamentoUsuario dadosUsuario = usuarioService.atualizarSenha(id, dados, usuario);
        return ResponseEntity.ok(dadosUsuario);
    }

    @PutMapping("/tipo/{id}")
    public ResponseEntity atualizarUsuarioTipo(@PathVariable Long id,
            @RequestBody @Valid DadosAtualizacaoUsuarioTipo dados, @AuthenticationPrincipal Usuario usuario) {
        DadosDetalhamentoUsuario dadosUsuario = usuarioService.atualizarUsuarioTipo(id, dados, usuario);
        return ResponseEntity.ok(dadosUsuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id, @AuthenticationPrincipal Usuario Usuario) {
        usuarioService.deletar(id, Usuario);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/ativar")
    public ResponseEntity<Void> ativar(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
        usuarioService.ativar(id, usuario);
        return ResponseEntity.noContent().build();
    }
}
