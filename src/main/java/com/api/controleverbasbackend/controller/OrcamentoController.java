package com.api.controleverbasbackend.controller;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.api.controleverbasbackend.domain.entity.usuario.Usuario;
import com.api.controleverbasbackend.domain.enums.sistemalog.TipoLog;
import com.api.controleverbasbackend.dto.orcamento.DadosCadastroOrcamento;
import com.api.controleverbasbackend.dto.orcamento.DadosDetalhamentoOrcamento;
import com.api.controleverbasbackend.dto.orcamento.DadosListagemOrcamento;
import com.api.controleverbasbackend.infra.messaging.log.Loggable;
import com.api.controleverbasbackend.infra.messaging.log.Loggables;
import com.api.controleverbasbackend.service.OrcamentoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/orcamentos")
public class OrcamentoController {

        @Autowired
        private OrcamentoService orcamentoService;

        @GetMapping
        public ResponseEntity<Page<DadosListagemOrcamento>> listar(Optional<Integer> statusId,
                        @PageableDefault(size = 5000, sort = ("id")) Pageable pageable,
                        @AuthenticationPrincipal Usuario usuarioLogado) {

                Page<DadosListagemOrcamento> page = orcamentoService.listar(pageable, usuarioLogado, statusId);
                return ResponseEntity.ok(page);
        }

        @PostMapping
        @Loggables({
                        @Loggable(tipo = TipoLog.INSERT, entidade = "Orcamento"),
                        @Loggable(tipo = TipoLog.POST_UPDATE, entidade = "Orcamento")
        })
        public ResponseEntity<DadosDetalhamentoOrcamento> cadastrar(@RequestBody @Valid DadosCadastroOrcamento dados,
                        UriComponentsBuilder uriComponentsBuilder, @AuthenticationPrincipal Usuario usuario) {

                DadosDetalhamentoOrcamento orcamento = orcamentoService.cadastrar(dados, usuario);
                URI uri = uriComponentsBuilder.path("/orcamentos/{id}")
                                .buildAndExpand(orcamento.id())
                                .toUri();

                return ResponseEntity.created(uri).body(orcamento);
        }

        @PutMapping("/{id}/aprovar")
        @Loggables({
                        @Loggable(tipo = TipoLog.PRE_UPDATE, entidade = "Orcamento"),
                        @Loggable(tipo = TipoLog.POST_UPDATE, entidade = "Orcamento")
        })
        public ResponseEntity<DadosDetalhamentoOrcamento> aprovar(@PathVariable Long id,
                        @AuthenticationPrincipal Usuario usuario) {

                DadosDetalhamentoOrcamento dados = orcamentoService.aprovar(id, usuario);
                return ResponseEntity.ok(dados);
        }

        @PutMapping("/{id}/reprovar")
        @Loggables({
                        @Loggable(tipo = TipoLog.PRE_UPDATE, entidade = "Orcamento"),
                        @Loggable(tipo = TipoLog.POST_UPDATE, entidade = "Orcamento")
        })
        public ResponseEntity<DadosDetalhamentoOrcamento> reprovar(@PathVariable Long id,
                        @AuthenticationPrincipal Usuario usuario) {

                DadosDetalhamentoOrcamento dados = orcamentoService.reprovar(id, usuario);
                return ResponseEntity.ok(dados);
        }

        @PutMapping("/{id}/liberar_verba")
        @Loggables({
                        @Loggable(tipo = TipoLog.PRE_UPDATE, entidade = "Orcamento"),
                        @Loggable(tipo = TipoLog.POST_UPDATE, entidade = "Orcamento")
        })
        public ResponseEntity<DadosDetalhamentoOrcamento> liberarVerba(@PathVariable Long id,
                        @AuthenticationPrincipal Usuario usuario) {
                DadosDetalhamentoOrcamento dados = orcamentoService.liberarVerba(id, usuario);
                return ResponseEntity.ok(dados);
        }
}
