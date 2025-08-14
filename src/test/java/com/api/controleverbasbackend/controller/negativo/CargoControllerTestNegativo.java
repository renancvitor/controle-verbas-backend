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

import com.api.controleverbasbackend.controller.CargoController;
import com.api.controleverbasbackend.domain.usuario.Usuario;
import com.api.controleverbasbackend.dto.cargo.DadosAtualizacaoCargo;
import com.api.controleverbasbackend.dto.cargo.DadosCadastroCargo;
import com.api.controleverbasbackend.service.CargoService;

class CargoControllerTestNegativo {

    @Mock
    private CargoService cargoService;

    @InjectMocks
    private CargoController cargoController;

    private Usuario usuario;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        usuario = new Usuario();
    }

    @Test
    void testAtivar() {
        doThrow(new RuntimeException("Erro ao ativar")).when(cargoService).ativar(1L, usuario);

        try {
            cargoController.ativar(1L, usuario);
        } catch (RuntimeException ex) {
            assertThat(ex.getMessage()).isEqualTo("Erro ao ativar");
        }
    }

    @Test
    void testAtualizar() {
        DadosAtualizacaoCargo dadosAtualizacao = new DadosAtualizacaoCargo("Cargo Atualizado");

        when(cargoService.atualizar(any(), any(), any()))
                .thenThrow(new NoSuchElementException("Cargo não encontrado"));

        try {
            cargoController.atualizar(99L, dadosAtualizacao, usuario);
        } catch (NoSuchElementException ex) {
            assertThat(ex.getMessage()).isEqualTo("Cargo não encontrado");
        }
    }

    @Test
    void testCadastrar() {
        DadosCadastroCargo dadosCadastro = new DadosCadastroCargo("Cargo Teste");

        when(cargoService.cadastrar(any(DadosCadastroCargo.class), any()))
                .thenThrow(new RuntimeException("Falha ao cadastrar"));

        try {
            cargoController.cadastrar(dadosCadastro, UriComponentsBuilder.newInstance(), usuario);
        } catch (RuntimeException ex) {
            assertThat(ex.getMessage()).isEqualTo("Falha ao cadastrar");
        }
    }

    @Test
    void testDeletar() {
        doThrow(new RuntimeException("Erro ao deletar")).when(cargoService).deletar(1L, usuario);

        try {
            cargoController.deletar(1L, usuario);
        } catch (RuntimeException ex) {
            assertThat(ex.getMessage()).isEqualTo("Erro ao deletar");
        }
    }

    @Test
    void testListar() {
        when(cargoService.listar(any(), any(), any()))
                .thenThrow(new RuntimeException("Erro interno"));

        try {
            cargoController.listar(null, PageRequest.of(0, 10), usuario);
        } catch (RuntimeException ex) {
            assertThat(ex.getMessage()).isEqualTo("Erro interno");
        }
    }
}