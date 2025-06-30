package com.api.controleverbasbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.controleverbasbackend.domain.cargo.Cargo;
import com.api.controleverbasbackend.domain.departamento.Departamento;
import com.api.controleverbasbackend.domain.pessoa.Pessoa;
import com.api.controleverbasbackend.dto.pessoa.DadosCadastroPessoa;
import com.api.controleverbasbackend.dto.pessoa.DadosDetalhamentoPessoa;
import com.api.controleverbasbackend.dto.pessoa.DadosListagemPessoa;
import com.api.controleverbasbackend.repository.CargoRepository;
import com.api.controleverbasbackend.repository.DepartamentoRepository;
import com.api.controleverbasbackend.repository.PessoaRepository;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private CargoRepository cargoRepository;

    @Transactional
    public Page<DadosListagemPessoa> listar(Pageable pageable) {
        return pessoaRepository.findAll(pageable).map(DadosListagemPessoa::new);
    }

    @Transactional
    public DadosDetalhamentoPessoa cadastrar(DadosCadastroPessoa dados) {
        Departamento departamento = departamentoRepository.findById(dados.idDepartamento())
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado"));
        Cargo cargo = cargoRepository.findById(dados.idCargo())
                .orElseThrow(() -> new RuntimeException("Cargo não encontrado"));

        Pessoa pessoa = new Pessoa(
                dados.nome(),
                dados.cpf(),
                dados.email(),
                departamento,
                cargo);

        pessoaRepository.save(pessoa);
        return new DadosDetalhamentoPessoa(pessoa);
    }
}
