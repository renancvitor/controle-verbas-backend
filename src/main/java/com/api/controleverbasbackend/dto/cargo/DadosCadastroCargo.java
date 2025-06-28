package com.api.controleverbasbackend.dto.cargo;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastroCargo(@NotBlank(message = "O nome do cargo é obrigatório.") String nome) {
}
