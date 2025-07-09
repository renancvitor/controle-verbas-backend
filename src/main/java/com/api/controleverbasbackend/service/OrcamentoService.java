package com.api.controleverbasbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.controleverbasbackend.domain.orcamento.StatusOrcamentoEnum;
import com.api.controleverbasbackend.domain.usuario.TipoUsuarioEnum;
import com.api.controleverbasbackend.domain.usuario.Usuario;
import com.api.controleverbasbackend.dto.orcamento.DadosListagemOrcamento;
import com.api.controleverbasbackend.repository.OrcamentoRepository;
import com.api.controleverbasbackend.repository.StatusOrcamentoRepository;

@Service
public class OrcamentoService {

    @Autowired
    OrcamentoRepository orcamentoRepository;

    @Autowired
    StatusOrcamentoRepository statusOrcamentoRepository;

    @Transactional
    public Page<DadosListagemOrcamento> listar(Pageable pageable, Usuario usuarioLogado) {
        Integer idTipoUsuario = usuarioLogado.getTipoUsuario().getId();
        Long idUsuario = usuarioLogado.getId();

        Integer ENVIADO = StatusOrcamentoEnum.ENVIADO.getId();
        Integer APROVADO = StatusOrcamentoEnum.APROVADO.getId();
        Integer REPROVADO = StatusOrcamentoEnum.REPROVADO.getId();

        if (idTipoUsuario.equals(TipoUsuarioEnum.ADMIN.getId())
                || idTipoUsuario.equals(TipoUsuarioEnum.GESTOR.getId())) {
            return orcamentoRepository.findAll(pageable).map(DadosListagemOrcamento::new);

        } else if (idTipoUsuario.equals(TipoUsuarioEnum.TESOUREIRO.getId())) {
            return orcamentoRepository.findAprovadosOuPropriosNaoAprovados(
                    idUsuario, APROVADO, ENVIADO, REPROVADO, pageable).map(DadosListagemOrcamento::new);

        } else if (idTipoUsuario.equals(TipoUsuarioEnum.COMUM.getId())) {
            return orcamentoRepository.findBySolicitanteId(idUsuario, pageable)
                    .map(DadosListagemOrcamento::new);
        }

        return Page.empty();
    }

}
