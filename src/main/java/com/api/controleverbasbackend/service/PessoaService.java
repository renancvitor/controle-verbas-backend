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
import com.api.controleverbasbackend.domain.usuario.Usuario;
import com.api.controleverbasbackend.dto.pessoa.DadosAtualizacaoPessoa;
import com.api.controleverbasbackend.dto.pessoa.DadosCadastroPessoa;
import com.api.controleverbasbackend.dto.pessoa.DadosDetalhamentoPessoa;
import com.api.controleverbasbackend.dto.pessoa.DadosListagemPessoa;
import com.api.controleverbasbackend.dto.usuario.DadosCadastroUsuario;
import com.api.controleverbasbackend.infra.exception.AutorizacaoException;
import com.api.controleverbasbackend.infra.exception.NaoEncontradoException;
import com.api.controleverbasbackend.infra.exception.ValidacaoException;
import com.api.controleverbasbackend.repository.CargoRepository;
import com.api.controleverbasbackend.repository.DepartamentoRepository;
import com.api.controleverbasbackend.repository.PessoaRepository;

import jakarta.persistence.EntityNotFoundException;

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
        public Page<DadosListagemPessoa> listar(Pageable pageable, Usuario usuario, Boolean ativo) {
                if (!usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.ADMIN.getId()) &&
                                !usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.TESTER.getId())) {
                        throw new AutorizacaoException("Apenas o admin pode listar pessoas cadastradas.");
                }

                if (ativo != null) {
                        return pessoaRepository.findAllByAtivo(ativo, pageable).map(DadosListagemPessoa::new);
                }
                return pessoaRepository.findAll(pageable).map(DadosListagemPessoa::new);
        }

        @Transactional
        public DadosDetalhamentoPessoa cadastrar(DadosCadastroPessoa dadosPessoa, DadosCadastroUsuario dadosUsuario,
                        Usuario usuario) {
                if (usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.TESTER.getId())) {
                        throw new AutorizacaoException("Usuário TESTER não pode alterar dados.");
                }

                Departamento departamento = departamentoRepository.findByIdAndAtivoTrue(dadosPessoa.idDepartamento())
                                .orElseThrow(() -> new NaoEncontradoException("Departamento não existe."));
                Cargo cargo = cargoRepository.findByIdAndAtivoTrue(dadosPessoa.idCargo())
                                .orElseThrow(() -> new NaoEncontradoException("Cargo não existe."));

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

        @Transactional
        public DadosDetalhamentoPessoa atualizar(Long id, DadosAtualizacaoPessoa dados, Usuario usuario) {
                Pessoa pessoa = pessoaRepository.findByIdAndAtivoTrue(id)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Pessoa com ID " + id + " náo encontrado."));

                if (usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.TESTER.getId())) {
                        throw new AutorizacaoException("Usuário TESTER não pode alterar dados.");
                }

                if (!usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.ADMIN.getId())) {
                        throw new AutorizacaoException("Apenas o admin pode atualizar pessoas.");
                }
                if (pessoa.getCargo().getId() == null || pessoa.getDepartamento().getId() == null) {
                        throw new ValidacaoException("O nome deve ser preenchido corretamente.");
                }

                pessoa.atualizar(dados, cargoRepository, departamentoRepository);
                return new DadosDetalhamentoPessoa(pessoa);
        }

        @Transactional
        public void deletar(Long id, Usuario usuario) {
                Pessoa pessoa = pessoaRepository.findByIdAndAtivoTrue(id)
                                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada."));

                if (usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.TESTER.getId())) {
                        throw new AutorizacaoException("Usuário TESTER não pode alterar dados.");
                }

                if (!usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.ADMIN.getId())) {
                        throw new AutorizacaoException("Apenas o admin pode deletar uma pessoa.");
                }

                pessoa.setAtivo(false);
        }

        @Transactional
        public void ativar(Long id, Usuario usuario) {
                Pessoa pessoa = pessoaRepository.findByIdAndAtivoFalse(id)
                                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada."));

                if (usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.TESTER.getId())) {
                        throw new AutorizacaoException("Usuário TESTER não pode alterar dados.");
                }

                if (!usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.ADMIN.getId())) {
                        throw new AutorizacaoException("Apenas o admin pode ativar uma pessoa.");
                }

                pessoa.setAtivo(true);
        }
}