package com.api.controleverbasbackend.dto.usuario;

import com.api.controleverbasbackend.domain.entity.usuario.Usuario;

public record DadosDetalhamentoUsuario(Long id, String cpfPessoa, String emailPessoa,
        String nomeTipoUsuario) {

    public DadosDetalhamentoUsuario(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getPessoa().getCpf(),
                usuario.getPessoa().getEmail(),
                usuario.getTipoUsuario().getNome());
    }
}
