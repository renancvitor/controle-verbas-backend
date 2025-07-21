package com.api.controleverbasbackend.domain.cargo;

import com.api.controleverbasbackend.dto.cargo.DadosCadastroCargo;
import com.api.controleverbasbackend.dto.cargo.DadosAtualizacaoCargo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "cargos")
@Entity(name = "Cargo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Cargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nome;

    @Column(nullable = false)
    private Boolean ativo = true;

    public Cargo(DadosCadastroCargo dados) {
        this.nome = dados.nome();
        this.ativo = true;
    }

    public void atualizar(DadosAtualizacaoCargo dados) {
        if (dados.nome() != null) {
            this.nome = dados.nome();
        }
    }
}
