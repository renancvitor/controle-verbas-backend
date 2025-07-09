package com.api.controleverbasbackend.dto.orcamento;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroOrcamento(@NotBlank(message = "Fornecedor é obrigatório.") String fornecedor,
                @NotBlank(message = "Descrição é obrigatório.") String descricao,
                @NotBlank(message = "Forma de pagamento é obrigatório.") String formaPagamento,
                @NotNull(message = "Valor total é obrigatório.") BigDecimal valorTotal,
                String observacoesGerais) {
}
