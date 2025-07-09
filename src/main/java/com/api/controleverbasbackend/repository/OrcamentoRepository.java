package com.api.controleverbasbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.controleverbasbackend.domain.orcamento.Orcamento;

public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {
}
