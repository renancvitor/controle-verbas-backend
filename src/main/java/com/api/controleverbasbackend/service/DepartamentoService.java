package com.api.controleverbasbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.controleverbasbackend.domain.departamento.Departamento;
import com.api.controleverbasbackend.domain.usuario.TipoUsuarioEnum;
import com.api.controleverbasbackend.domain.usuario.Usuario;
import com.api.controleverbasbackend.dto.departamento.DadosCadastroDepartamento;
import com.api.controleverbasbackend.dto.departamento.DadosDetalhamentoDepartamento;
import com.api.controleverbasbackend.dto.departamento.DadosListagemDepartamento;
import com.api.controleverbasbackend.infra.exception.AutorizacaoException;
import com.api.controleverbasbackend.repository.DepartamentoRepository;

@Service
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Transactional
    public Page<DadosListagemDepartamento> listar(Pageable pageable, Usuario usuario) {
        if (!usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.ADMIN.name())) {
            throw new AutorizacaoException("Apenas o admin pode listar derpatamentos cadastrados.");
        }
        return departamentoRepository.findAll(pageable).map(DadosListagemDepartamento::new);
    }

    @Transactional
    public DadosDetalhamentoDepartamento cadastrar(DadosCadastroDepartamento dados, Usuario usuario) {
        if (!usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.ADMIN.name())) {
            throw new AutorizacaoException("Apenas o admin pode cadastrar novos cargos.");
        }
        Departamento departamento = new Departamento();
        departamentoRepository.save(departamento);
        return new DadosDetalhamentoDepartamento(departamento);
    }
}
