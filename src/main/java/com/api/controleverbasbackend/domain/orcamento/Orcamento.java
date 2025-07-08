package com.api.controleverbasbackend.domain.orcamento;

import java.time.LocalDate;

import org.springframework.cglib.core.Local;

import com.api.controleverbasbackend.domain.usuario.Usuario;

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

    @Column(nullable = false)
    private Double valorTotal;
    private String observacoesGerais;

    @OneToOne(optional = false)
    @JoinColumn(name = "solicitante_id", nullable = false, unique = true)
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

    private LocalDate dataCriacao;
    private LocalDate dataAnalise;
    private LocalDate dataLiberacaoVerba;
}
