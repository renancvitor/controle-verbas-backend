package com.api.controleverbasbackend.dto.cargo;

import com.api.controleverbasbackend.domain.entity.cargo.Cargo;

public record DadosDetalhamentoCargo(Long id, String nome) {

    public DadosDetalhamentoCargo(Cargo cargo) {
        this(
                cargo.getId(),
                cargo.getNome());
    }
}
