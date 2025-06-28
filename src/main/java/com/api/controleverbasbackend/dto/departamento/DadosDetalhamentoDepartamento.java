package com.api.controleverbasbackend.dto.departamento;

import com.api.controleverbasbackend.domain.departamento.Departamento;

public record DadosDetalhamentoDepartamento(Long id, String nome) {

    public DadosDetalhamentoDepartamento(Departamento departamento) {
        this(
                departamento.getId(),
                departamento.getNome());
    }
}
