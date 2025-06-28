package com.api.controleverbasbackend.dto.departamento;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastroDepartamento(@NotBlank(message = "O nome do departamento é obrigatório") String nome) {
}
