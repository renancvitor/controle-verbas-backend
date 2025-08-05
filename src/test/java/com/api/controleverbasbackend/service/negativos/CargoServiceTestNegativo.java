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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import com.api.controleverbasbackend.domain.cargo.Cargo;
import com.api.controleverbasbackend.domain.usuario.TipoUsuarioEnum;
import com.api.controleverbasbackend.domain.usuario.Usuario;
import com.api.controleverbasbackend.dto.cargo.DadosAtualizacaoCargo;
import com.api.controleverbasbackend.dto.cargo.DadosCadastroCargo;
import com.api.controleverbasbackend.infra.exception.AutorizacaoException;
import com.api.controleverbasbackend.infra.exception.ValidacaoException;
import com.api.controleverbasbackend.infra.mensageria.kafka.LogProducer;
import com.api.controleverbasbackend.repository.CargoRepository;
import com.api.controleverbasbackend.service.CargoService;
import com.api.controleverbasbackend.utils.MockUtils;

import jakarta.persistence.EntityNotFoundException;

@SpringBootTest
@ActiveProfiles("test")
public class CargoServiceTestNegativo {

    @MockBean
    private LogProducer logProducer;

    @MockBean
    private CargoRepository cargoRepository;

    @Autowired
    private CargoService cargoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAtivarComCargoInexistente() {
        when(cargoRepository.findByIdAndAtivoFalse(1L)).thenReturn(Optional.empty());

        Usuario usuario = MockUtils.criarUsuarioAdmin();

        assertThrows(EntityNotFoundException.class, () -> cargoService.ativar(1L, usuario));
    }

    @Test
    void testAtivarComUsuarioTester() {
        Cargo cargo = new Cargo();
        cargo.setId(1L);
        cargo.setAtivo(false);

        when(cargoRepository.findByIdAndAtivoFalse(1L)).thenReturn(Optional.of(cargo));

        Usuario usuario = MockUtils.criarUsuario(TipoUsuarioEnum.TESTER);

        assertThrows(AutorizacaoException.class, () -> cargoService.ativar(1L, usuario));
    }

    @Nested
    class AtualizarTestes {

        @Test
        void testAtualizarCargoInexistente() {
            when(cargoRepository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.empty());

            Usuario usuario = MockUtils.criarUsuarioAdmin();
            DadosAtualizacaoCargo dados = new DadosAtualizacaoCargo("Novo Nome");

            assertThrows(EntityNotFoundException.class, () -> cargoService.atualizar(1L, dados, usuario));
        }

        @Test
        void testAtualizarComUsuarioNaoAdmin() {
            Cargo cargo = new Cargo();
            cargo.setId(1L);
            cargo.setNome("Antigo");
            cargo.setAtivo(true);

            when(cargoRepository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.of(cargo));

            Usuario usuario = MockUtils.criarUsuario(TipoUsuarioEnum.GESTOR);
            DadosAtualizacaoCargo dados = new DadosAtualizacaoCargo("Novo Nome");

            assertThrows(AutorizacaoException.class, () -> cargoService.atualizar(1L, dados, usuario));
        }

        @Test
        void testAtualizarComNomeInvalido() {
            Cargo cargo = new Cargo();
            cargo.setId(1L);
            cargo.setNome(null);
            cargo.setAtivo(true);

            when(cargoRepository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.of(cargo));

            Usuario usuario = MockUtils.criarUsuarioAdmin();
            DadosAtualizacaoCargo dados = new DadosAtualizacaoCargo("Novo Nome");

            assertThrows(ValidacaoException.class, () -> cargoService.atualizar(1L, dados, usuario));
        }
    }

    @Nested
    class CadastrarTestes {

        @Test
        void testCadastrarComUsuarioTester() {
            Usuario usuario = MockUtils.criarUsuario(TipoUsuarioEnum.TESTER);
            DadosCadastroCargo dados = new DadosCadastroCargo("Novo Cargo");

            assertThrows(AutorizacaoException.class, () -> cargoService.cadastrar(dados, usuario));
        }

        @Test
        void testCadastrarComUsuarioNaoAdmin() {
            Usuario usuario = MockUtils.criarUsuario(TipoUsuarioEnum.GESTOR);
            DadosCadastroCargo dados = new DadosCadastroCargo("Novo Cargo");

            assertThrows(AutorizacaoException.class, () -> cargoService.cadastrar(dados, usuario));
        }
    }

    @Nested
    class DeletarTestes {

        @Test
        void testDeletarCargoInexistente() {
            when(cargoRepository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.empty());

            Usuario usuario = MockUtils.criarUsuarioAdmin();

            assertThrows(EntityNotFoundException.class, () -> cargoService.deletar(1L, usuario));
        }

        @Test
        void testDeletarComUsuarioTester() {
            Cargo cargo = new Cargo();
            cargo.setId(1L);
            cargo.setAtivo(true);

            when(cargoRepository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.of(cargo));

            Usuario usuario = MockUtils.criarUsuario(TipoUsuarioEnum.TESTER);

            assertThrows(AutorizacaoException.class, () -> cargoService.deletar(1L, usuario));
        }
    }

    @Test
    void testListarComUsuarioNaoAutorizado() {
        Usuario usuario = MockUtils.criarUsuario(TipoUsuarioEnum.GESTOR);
        Pageable pageable = PageRequest.of(0, 10);

        assertThrows(AutorizacaoException.class, () -> cargoService.listar(pageable, usuario, true));
    }
}
