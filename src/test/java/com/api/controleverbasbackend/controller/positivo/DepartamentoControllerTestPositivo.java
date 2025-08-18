package com.api.controleverbasbackend.controller.positivo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.api.controleverbasbackend.controller.DepartamentoController;
import com.api.controleverbasbackend.domain.entity.usuario.Usuario;
import com.api.controleverbasbackend.dto.departamento.DadosAtualizacaoDepartamento;
import com.api.controleverbasbackend.dto.departamento.DadosCadastroDepartamento;
import com.api.controleverbasbackend.dto.departamento.DadosDetalhamentoDepartamento;
import com.api.controleverbasbackend.dto.departamento.DadosListagemDepartamento;
import com.api.controleverbasbackend.service.DepartamentoService;

public class DepartamentoControllerTestPositivo {

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
        ResponseEntity<Void> response = departamentoController.ativar(1L, usuario);
        assertThat(response.getStatusCodeValue()).isEqualTo(204);
        verify(departamentoService).ativar(1L, usuario);
    }

    @SuppressWarnings("null")
    @Test
    void testAtualizar() {
        DadosAtualizacaoDepartamento dadosAtualizacao = new DadosAtualizacaoDepartamento("Departamento Atualizado");
        DadosDetalhamentoDepartamento detalhes = new DadosDetalhamentoDepartamento(1L, "Departamento Atualizado");

        when(departamentoService.atualizar(1L, dadosAtualizacao, usuario)).thenReturn(detalhes);

        ResponseEntity<DadosDetalhamentoDepartamento> response = departamentoController.atualizar(1L, dadosAtualizacao,
                usuario);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().nome()).isEqualTo("Departamento Atualizado");
    }

    @SuppressWarnings("null")
    @Test
    void testCadastrar() {
        DadosCadastroDepartamento dadosCadastro = new DadosCadastroDepartamento("Departamento Teste");
        DadosDetalhamentoDepartamento detalhes = new DadosDetalhamentoDepartamento(1L, "Departamento Teste");

        when(departamentoService.cadastrar(any(DadosCadastroDepartamento.class), any())).thenReturn(detalhes);

        ResponseEntity<DadosDetalhamentoDepartamento> response = departamentoController.cadastrar(dadosCadastro,
                UriComponentsBuilder.newInstance(), usuario);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody().nome()).isEqualTo("Departamento Teste");
    }

    @Test
    void testDeletar() {
        ResponseEntity<Void> response = departamentoController.deletar(1L, usuario);
        assertThat(response.getStatusCodeValue()).isEqualTo(204);
        verify(departamentoService).deletar(1L, usuario);
    }

    @SuppressWarnings("null")
    @Test
    void testListar() {
        DadosListagemDepartamento dto = new DadosListagemDepartamento(1L, "Departamento Teste", true);
        Page<DadosListagemDepartamento> page = new PageImpl<>(List.of(dto), PageRequest
                .of(0, 10), 1);

        when(departamentoService.listar(any(), any(), any())).thenReturn(page);

        ResponseEntity<Page<DadosListagemDepartamento>> response = departamentoController.listar(null,
                PageRequest.of(0, 10),
                usuario);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().getContent().get(0).nome()).isEqualTo("Departamento Teste");
    }
}
