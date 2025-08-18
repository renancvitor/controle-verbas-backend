package com.api.controleverbasbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.controleverbasbackend.domain.entity.usuario.TipoUsuarioEntidade;

public interface TipoUsuarioRepository extends JpaRepository<TipoUsuarioEntidade, Integer> {
}
