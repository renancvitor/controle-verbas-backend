package com.api.controleverbasbackend.dto.usuario;

import com.api.controleverbasbackend.domain.usuario.Usuario;

public record DadosResumidoUsuario(Long id, String nomeTipoUsuario, String nomePessoa) {

    public DadosResumidoUsuario(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getTipoUsuario().getNome(),
                usuario.getPessoa().getNome());
    }
}
