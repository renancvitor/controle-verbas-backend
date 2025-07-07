package com.api.controleverbasbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.controleverbasbackend.domain.departamento.Departamento;
import com.api.controleverbasbackend.domain.usuario.TipoUsuarioEnum;
import com.api.controleverbasbackend.domain.usuario.Usuario;
import com.api.controleverbasbackend.dto.departamento.DadosAtualizacaoDepartamento;
import com.api.controleverbasbackend.dto.departamento.DadosCadastroDepartamento;
import com.api.controleverbasbackend.dto.departamento.DadosDetalhamentoDepartamento;
import com.api.controleverbasbackend.dto.departamento.DadosListagemDepartamento;
import com.api.controleverbasbackend.infra.exception.AutorizacaoException;
import com.api.controleverbasbackend.infra.exception.ValidacaoException;
import com.api.controleverbasbackend.repository.DepartamentoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Transactional
    public Page<DadosListagemDepartamento> listar(Pageable pageable, Usuario usuario, Boolean ativo) {
        if (!usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.ADMIN.getId())) {
            throw new AutorizacaoException("Apenas o admin pode listar derpatamentos cadastrados.");
        }

        Boolean filtro = (ativo != null) ? ativo : true;
        return departamentoRepository.findAllByAtivo(filtro, pageable).map(DadosListagemDepartamento::new);
    }

    @Transactional
    public DadosDetalhamentoDepartamento cadastrar(DadosCadastroDepartamento dados, Usuario usuario) {
        if (!usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.ADMIN.getId())) {
            throw new AutorizacaoException("Apenas o admin pode cadastrar novos cargos.");
        }

        Departamento departamento = new Departamento(dados);
        departamentoRepository.save(departamento);
        return new DadosDetalhamentoDepartamento(departamento);
    }

    @Transactional
    public DadosDetalhamentoDepartamento atualizar(Long id, DadosAtualizacaoDepartamento dados, Usuario usuario) {
        Departamento departamento = departamentoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Departamento com ID " + id + " não encontrado."));

        if (!usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.ADMIN.getId())) {
            throw new AutorizacaoException("Apenas o admin pode atualizar departamentos.");
        }
        if (departamento.getNome() == null || departamento.getNome().isBlank()) {
            throw new ValidacaoException("O nome deve ser preenchido corretamente.");
        }

        departamento.atualizar(dados);
        return new DadosDetalhamentoDepartamento(departamento);
    }

    @Transactional
    public void deletar(Long id, Usuario usuario) {
        Departamento departamento = departamentoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Departamento não encontrado."));

        if (!usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.ADMIN.getId())) {
            throw new AutorizacaoException("Apenas o ADMIN pode deletar um departamento.");
        }

        departamento.setAtivo(false);
    }

    @Transactional
    public void ativar(Long id, Usuario usuario) {
        Departamento departamento = departamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Departamento não encontrado."));

        if (!usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.ADMIN.getId())) {
            throw new AutorizacaoException("Apenas o ADMIN pode ativar um departamento.");
        }

        departamento.setAtivo(true);
    }
}
