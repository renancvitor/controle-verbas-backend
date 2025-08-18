package com.api.controleverbasbackend.dto.cargo;

import com.api.controleverbasbackend.domain.entity.cargo.Cargo;

public record DadosListagemCargo(Long id, String nome, Boolean ativo) {

    public DadosListagemCargo(Cargo cargo) {
        this(
                cargo.getId(),
                cargo.getNome(),
                cargo.getAtivo());
    }
}
