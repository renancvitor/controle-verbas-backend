package com.api.controleverbasbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.controleverbasbackend.domain.departamento.Departamento;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
    Optional<Departamento> findByNome(String nome);
}
