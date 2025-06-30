package com.api.controleverbasbackend.domain.pessoa;

import java.time.LocalDateTime;

import com.api.controleverbasbackend.domain.cargo.Cargo;
import com.api.controleverbasbackend.domain.departamento.Departamento;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "pessoas")
@Entity(name = "Pessoa")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nome;

    @Column(unique = true, nullable = false)
    private String cpf;

    @Column(unique = true, nullable = false)
    private String email;

    @ManyToOne
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;

    @ManyToOne
    @JoinColumn(name = "cargo_id")
    private Cargo cargo;

    private LocalDateTime dataCadastro;

    public Pessoa(String nome, String cpf, String email, Departamento departamento, Cargo cargo) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.departamento = departamento;
        this.cargo = cargo;
        this.dataCadastro = LocalDateTime.now();
    }
}
