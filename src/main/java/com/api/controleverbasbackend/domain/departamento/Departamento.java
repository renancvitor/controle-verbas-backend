package com.api.controleverbasbackend.domain.departamento;

import com.api.controleverbasbackend.dto.departamento.DadosCadastroDepartamento;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "departamentos")
@Entity(name = "Departamento")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nome;

    public Departamento(DadosCadastroDepartamento dados) {
        this.nome = dados.nome();
    }
}
