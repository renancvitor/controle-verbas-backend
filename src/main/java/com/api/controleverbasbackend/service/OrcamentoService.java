package com.api.controleverbasbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.controleverbasbackend.repository.OrcamentoRepository;

@Service
public class OrcamentoService {

    @Autowired
    OrcamentoRepository orcamentoRepository;

    @Autowired
    StatusOrcamentoRepository statusOrcamentoRepository;

}
