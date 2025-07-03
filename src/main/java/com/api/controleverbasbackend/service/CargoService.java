package com.api.controleverbasbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.controleverbasbackend.domain.cargo.Cargo;
import com.api.controleverbasbackend.domain.usuario.TipoUsuarioEnum;
import com.api.controleverbasbackend.domain.usuario.Usuario;
import com.api.controleverbasbackend.dto.cargo.DadosCadastroCargo;
import com.api.controleverbasbackend.dto.cargo.DadosDetalhamentoCargo;
import com.api.controleverbasbackend.dto.cargo.DadosListagemCargo;
import com.api.controleverbasbackend.infra.exception.AutorizacaoException;
import com.api.controleverbasbackend.repository.CargoRepository;

@Service
public class CargoService {

    @Autowired
    private CargoRepository cargoRepository;

    @Autowired
    private Usuario usuario;

    @Transactional
    public Page<DadosListagemCargo> listar(Pageable pageable) {
        return cargoRepository.findAll(pageable).map(DadosListagemCargo::new);
    }

    @Transactional
    public DadosDetalhamentoCargo cadastrar(DadosCadastroCargo dados, Usuario usuario) {
        if (!usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.ADMIN.getId())) {
            throw new AutorizacaoException("Apenas o admin pode cadastrar novos cargos");
        }

        Cargo cargo = new Cargo(dados);
        cargoRepository.save(cargo);
        return new DadosDetalhamentoCargo(cargo);
    }
}
