package com.api.controleverbasbackend.dto.usuario;

import com.api.controleverbasbackend.domain.usuario.Usuario;

public record DadosListagemUsuario(Long id, String cpf, String email,
        String TipoUsuario, Boolean ativo) {
    public DadosListagemUsuario(Usuario usuario) {
        this(usuario.getId(),
                usuario.getPessoa().getCpf(),
                usuario.getPessoa().getEmail(),
                usuario.getTipoUsuario().getNome(),
                usuario.getAtivo());
    }
}
