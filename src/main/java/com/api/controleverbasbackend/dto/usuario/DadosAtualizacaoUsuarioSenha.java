package com.api.controleverbasbackend.dto.usuario;

import jakarta.validation.constraints.NotBlank;

public record DadosAtualizacaoUsuarioSenha(
        @NotBlank(message = "A senha atual não pode estar em branco") String senhaAtual,
        @NotBlank(message = "A nova senha não pode estar em branco") String novaSenha,
        @NotBlank(message = "A confirmação da nova senha não pode estar em branco") String confirmarNovaSenha) {
}
