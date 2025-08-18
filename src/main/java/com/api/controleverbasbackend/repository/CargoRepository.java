package com.api.controleverbasbackend.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.api.controleverbasbackend.domain.entity.cargo.Cargo;

public interface CargoRepository extends JpaRepository<Cargo, Long> {
    Optional<Cargo> findByNome(String nome);

    Page<Cargo> findAllByAtivoTrue(Pageable pageable);

    Optional<Cargo> findByIdAndAtivoTrue(Long id);

    Optional<Cargo> findByIdAndAtivoFalse(Long id);

    Page<Cargo> findAllByAtivo(Boolean ativo, Pageable pageable);
}
