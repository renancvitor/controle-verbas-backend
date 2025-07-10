package com.api.controleverbasbackend.dto.orcamento;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.api.controleverbasbackend.domain.orcamento.Orcamento;

public record DadosListagemOrcamento(
        Long id,
        String fornecedor,
        String descricao,
        String formaPagamento,
        BigDecimal valorTotal,
        String observacoesGerais,
        String solicitanteNome,
        String gestorNome,
        String tesoureiroNome,
        String status,
        LocalDate dataCriacao,
        LocalDate dataAnalise,
        String verbaLiberada,
        LocalDate dataLiberacaoVerba) {

    public DadosListagemOrcamento(Orcamento orcamento) {
        this(
                orcamento.getId(),
                orcamento.getFornecedor(),
                orcamento.getDescricao(),
                orcamento.getFormaPagamento(),
                orcamento.getValorTotal(),
                orcamento.getObservacoesGerais(),
                orcamento.getSolicitante().getPessoa().getNome(),
                orcamento.getGestor() != null ? orcamento.getGestor().getPessoa().getNome() : null,
                orcamento.getTesoureiro() != null ? orcamento.getTesoureiro().getPessoa().getNome() : null,
                orcamento.getStatusOrcamentoEntidade().getNome(),
                orcamento.getDataCriacao(),
                orcamento.getDataAnalise(),
                orcamento.getVerbaLiberadaFormatado(),
                orcamento.getDataLiberacaoVerba());
    }
}
