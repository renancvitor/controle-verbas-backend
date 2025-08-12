package com.api.controleverbasbackend.service.negativos;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.api.controleverbasbackend.domain.departamento.Departamento;
import com.api.controleverbasbackend.domain.usuario.TipoUsuarioEnum;
import com.api.controleverbasbackend.domain.usuario.Usuario;
import com.api.controleverbasbackend.dto.departamento.DadosAtualizacaoDepartamento;
import com.api.controleverbasbackend.dto.departamento.DadosCadastroDepartamento;
import com.api.controleverbasbackend.infra.exception.AutorizacaoException;
import com.api.controleverbasbackend.infra.exception.ValidacaoException;
import com.api.controleverbasbackend.infra.mensageria.kafka.LogProducer;
import com.api.controleverbasbackend.repository.DepartamentoRepository;
import com.api.controleverbasbackend.service.DepartamentoService;
import com.api.controleverbasbackend.utils.MockUtils;

import jakarta.persistence.EntityNotFoundException;

@SpringBootTest
@ActiveProfiles("test")
public class DepartamentoServiceTestNegativo {

    @MockitoBean
    private LogProducer logProducer;

    @MockitoBean
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private DepartamentoService departamentoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAtivarComDepartamentoInexistente() {
        Departamento departamento = MockUtils.idPadrao(new Departamento());
        when(departamentoRepository.findByIdAndAtivoFalse(departamento.getId())).thenReturn(Optional.empty());

        Usuario usuario = MockUtils.criarUsuarioAdmin();

        assertThrows(EntityNotFoundException.class, () -> departamentoService.ativar(departamento.getId(), usuario));
    }

    @Test
    void testAtivarComUsuarioTester() {
        Departamento departamento = MockUtils.idPadrao(new Departamento());
        departamento.setAtivo(false);

        when(departamentoRepository.findByIdAndAtivoFalse(departamento.getId())).thenReturn(Optional.of(departamento));

        Usuario usuario = MockUtils.criarUsuario(TipoUsuarioEnum.TESTER);

        assertThrows(AutorizacaoException.class, () -> departamentoService.ativar(departamento.getId(), usuario));
    }

    @Nested
    class AtualizarTestes {

        @Test
        void testAtualizarDepartamentoInexistente() {
            Departamento departamento = MockUtils.idPadrao(new Departamento());
            when(departamentoRepository.findByIdAndAtivoTrue(departamento.getId())).thenReturn(Optional.empty());

            Usuario usuario = MockUtils.criarUsuarioAdmin();
            DadosAtualizacaoDepartamento dados = new DadosAtualizacaoDepartamento("Novo Nome");

            assertThrows(EntityNotFoundException.class,
                    () -> departamentoService.atualizar(departamento.getId(), dados, usuario));
        }

        @Test
        void testAtualizarComUsuarioNaoAdmin() {
            Departamento departamento = MockUtils.idPadrao(new Departamento());
            departamento.setNome("Antigo");
            departamento.setAtivo(true);

            when(departamentoRepository.findByIdAndAtivoTrue(departamento.getId()))
                    .thenReturn(Optional.of(departamento));

            Usuario usuario = MockUtils.criarUsuario(TipoUsuarioEnum.GESTOR);
            DadosAtualizacaoDepartamento dados = new DadosAtualizacaoDepartamento("Novo Nome");

            assertThrows(AutorizacaoException.class,
                    () -> departamentoService.atualizar(departamento.getId(), dados, usuario));
        }

        @Test
        void testAtualizarComNomeInvalido() {
            Departamento departamento = MockUtils.idPadrao(new Departamento());
            departamento.setNome(null);
            departamento.setAtivo(true);

            when(departamentoRepository.findByIdAndAtivoTrue(departamento.getId()))
                    .thenReturn(Optional.of(departamento));

            Usuario usuario = MockUtils.criarUsuarioAdmin();
            DadosAtualizacaoDepartamento dados = new DadosAtualizacaoDepartamento("Novo Nome");

            assertThrows(ValidacaoException.class,
                    () -> departamentoService.atualizar(departamento.getId(), dados, usuario));
        }
    }

    @Nested
    class CadastrarTestes {

        @Test
        void testCadastrarComUsuarioTester() {
            Usuario usuario = MockUtils.criarUsuario(TipoUsuarioEnum.TESTER);
            DadosCadastroDepartamento dados = new DadosCadastroDepartamento("Novo Departamento");

            assertThrows(AutorizacaoException.class, () -> departamentoService.cadastrar(dados, usuario));
        }

        @Test
        void testCadastrarComUsuarioNaoAdmin() {
            Usuario usuario = MockUtils.criarUsuario(TipoUsuarioEnum.GESTOR);
            DadosCadastroDepartamento dados = new DadosCadastroDepartamento("Novo Departamento");

            assertThrows(AutorizacaoException.class, () -> departamentoService.cadastrar(dados, usuario));
        }
    }

    @Nested
    class DeletarTestes {

        @Test
        void testDeletarDepartamentoInexistente() {
            Departamento departamento = MockUtils.idPadrao(new Departamento());
            when(departamentoRepository.findByIdAndAtivoTrue(departamento.getId())).thenReturn(Optional.empty());

            Usuario usuario = MockUtils.criarUsuarioAdmin();

            assertThrows(EntityNotFoundException.class,
                    () -> departamentoService.deletar(departamento.getId(), usuario));
        }

        @Test
        void testDeletarComUsuarioTester() {
            Departamento departamento = MockUtils.idPadrao(new Departamento());
            departamento.setAtivo(true);

            when(departamentoRepository.findByIdAndAtivoTrue(departamento.getId()))
                    .thenReturn(Optional.of(departamento));

            Usuario usuario = MockUtils.criarUsuario(TipoUsuarioEnum.TESTER);

            assertThrows(AutorizacaoException.class, () -> departamentoService.deletar(departamento.getId(), usuario));
        }
    }

    @Test
    void testListarComUsuarioNaoAutorizado() {
        Usuario usuario = MockUtils.criarUsuario(TipoUsuarioEnum.GESTOR);
        Pageable pageable = PageRequest.of(0, 10);

        assertThrows(AutorizacaoException.class, () -> departamentoService.listar(pageable, usuario, true));
    }
}
