package com.api.controleverbasbackend.dto;

import com.api.controleverbasbackend.domain.cargo.Cargo;

public record DadosDetalhamentoCargo(Long id, String nome) {

    public DadosDetalhamentoCargo(Cargo cargo) {
        this(
                cargo.getId(),
                cargo.getNome());
    }
}
