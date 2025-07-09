package com.api.controleverbasbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.controleverbasbackend.domain.orcamento.StatusOrcamentoEntidade;

public interface StatusOrcamentoRepository extends JpaRepository<StatusOrcamentoEntidade, Integer> {
}
