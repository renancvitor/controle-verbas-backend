package com.api.controleverbasbackend.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.controleverbasbackend.domain.orcamento.Orcamento;
import com.api.controleverbasbackend.domain.orcamento.StatusOrcamentoEntidade;
import com.api.controleverbasbackend.domain.orcamento.StatusOrcamentoEnum;
import com.api.controleverbasbackend.domain.usuario.TipoUsuarioEnum;
import com.api.controleverbasbackend.domain.usuario.Usuario;
import com.api.controleverbasbackend.dto.orcamento.DadosCadastroOrcamento;
import com.api.controleverbasbackend.dto.orcamento.DadosDetalhamentoOrcamento;
import com.api.controleverbasbackend.dto.orcamento.DadosListagemOrcamento;
import com.api.controleverbasbackend.infra.exception.AutorizacaoException;
import com.api.controleverbasbackend.infra.exception.ValidacaoException;
import com.api.controleverbasbackend.repository.OrcamentoRepository;
import com.api.controleverbasbackend.repository.StatusOrcamentoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class OrcamentoService {

    @Autowired
    OrcamentoRepository orcamentoRepository;

    @Autowired
    StatusOrcamentoRepository statusOrcamentoRepository;

    @Transactional
    public Page<DadosListagemOrcamento> listar(Pageable pageable, Usuario usuarioLogado, Optional<Integer> statusId) {
        Integer idTipoUsuario = usuarioLogado.getTipoUsuario().getId();
        Long idUsuario = usuarioLogado.getId();

        Integer ENVIADO = StatusOrcamentoEnum.ENVIADO.getId();
        Integer APROVADO = StatusOrcamentoEnum.APROVADO.getId();
        Integer REPROVADO = StatusOrcamentoEnum.REPROVADO.getId();

        if (idTipoUsuario.equals(TipoUsuarioEnum.ADMIN.getId())
                || idTipoUsuario.equals(TipoUsuarioEnum.GESTOR.getId())) {
            if (statusId.isPresent()) {
                return orcamentoRepository.findByStatusOrcamentoEntidadeId(statusId.get(), pageable)
                        .map(DadosListagemOrcamento::new);
            }
            return orcamentoRepository.findAll(pageable).map(DadosListagemOrcamento::new);

        } else if (idTipoUsuario.equals(TipoUsuarioEnum.TESOUREIRO.getId())) {
            if (statusId.isPresent()) {
                return orcamentoRepository.findAprovadosOuPropriosFiltrandoStatus(
                        idUsuario,
                        statusId.get(),
                        APROVADO,
                        ENVIADO,
                        REPROVADO,
                        pageable).map(DadosListagemOrcamento::new);
            }
            return orcamentoRepository.findAprovadosOuPropriosNaoAprovados(
                    idUsuario, APROVADO, ENVIADO, REPROVADO, pageable).map(DadosListagemOrcamento::new);

        } else if (idTipoUsuario.equals(TipoUsuarioEnum.COMUM.getId())) {
            if (statusId.isPresent()) {
                return orcamentoRepository.findBySolicitanteIdAndStatusOrcamentoEntidadeId(
                        idUsuario,
                        statusId.get(),
                        pageable).map(DadosListagemOrcamento::new);
            }
            return orcamentoRepository.findBySolicitanteId(idUsuario, pageable)
                    .map(DadosListagemOrcamento::new);
        }

        return Page.empty();
    }

    @Transactional
    public DadosDetalhamentoOrcamento cadastrar(DadosCadastroOrcamento dados, Usuario usuario) {
        Orcamento orcamento = new Orcamento(dados);

        orcamento.setSolicitante(usuario);

        StatusOrcamentoEntidade status = statusOrcamentoRepository
                .findById(StatusOrcamentoEnum.ENVIADO.getId())
                .orElseThrow(() -> new ValidacaoException("Status inicial não encontrado."));

        orcamento.setStatusOrcamentoEntidade(status);

        orcamentoRepository.save(orcamento);
        return new DadosDetalhamentoOrcamento(orcamento);
    }

    @Transactional
    public DadosDetalhamentoOrcamento aprovar(Long id, Usuario usuario) {
        if (!usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.GESTOR.getId())) {
            throw new AutorizacaoException("Apenas o gestor pode aprovar orçamentos.");
        }

        Orcamento orcamento = orcamentoRepository.getReferenceById(id);

        StatusOrcamentoEntidade status = statusOrcamentoRepository.findById(StatusOrcamentoEnum.APROVADO.getId())
                .orElseThrow(() -> new ValidacaoException("Status APROVADO não encontrado."));
        orcamento.setStatusOrcamentoEntidade(status);
        orcamento.setDataAnalise(LocalDate.now());
        orcamento.setGestor(usuario);

        orcamentoRepository.save(orcamento);
        return new DadosDetalhamentoOrcamento(orcamento);
    }

    @Transactional
    public DadosDetalhamentoOrcamento reprovar(Long id, Usuario usuario) {
        if (!usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.GESTOR.getId())) {
            throw new AutorizacaoException("Apenas o gestor pode reprovar orçamentos.");
        }

        Orcamento orcamento = orcamentoRepository.getReferenceById(id);

        StatusOrcamentoEntidade status = statusOrcamentoRepository.findById(StatusOrcamentoEnum.REPROVADO.getId())
                .orElseThrow(() -> new ValidacaoException("Status REPROVADO não encontrado."));
        orcamento.setStatusOrcamentoEntidade(status);
        orcamento.setDataAnalise(LocalDate.now());
        orcamento.setGestor(usuario);

        orcamentoRepository.save(orcamento);
        return new DadosDetalhamentoOrcamento(orcamento);
    }

    @Transactional
    public DadosDetalhamentoOrcamento liberarVerba(Long id, Usuario usuario) {
        Orcamento orcamento = orcamentoRepository.findByIdAndVerbaLiberadaFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Orçamento não encontrado."));

        if (!usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.GESTOR.getId())
                && !usuario.getTipoUsuario().getId().equals(TipoUsuarioEnum.TESOUREIRO.getId())) {
            throw new AutorizacaoException("Apenas o gestor ou o tesoureiro podem liberar verbas.");
        }

        if (!orcamento.getStatusOrcamentoEntidade().getId().equals(StatusOrcamentoEnum.APROVADO.getId())) {
            throw new ValidacaoException("Apenas orçamentos com status APROVADO podem ter verba liberada.");
        }

        orcamento.setVerbaLiberada(true);
        orcamento.setTesoureiro(usuario);
        return new DadosDetalhamentoOrcamento(orcamento);
    }
}
