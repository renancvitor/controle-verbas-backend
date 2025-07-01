package com.api.controleverbasbackend.dto;

import com.api.controleverbasbackend.dto.pessoa.DadosCadastroPessoa;
import com.api.controleverbasbackend.dto.usuario.DadosCadastroUsuario;

public record DadosCadastroPessoaUsuario(DadosCadastroPessoa pessoa,
        DadosCadastroUsuario usuario) {
}
