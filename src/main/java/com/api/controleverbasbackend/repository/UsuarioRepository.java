package com.api.controleverbasbackend.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.api.controleverbasbackend.domain.usuario.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByPessoaEmail(String email);

    Page<Usuario> findAllByAtivoTrue(Pageable pageable);

    Optional<Usuario> findByIdAndAtivoTrue(Long id);
}
