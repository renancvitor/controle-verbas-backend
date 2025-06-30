package com.api.controleverbasbackend.dto.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosCadastroUsuario(
        @NotBlank(message = "CPF é obrigatório") @Pattern(regexp = "\\d{3}\\.?\\d{3}\\.?\\d{3}\\-?\\d{2}", message = "Formato inválido") String cpfPessoa,
        @NotBlank(message = "Senha é obrigatório") String senha,
        @NotNull(message = "Não pode ser vazio") Long idTipousuario) {
}
