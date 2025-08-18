package com.api.controleverbasbackend.controller.negativo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.util.UriComponentsBuilder;

import com.api.controleverbasbackend.controller.DepartamentoController;
import com.api.controleverbasbackend.domain.entity.usuario.Usuario;
import com.api.controleverbasbackend.dto.departamento.DadosAtualizacaoDepartamento;
import com.api.controleverbasbackend.dto.departamento.DadosCadastroDepartamento;
import com.api.controleverbasbackend.service.DepartamentoService;

public class DepartamentoControllerTestNegativo {

    @Mock
    private DepartamentoService departamentoService;

    @InjectMocks
    private DepartamentoController departamentoController;

    private Usuario usuario;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        usuario = new Usuario();
    }

    @Test
    void testAtivar() {
        doThrow(new RuntimeException("Erro ao ativar")).when(departamentoService).ativar(1L, usuario);

        try {
            departamentoController.ativar(1L, usuario);
        } catch (RuntimeException ex) {
            assertThat(ex.getMessage()).isEqualTo("Erro ao ativar");
        }
    }

    @Test
    void testAtualizar() {
        DadosAtualizacaoDepartamento dadosAtualizacao = new DadosAtualizacaoDepartamento("Departamento Atualizado");

        when(departamentoService.atualizar(any(), any(), any()))
                .thenThrow(new NoSuchElementException("Departamento não encontrado"));

        try {
            departamentoController.atualizar(99L, dadosAtualizacao, usuario);
        } catch (NoSuchElementException ex) {
            assertThat(ex.getMessage()).isEqualTo("Departamento não encontrado");
        }
    }

    @Test
    void testCadastrar() {
        DadosCadastroDepartamento dadosCadastro = new DadosCadastroDepartamento("Departamento Teste");

        when(departamentoService.cadastrar(any(DadosCadastroDepartamento.class), any()))
                .thenThrow(new RuntimeException("Falha ao cadastrar"));

        try {
            departamentoController.cadastrar(dadosCadastro, UriComponentsBuilder.newInstance(), usuario);
        } catch (RuntimeException ex) {
            assertThat(ex.getMessage()).isEqualTo("Falha ao cadastrar");
        }
    }

    @Test
    void testDeletar() {
        doThrow(new RuntimeException("Erro ao deletar")).when(departamentoService).deletar(1L, usuario);

        try {
            departamentoController.deletar(1L, usuario);
        } catch (RuntimeException ex) {
            assertThat(ex.getMessage()).isEqualTo("Erro ao deletar");
        }
    }

    @Test
    void testListar() {
        when(departamentoService.listar(any(), any(), any()))
                .thenThrow(new RuntimeException("Erro interno"));

        try {
            departamentoController.listar(null, PageRequest.of(0, 10), usuario);
        } catch (RuntimeException ex) {
            assertThat(ex.getMessage()).isEqualTo("Erro interno");
        }
    }
}
