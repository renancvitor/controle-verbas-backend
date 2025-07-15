package com.api.controleverbasbackend.dto.departamento;

import com.api.controleverbasbackend.domain.departamento.Departamento;

public record DadosListagemDepartamento(Long id, String nome, Boolean ativo) {

    public DadosListagemDepartamento(Departamento departamento) {
        this(
                departamento.getId(),
                departamento.getNome(),
                departamento.getAtivo());
    }
}
