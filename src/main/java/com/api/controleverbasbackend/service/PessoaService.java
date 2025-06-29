package com.api.controleverbasbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.controleverbasbackend.domain.pessoa.Pessoa;
import com.api.controleverbasbackend.dto.pessoa.DadosCadastroPessoa;
import com.api.controleverbasbackend.dto.pessoa.DadosDetalhamentoPessoa;
import com.api.controleverbasbackend.dto.pessoa.DadosListagemPessoa;
import com.api.controleverbasbackend.repository.PessoaRepository;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Transactional
    public Page<DadosListagemPessoa> listar(Pageable pageable) {
        return pessoaRepository.findAll(pageable).map(DadosListagemPessoa::new);
    }

    @Transactional
    public DadosDetalhamentoPessoa cadastrar(DadosCadastroPessoa dados) {
        Pessoa pessoa = new Pessoa();
        pessoaRepository.save(pessoa);
        return new DadosDetalhamentoPessoa(pessoa);
    }
}
