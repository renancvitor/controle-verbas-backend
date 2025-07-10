package com.api.controleverbasbackend.domain.orcamento;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.api.controleverbasbackend.domain.usuario.Usuario;
import com.api.controleverbasbackend.dto.orcamento.DadosCadastroOrcamento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "orcamentos")
@Entity(name = "Orcamento")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Orcamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fornecedor;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private String formaPagamento;

    @Column(name = "valor_total", precision = 15, scale = 2)
    private BigDecimal valorTotal;

    private String observacoesGerais;

    @OneToOne(optional = false)
    @JoinColumn(name = "solicitante_id", nullable = false)
    private Usuario solicitante;

    @OneToOne(optional = true)
    @JoinColumn(name = "gestor_id")
    private Usuario gestor;

    @OneToOne(optional = true)
    @JoinColumn(name = "tesoureiro_id")
    private Usuario tesoureiro;

    @OneToOne(optional = false)
    @JoinColumn(name = "status_orcamento_id", nullable = false)
    private StatusOrcamentoEntidade statusOrcamentoEntidade;

    private LocalDate dataCriacao = LocalDate.now();
    private LocalDate dataAnalise;

    @Column(name = "verba_liberada", nullable = false)
    private Boolean verbaLiberada = false;
    private LocalDate dataLiberacaoVerba;

    public Orcamento(DadosCadastroOrcamento dados) {
        this.fornecedor = dados.fornecedor();
        this.descricao = dados.descricao();
        this.formaPagamento = dados.formaPagamento();
        this.valorTotal = dados.valorTotal();
        this.observacoesGerais = dados.observacoesGerais();
        this.verbaLiberada = false;
    }

    public String getVerbaLiberadaFormatado() {
        return this.verbaLiberada ? "Sim" : "Não";
    }
}
