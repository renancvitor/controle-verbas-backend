package com.api.controleverbasbackend.dto.pessoa;

import com.api.controleverbasbackend.dto.usuario.DadosCadastroUsuario;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroPessoaUsuario(@NotNull @Valid DadosCadastroPessoa pessoa,
        DadosCadastroUsuario usuario) {
}
