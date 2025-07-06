package com.api.controleverbasbackend.domain.pessoa;

import java.time.LocalDateTime;

import com.api.controleverbasbackend.domain.cargo.Cargo;
import com.api.controleverbasbackend.domain.departamento.Departamento;
import com.api.controleverbasbackend.dto.pessoa.DadosAtualizacaoPessoa;
import com.api.controleverbasbackend.repository.CargoRepository;
import com.api.controleverbasbackend.repository.DepartamentoRepository;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "pessoas")
@Entity(name = "Pessoa")
@Getter
@Setter
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

    @Column(nullable = false)
    private Boolean ativo = true;

    public Pessoa(String nome, String cpf, String email, Departamento departamento, Cargo cargo) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.departamento = departamento;
        this.cargo = cargo;
        this.dataCadastro = LocalDateTime.now();
        this.ativo = true;
    }

    public void atualizar(DadosAtualizacaoPessoa dados,
            CargoRepository cargoRepository,
            DepartamentoRepository departamentoRepository) {
        if (dados.idCargo() != null) {
            this.cargo = cargoRepository.findById(dados.idCargo())
                    .orElseThrow(() -> new RuntimeException("Cargo não encontrado"));
        }
        if (dados.idDepartamento() != null) {
            this.departamento = departamentoRepository.findById(dados.idDepartamento())
                    .orElseThrow(() -> new RuntimeException("Departamento não encontrado"));
        }
    }
}
