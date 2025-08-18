package com.api.controleverbasbackend.controller.negativo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.api.controleverbasbackend.controller.OrcamentoController;
import com.api.controleverbasbackend.domain.entity.usuario.Usuario;
import com.api.controleverbasbackend.dto.orcamento.DadosCadastroOrcamento;
import com.api.controleverbasbackend.service.OrcamentoService;
import com.api.controleverbasbackend.utils.MockUtils;

public class OrcamentoControllerTestNegativo {

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

    @Test
    void testAprovar() {
        doThrow(new RuntimeException("Erro ao aprovar")).when(orcamentoService).aprovar(1L, usuario);

        try {
            orcamentoController.aprovar(1L, usuario);
        } catch (RuntimeException ex) {
            assertThat(ex).hasMessage("Erro ao aprovar");
        }
    }

    @Test
    void testCadastrar() {
        DadosCadastroOrcamento dadosCadastro = MockUtils.criarDadosCadastro();
        when(orcamentoService.cadastrar(any(DadosCadastroOrcamento.class), eq(usuario)))
                .thenThrow(new IllegalArgumentException("Dados inválidos"));

        try {
            orcamentoController.cadastrar(dadosCadastro, null, usuario);
        } catch (IllegalArgumentException ex) {
            assertThat(ex).hasMessage("Dados inválidos");
        }
    }

    @Test
    void testLiberar() {
        doThrow(new RuntimeException("Erro ao liberar verba")).when(orcamentoService).liberarVerba(1L, usuario);

        try {
            orcamentoController.liberarVerba(1L, usuario);
        } catch (RuntimeException ex) {
            assertThat(ex).hasMessage("Erro ao liberar verba");
        }
    }

    @Test
    void testListar() {
        when(orcamentoService.listar(any(Pageable.class), eq(usuario), eq(Optional.empty())))
                .thenThrow(new RuntimeException("Falha na listagem"));

        try {
            orcamentoController.listar(Optional.empty(), PageRequest.of(0, 10), usuario);
        } catch (RuntimeException ex) {
            assertThat(ex).hasMessage("Falha na listagem");
        }
    }

    @Test
    void testReprovar() {
        doThrow(new RuntimeException("Erro ao reprovar")).when(orcamentoService).reprovar(1L, usuario);

        try {
            orcamentoController.reprovar(1L, usuario);
        } catch (RuntimeException ex) {
            assertThat(ex).hasMessage("Erro ao reprovar");
        }
    }
}
