package com.api.controleverbasbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.controleverbasbackend.dto.DadosListagemCargo;
import com.api.controleverbasbackend.repository.CargoRepository;

@Service
public class CargoService {

    @Autowired
    private CargoRepository cargoRepository;

    @Transactional
    public Page<DadosListagemCargo> listar(Pageable pageable) {
        return cargoRepository.findAll(pageable).map(DadosListagemCargo::new);
    }
}
