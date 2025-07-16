package com.api.controleverbasbackend.dto.pessoa;

import java.time.LocalDateTime;

import com.api.controleverbasbackend.domain.pessoa.Pessoa;

public record DadosListagemPessoa(Long id, String nome, String cpf, String email,
        String nomeDepartamento, String nomeCargo, LocalDateTime dataCadastro, Boolean ativo) {

    public DadosListagemPessoa(Pessoa pessoa) {
        this(
                pessoa.getId(),
                pessoa.getNome(),
                pessoa.getCpf(),
                pessoa.getEmail(),
                pessoa.getDepartamento().getNome(),
                pessoa.getCargo().getNome(),
                pessoa.getDataCadastro(),
                pessoa.getAtivo());
    }
}
