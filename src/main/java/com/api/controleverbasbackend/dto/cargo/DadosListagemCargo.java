package com.api.controleverbasbackend.dto.cargo;

import com.api.controleverbasbackend.domain.cargo.Cargo;

import jakarta.validation.constraints.NotBlank;

public record DadosListagemCargo(Long id, String nome) {

    public DadosListagemCargo(Cargo cargo) {
        this(
                cargo.getId(),
                cargo.getNome());
    }
}
