package com.api.controleverbasbackend.service.positivos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.api.controleverbasbackend.domain.departamento.Departamento;
import com.api.controleverbasbackend.domain.usuario.TipoUsuarioEntidade;
import com.api.controleverbasbackend.domain.usuario.TipoUsuarioEnum;
import com.api.controleverbasbackend.domain.usuario.Usuario;
import com.api.controleverbasbackend.dto.departamento.DadosAtualizacaoDepartamento;
import com.api.controleverbasbackend.dto.departamento.DadosCadastroDepartamento;
import com.api.controleverbasbackend.dto.departamento.DadosDetalhamentoDepartamento;
import com.api.controleverbasbackend.infra.mensageria.kafka.LogProducer;
import com.api.controleverbasbackend.repository.DepartamentoRepository;
import com.api.controleverbasbackend.service.DepartamentoService;
import com.api.controleverbasbackend.utils.MockUtils;

@SpringBootTest
@ActiveProfiles("test")
public class DepartamentoServiceTestPositivo {

    @MockitoBean
    private LogProducer logProducer;

    @MockitoBean
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private DepartamentoService departamentoService;

    @Test
    void testAtivar() {
        Departamento departamento = MockUtils.idPadrao(new Departamento());
        departamento.setAtivo(false);

        when(departamentoRepository.findByIdAndAtivoFalse(departamento.getId()))
                .thenReturn(Optional.of(departamento));

        TipoUsuarioEntidade tipoUsuarioEntidade = new TipoUsuarioEntidade();
        tipoUsuarioEntidade.setId(TipoUsuarioEnum.ADMIN.getId());

        Usuario usuario = new Usuario();
        usuario.setTipoUsuario(tipoUsuarioEntidade);

        departamentoService.ativar(departamento.getId(), usuario);

        assertTrue(departamento.getAtivo());
    }

    @Test
    void testAtualizar() {
        String nomeAtual = "Departamento Atual";
        String novoNome = "Empresa";

        Departamento departamento = MockUtils.idPadrao(new Departamento());
        departamento.setAtivo(false);
        departamento.setNome(nomeAtual);
        departamento.setAtivo(true);

        Usuario usuario = MockUtils.criarUsuarioAdmin();

        when(departamentoRepository.findByIdAndAtivoTrue(departamento.getId()))
                .thenReturn(Optional.of(departamento));

        DadosAtualizacaoDepartamento dados = new DadosAtualizacaoDepartamento(novoNome);

        DadosDetalhamentoDepartamento resultado = departamentoService
                .atualizar(departamento.getId(), dados, usuario);

        assertEquals(novoNome, resultado.nome());
    }

    @Test
    void testCadastrar() {
        Usuario usuario = MockUtils.criarUsuarioAdmin();

        DadosCadastroDepartamento dados = new DadosCadastroDepartamento("Empresa");

        DadosDetalhamentoDepartamento resultado = departamentoService.cadastrar(dados, usuario);

        assertNotNull(resultado);
        assertEquals("Empresa", resultado.nome());

        verify(departamentoRepository, times(1)).save(any(Departamento.class));
    }

    @Test
    void testDeletar() {

    }

    @Test
    void testListar() {

    }
}
