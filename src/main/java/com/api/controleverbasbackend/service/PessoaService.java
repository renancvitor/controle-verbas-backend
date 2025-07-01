package com.api.controleverbasbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.controleverbasbackend.domain.cargo.Cargo;
import com.api.controleverbasbackend.domain.departamento.Departamento;
import com.api.controleverbasbackend.domain.pessoa.Pessoa;
import com.api.controleverbasbackend.domain.usuario.TipoUsuarioEnum;
import com.api.controleverbasbackend.dto.pessoa.DadosCadastroPessoa;
import com.api.controleverbasbackend.dto.pessoa.DadosDetalhamentoPessoa;
import com.api.controleverbasbackend.dto.pessoa.DadosListagemPessoa;
import com.api.controleverbasbackend.dto.usuario.DadosCadastroUsuario;
import com.api.controleverbasbackend.infra.exception.NaoEncontradoException;
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

    @Autowired
    private UsuarioService usuarioService;

    @Transactional
    public Page<DadosListagemPessoa> listar(Pageable pageable) {
        return pessoaRepository.findAll(pageable).map(DadosListagemPessoa::new);
    }

    @Transactional
    public DadosDetalhamentoPessoa cadastrar(DadosCadastroPessoa dadosPessoa, DadosCadastroUsuario dadosUsuario) {
        Departamento departamento = departamentoRepository.findById(dadosPessoa.idDepartamento())
                .orElseThrow(() -> new NaoEncontradoException("Departamento não existe"));
        Cargo cargo = cargoRepository.findById(dadosPessoa.idCargo())
                .orElseThrow(() -> new NaoEncontradoException("Cargo não existe"));

        Pessoa pessoa = new Pessoa(
                dadosPessoa.nome(),
                dadosPessoa.cpf(),
                dadosPessoa.email(),
                departamento,
                cargo);

        pessoaRepository.save(pessoa);

        usuarioService.cadastrar(pessoa, new DadosCadastroUsuario(
                pessoa.getCpf(),
                dadosUsuario.senha(),
                TipoUsuarioEnum.COMUM.getId()));

        return new DadosDetalhamentoPessoa(pessoa);
    }
}
