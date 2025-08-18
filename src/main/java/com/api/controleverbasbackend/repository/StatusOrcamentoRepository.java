package com.api.controleverbasbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.controleverbasbackend.domain.entity.orcamento.StatusOrcamentoEntidade;

public interface StatusOrcamentoRepository extends JpaRepository<StatusOrcamentoEntidade, Integer> {

    Optional<StatusOrcamentoEntidade> findByNome(String string);
}
