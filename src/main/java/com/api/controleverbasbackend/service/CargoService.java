package com.api.controleverbasbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.controleverbasbackend.domain.entity.cargo.Cargo;
import com.api.controleverbasbackend.domain.entity.usuario.Usuario;
import com.api.controleverbasbackend.domain.enums.usuario.TipoUsuarioEnum;
import com.api.controleverbasbackend.dto.cargo.DadosCadastroCargo;
import com.api.controleverbasbackend.dto.cargo.DadosDetalhamentoCargo;
import com.api.controleverbasbackend.dto.cargo.DadosListagemCargo;
import com.api.controleverbasbackend.exception.AutorizacaoException;
import com.api.controleverbasbackend.exception.ValidacaoException;
import com.api.controleverbasbackend.dto.cargo.DadosAtualizacaoCargo;
import com.api.controleverbasbackend.repository.CargoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CargoService {

    @Autowired
    private CargoRepository cargoRepository;

    @Transactional
    public Page<DadosListagemCargo> listar(Pageable pageable, Usuario usuario, Boolean ativo) {
        if (!usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.ADMIN.getId()) &&
                !usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.TESTER.getId())) {
            throw new AutorizacaoException("Apenas o admin pode listar cargos cadastrados.");
        }

        if (ativo != null) {
            return cargoRepository.findAllByAtivo(ativo, pageable).map(DadosListagemCargo::new);
        }

        return cargoRepository.findAll(pageable).map(DadosListagemCargo::new);
    }

    @Transactional
    public DadosDetalhamentoCargo cadastrar(DadosCadastroCargo dados, Usuario usuario) {
        if (usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.TESTER.getId())) {
            throw new AutorizacaoException("Usuário TESTER não pode alterar dados.");
        }

        if (!usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.ADMIN.getId())) {
            throw new AutorizacaoException("Apenas o admin pode cadastrar novos cargos.");
        }

        Cargo cargo = new Cargo(dados);
        cargoRepository.save(cargo);
        return new DadosDetalhamentoCargo(cargo);
    }

    @Transactional
    public DadosDetalhamentoCargo atualizar(Long id, DadosAtualizacaoCargo dados, Usuario usuario) {
        Cargo cargo = cargoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Cargo com ID " + id + " não encontrado."));

        if (usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.TESTER.getId())) {
            throw new AutorizacaoException("Usuário TESTER não pode alterar dados.");
        }

        if (!usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.ADMIN.getId())) {
            throw new AutorizacaoException("Apenas o admin pode atualizar cargos.");
        }
        if (cargo.getNome() == null || cargo.getNome().isBlank()) {
            throw new ValidacaoException("O nome deve ser preenchido corretamente.");
        }

        cargo.atualizar(dados);
        return new DadosDetalhamentoCargo(cargo);
    }

    @Transactional
    public void deletar(Long id, Usuario usuario) {
        Cargo cargo = cargoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Cargo não encontrado."));

        if (usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.TESTER.getId())) {
            throw new AutorizacaoException("Usuário TESTER não pode alterar dados.");
        }

        if (!usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.ADMIN.getId())) {
            throw new AutorizacaoException("Apenas o admin pode deletar um cargo.");
        }

        cargo.setAtivo(false);
    }

    @Transactional
    public void ativar(Long id, Usuario usuario) {
        Cargo cargo = cargoRepository.findByIdAndAtivoFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Cargo não encontrado."));

        if (usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.TESTER.getId())) {
            throw new AutorizacaoException("Usuário TESTER não pode alterar dados.");
        }

        if (!usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.ADMIN.getId())) {
            throw new AutorizacaoException("Apenas o admin pode ativar um cargo.");
        }

        cargo.setAtivo(true);
    }
}
