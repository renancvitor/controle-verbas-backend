package com.api.controleverbasbackend.domain.departamento;

import com.api.controleverbasbackend.dto.departamento.DadosAtualizacaoDepartamento;
import com.api.controleverbasbackend.dto.departamento.DadosCadastroDepartamento;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "departamentos")
@Entity(name = "Departamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nome;

    @Column(nullable = false)
    private Boolean ativo = true;

    public Departamento(DadosCadastroDepartamento dados) {
        this.nome = dados.nome();
        this.ativo = true;
    }

    public void atualizar(DadosAtualizacaoDepartamento dados) {
        if (dados.nome() != null) {
            this.nome = dados.nome();
        }
    }
}
