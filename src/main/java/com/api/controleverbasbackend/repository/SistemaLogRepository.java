package com.api.controleverbasbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.controleverbasbackend.domain.entity.sistemalog.SistemaLog;

public interface SistemaLogRepository extends JpaRepository<SistemaLog, Long> {
}
