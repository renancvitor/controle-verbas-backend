package com.api.controleverbasbackend.controller.positivo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import com.api.controleverbasbackend.controller.CargoController;
import com.api.controleverbasbackend.domain.entity.usuario.Usuario;
import com.api.controleverbasbackend.dto.cargo.DadosAtualizacaoCargo;
import com.api.controleverbasbackend.dto.cargo.DadosCadastroCargo;
import com.api.controleverbasbackend.dto.cargo.DadosDetalhamentoCargo;
import com.api.controleverbasbackend.dto.cargo.DadosListagemCargo;
import com.api.controleverbasbackend.service.CargoService;

class CargoControllerTestPositivo {

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
        ResponseEntity<Void> response = cargoController.ativar(1L, usuario);
        assertThat(response.getStatusCodeValue()).isEqualTo(204);
        verify(cargoService).ativar(1L, usuario);
    }

    @SuppressWarnings("null")
    @Test
    void testAtualizar() {
        DadosAtualizacaoCargo dadosAtualizacao = new DadosAtualizacaoCargo("Cargo Atualizado");
        DadosDetalhamentoCargo detalhes = new DadosDetalhamentoCargo(1L, "Cargo Atualizado");

        when(cargoService.atualizar(1L, dadosAtualizacao, usuario)).thenReturn(detalhes);

        ResponseEntity<DadosDetalhamentoCargo> response = cargoController.atualizar(1L, dadosAtualizacao, usuario);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().nome()).isEqualTo("Cargo Atualizado");
    }

    @SuppressWarnings("null")
    @Test
    void testCadastrar() {
        DadosCadastroCargo dadosCadastro = new DadosCadastroCargo("Cargo Teste");
        DadosDetalhamentoCargo detalhes = new DadosDetalhamentoCargo(1L, "Cargo Teste");

        when(cargoService.cadastrar(any(DadosCadastroCargo.class), any())).thenReturn(detalhes);

        ResponseEntity<DadosDetalhamentoCargo> response = cargoController.cadastrar(dadosCadastro,
                UriComponentsBuilder.newInstance(), usuario);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody().nome()).isEqualTo("Cargo Teste");
    }

    @Test
    void testDeletar() {
        ResponseEntity<Void> response = cargoController.deletar(1L, usuario);
        assertThat(response.getStatusCodeValue()).isEqualTo(204);
        verify(cargoService).deletar(1L, usuario);
    }

    @SuppressWarnings("null")
    @Test
    void testListar() {
        DadosListagemCargo dto = new DadosListagemCargo(1L, "Cargo Teste", true);
        Page<DadosListagemCargo> page = new PageImpl<>(List.of(dto), PageRequest.of(0, 10), 1);

        when(cargoService.listar(any(), any(), any())).thenReturn(page);

        ResponseEntity<Page<DadosListagemCargo>> response = cargoController.listar(null, PageRequest.of(0, 10),
                usuario);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().getContent().get(0).nome()).isEqualTo("Cargo Teste");
    }
}
