package com.api.controleverbasbackend.dto.usuario;

import com.api.controleverbasbackend.domain.usuario.Usuario;

public record DadosListagemUsuario(
        Long id,
        String email,
        String nomePessoa,
        Integer idTipoUsuario,
        String tipoUsuario,
        Boolean ativo) {
    public DadosListagemUsuario(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getPessoa().getEmail(),
                usuario.getPessoa().getNome(),
                usuario.getTipoUsuario().getId(),
                usuario.getTipoUsuario().getNome(),
                usuario.getAtivo());
    }
}
