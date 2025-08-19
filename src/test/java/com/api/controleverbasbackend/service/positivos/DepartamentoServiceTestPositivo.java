package com.api.controleverbasbackend.service.positivos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.api.controleverbasbackend.domain.entity.departamento.Departamento;
import com.api.controleverbasbackend.domain.entity.usuario.TipoUsuarioEntidade;
import com.api.controleverbasbackend.domain.entity.usuario.Usuario;
import com.api.controleverbasbackend.domain.enums.usuario.TipoUsuarioEnum;
import com.api.controleverbasbackend.dto.departamento.DadosAtualizacaoDepartamento;
import com.api.controleverbasbackend.dto.departamento.DadosCadastroDepartamento;
import com.api.controleverbasbackend.dto.departamento.DadosDetalhamentoDepartamento;
import com.api.controleverbasbackend.dto.departamento.DadosListagemDepartamento;
import com.api.controleverbasbackend.infra.messaging.LogProducer;
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
        Departamento departamento = MockUtils.idPadrao(new Departamento());
        departamento.setAtivo(true);

        when(departamentoRepository.findByIdAndAtivoTrue(departamento.getId()))
                .thenReturn(Optional.of(departamento));

        Usuario usuario = MockUtils.criarUsuarioAdmin();

        departamentoService.deletar(departamento.getId(), usuario);

        assertFalse(departamento.getAtivo());
    }

    @Test
    void testListar() {
        Usuario usuario = MockUtils.criarUsuarioAdmin();

        Departamento departamento1 = new Departamento();
        departamento1.setNome("RH");

        Departamento departamento2 = new Departamento();
        departamento2.setNome("Faturamento");

        Pageable pageable = PageRequest.of(0, 10);
        List<Departamento> departamentosAtivos = List.of(departamento1, departamento2);

        when(departamentoRepository.findAllByAtivo(true, pageable))
                .thenReturn(new PageImpl<>(departamentosAtivos));

        Page<DadosListagemDepartamento> resultado = departamentoService.listar(pageable, usuario, true);

        assertEquals(2, resultado.getTotalElements());
        assertEquals("Faturamento", resultado.getContent().get(1).nome());
    }
}
