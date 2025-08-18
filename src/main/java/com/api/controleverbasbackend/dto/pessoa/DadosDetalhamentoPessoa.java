package com.api.controleverbasbackend.dto.pessoa;

import java.time.LocalDateTime;

import com.api.controleverbasbackend.domain.entity.pessoa.Pessoa;

public record DadosDetalhamentoPessoa(Long id, String nome, String cpf, String email,
        String nomeDepartamento, String nomeCargo, LocalDateTime dataCadastro) {

    public DadosDetalhamentoPessoa(Pessoa pessoa) {
        this(
                pessoa.getId(),
                pessoa.getNome(),
                pessoa.getCpf(),
                pessoa.getEmail(),
                pessoa.getDepartamento().getNome(),
                pessoa.getCargo().getNome(),
                pessoa.getDataCadastro());
    }
}
