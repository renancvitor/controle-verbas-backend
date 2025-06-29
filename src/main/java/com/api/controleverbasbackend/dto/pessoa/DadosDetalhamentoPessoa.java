package com.api.controleverbasbackend.dto.pessoa;

import java.time.LocalDateTime;

import com.api.controleverbasbackend.domain.pessoa.Pessoa;

public record DadosDetalhamentoPessoa(Long id, String nome, String cpf, String email, LocalDateTime dataCadastro) {

    public DadosDetalhamentoPessoa(Pessoa pessoa) {
        this(
                pessoa.getId(),
                pessoa.getNome(),
                pessoa.getCpf(),
                pessoa.getEmail(),
                pessoa.getDataCadastro());
    }
}
