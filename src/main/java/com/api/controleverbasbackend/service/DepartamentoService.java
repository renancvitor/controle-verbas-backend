package com.api.controleverbasbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.controleverbasbackend.domain.departamento.Departamento;
import com.api.controleverbasbackend.dto.departamento.DadosDetalhamentoDepartamento;
import com.api.controleverbasbackend.dto.departamento.DadosListagemDepartamento;
import com.api.controleverbasbackend.repository.DepartamentoRepository;

@Service
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Transactional
    public Page<DadosListagemDepartamento> listar(Pageable pageable) {
        return departamentoRepository.findAll(pageable).map(DadosListagemDepartamento::new);
    }

    @Transactional
    public DadosDetalhamentoDepartamento cadastrar(DadosDetalhamentoDepartamento dados) {
        Departamento departamento = new Departamento();
        return new DadosDetalhamentoDepartamento(departamento);
    }
}
