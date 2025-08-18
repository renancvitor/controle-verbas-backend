package com.api.controleverbasbackend.controller.positivo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import com.api.controleverbasbackend.controller.OrcamentoController;
import com.api.controleverbasbackend.domain.entity.usuario.Usuario;
import com.api.controleverbasbackend.dto.orcamento.DadosCadastroOrcamento;
import com.api.controleverbasbackend.dto.orcamento.DadosDetalhamentoOrcamento;
import com.api.controleverbasbackend.dto.orcamento.DadosListagemOrcamento;
import com.api.controleverbasbackend.service.OrcamentoService;
import com.api.controleverbasbackend.utils.MockUtils;

public class OrcamentoControllerTestPositivo {

        @InjectMocks
        private OrcamentoController orcamentoController;

        @org.mockito.Mock
        private OrcamentoService orcamentoService;

        private Usuario usuario;

        @BeforeEach
        void setup() {
                MockitoAnnotations.openMocks(this);
                usuario = new Usuario();
        }

        @SuppressWarnings("null")
        @Test
        void testAprovar() {
                DadosDetalhamentoOrcamento detalhes = MockUtils.criarDetalhes("ENVIADO");
                when(orcamentoService.aprovar(1L, usuario)).thenReturn(detalhes);

                ResponseEntity<DadosDetalhamentoOrcamento> response = orcamentoController.aprovar(1L, usuario);

                assertThat(response.getStatusCodeValue()).isEqualTo(200);
                assertThat(response.getBody().gestorNome()).isEqualTo("Bruno");
        }

        @SuppressWarnings("null")
        @Test
        void testCadastrar() {
                DadosCadastroOrcamento dadosCadastro = MockUtils.criarDadosCadastro();

                DadosDetalhamentoOrcamento detalhes = MockUtils.criarDetalhes("ENVIADO");

                when(orcamentoService.cadastrar(eq(dadosCadastro), eq(usuario)))
                                .thenReturn(detalhes);

                UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("http://localhost");

                ResponseEntity<DadosDetalhamentoOrcamento> response = orcamentoController.cadastrar(dadosCadastro,
                                uriBuilder,
                                usuario);

                assertThat(response.getStatusCodeValue()).isEqualTo(201);
                assertThat(response.getHeaders().getLocation())
                                .isEqualTo(URI.create("http://localhost/orcamentos/" + 1L));
                assertThat(response.getBody().id()).isEqualTo(1L);
                assertThat(response.getBody().solicitanteNome()).isEqualTo("Alice");
        }

        @SuppressWarnings("null")
        @Test
        void testLiberar() {
                DadosDetalhamentoOrcamento detalhes = MockUtils.criarDetalhes("APROVADO");
                when(orcamentoService.liberarVerba(1L, usuario)).thenReturn(detalhes);

                ResponseEntity<DadosDetalhamentoOrcamento> response = orcamentoController.liberarVerba(1L, usuario);

                assertThat(response.getStatusCodeValue()).isEqualTo(200);
                assertThat(response.getBody().gestorNome()).isEqualTo("Bruno");
        }

        @SuppressWarnings("null")
        @Test
        void testListar() {
                DadosListagemOrcamento dto = MockUtils.criarListagem();
                Page<DadosListagemOrcamento> page = new PageImpl<>(List.of(dto), PageRequest.of(0, 10), 1);

                when(orcamentoService.listar(any(Pageable.class), eq(usuario), eq(Optional.empty())))
                                .thenReturn(page);

                ResponseEntity<Page<DadosListagemOrcamento>> response = orcamentoController.listar(Optional.empty(),
                                PageRequest.of(0, 10), usuario);

                assertThat(response.getStatusCodeValue()).isEqualTo(200);
                assertThat(response.getBody().getContent()).hasSize(1);
                assertThat(response.getBody().getContent().get(0).tesoureiroNome()).isEqualTo("Carla");
        }

        @SuppressWarnings("null")
        @Test
        void testReprovar() {
                DadosDetalhamentoOrcamento detalhes = MockUtils.criarDetalhes("APROVADO");
                when(orcamentoService.reprovar(1L, usuario)).thenReturn(detalhes);

                ResponseEntity<DadosDetalhamentoOrcamento> response = orcamentoController.reprovar(1L, usuario);

                assertThat(response.getStatusCodeValue()).isEqualTo(200);
                assertThat(response.getBody().gestorNome()).isEqualTo("Bruno");
        }
}
