package com.api.controleverbasbackend.dto.pessoa;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosCadastroPessoa(@NotBlank(message = "Nome é obrigatório") String nome,
        @NotBlank(message = "CPF é obrigatório") @Pattern(regexp = "\\d{3}\\.?\\d{3}\\.?\\d{3}\\-?\\d{2}", message = "Formato inválido") String cpf,
        @NotBlank(message = "E-mail é obrigatório") @Email(message = "Formato de e-mail inválido") String email,
        @NotNull(message = "Departamento é obrigatório") Long idDepartamento,
        @NotNull(message = "Cargo é obrigatório") Long idCargo) {
}
