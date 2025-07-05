package com.api.controleverbasbackend.dto.usuario;

import jakarta.validation.constraints.NotBlank;

public record DadosAtualizacaoUsuarioSenha(@NotBlank(message = "A nova senha não pode estar em branco") String senha) {
}
