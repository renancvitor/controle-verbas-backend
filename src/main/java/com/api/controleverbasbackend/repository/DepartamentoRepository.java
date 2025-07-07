package com.api.controleverbasbackend.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.api.controleverbasbackend.domain.departamento.Departamento;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
    Optional<Departamento> findByNome(String nome);

    Page<Departamento> findAllByAtivoTrue(Pageable pageable);

    Optional<Departamento> findByIdAndAtivoTrue(Long id);

    Page<Departamento> findAllByAtivo(Boolean ativo, Pageable pageable);
}
