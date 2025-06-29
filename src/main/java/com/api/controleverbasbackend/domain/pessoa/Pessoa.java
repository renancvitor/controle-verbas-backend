package com.api.controleverbasbackend.domain.pessoa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;

import java.time.LocalDateTime;

import com.api.controleverbasbackend.dto.pessoa.DadosCadastroPessoa;

import jakarta.persistence.*;
import jakarta.persistence.Table;
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

    private String cpf;
    private String email;
    private LocalDateTime dataCadastro;

    public Pessoa(DadosCadastroPessoa dados) {
        this.nome = dados.nome();
        this.cpf = dados.cpf();
        this.email = dados.email()
    }
}
