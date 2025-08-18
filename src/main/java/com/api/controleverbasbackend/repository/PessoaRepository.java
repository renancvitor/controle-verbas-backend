package com.api.controleverbasbackend.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.api.controleverbasbackend.domain.entity.pessoa.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    Optional<Pessoa> findByNome(String nome);

    Page<Pessoa> findAllByAtivoTrue(Pageable pageable);

    Optional<Pessoa> findByIdAndAtivoTrue(Long id);

    Optional<Pessoa> findByIdAndAtivoFalse(Long id);

    Page<Pessoa> findAllByAtivo(Boolean ativo, Pageable pageable);
}
