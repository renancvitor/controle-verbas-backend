package com.api.controleverbasbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.controleverbasbackend.domain.sistemalog.SistemaLog;

public interface SistemaLogRepository extends JpaRepository<SistemaLog, Long> {
}
