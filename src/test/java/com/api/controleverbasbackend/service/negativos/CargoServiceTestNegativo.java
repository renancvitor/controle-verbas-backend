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

import com.api.controleverbasbackend.domain.entity.cargo.Cargo;
import com.api.controleverbasbackend.domain.entity.usuario.Usuario;
import com.api.controleverbasbackend.domain.enums.usuario.TipoUsuarioEnum;
import com.api.controleverbasbackend.dto.cargo.DadosAtualizacaoCargo;
import com.api.controleverbasbackend.dto.cargo.DadosCadastroCargo;
import com.api.controleverbasbackend.exception.AutorizacaoException;
import com.api.controleverbasbackend.exception.ValidacaoException;
import com.api.controleverbasbackend.infra.messaging.kafka.LogProducer;
import com.api.controleverbasbackend.repository.CargoRepository;
import com.api.controleverbasbackend.service.CargoService;
import com.api.controleverbasbackend.utils.MockUtils;

import jakarta.persistence.EntityNotFoundException;

@SpringBootTest
@ActiveProfiles("test")
public class CargoServiceTestNegativo {

    @MockitoBean
    private LogProducer logProducer;

    @MockitoBean
    private CargoRepository cargoRepository;

    @Autowired
    private CargoService cargoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAtivarComCargoInexistente() {
        Cargo cargo = MockUtils.idPadrao(new Cargo());
        when(cargoRepository.findByIdAndAtivoFalse(cargo.getId()))
                .thenReturn(Optional.empty());

        Usuario usuario = MockUtils.criarUsuarioAdmin();

        assertThrows(EntityNotFoundException.class, () -> cargoService.ativar(cargo.getId(), usuario));
    }

    @Test
    void testAtivarComUsuarioTester() {
        Cargo cargo = MockUtils.idPadrao(new Cargo());
        cargo.setAtivo(false);

        when(cargoRepository.findByIdAndAtivoFalse(cargo.getId()))
                .thenReturn(Optional.of(cargo));

        Usuario usuario = MockUtils.criarUsuario(TipoUsuarioEnum.TESTER);

        assertThrows(AutorizacaoException.class, () -> cargoService.ativar(cargo.getId(), usuario));
    }

    @Nested
    class AtualizarTestes {

        @Test
        void testAtualizarCargoInexistente() {
            Cargo cargo = MockUtils.idPadrao(new Cargo());
            when(cargoRepository.findByIdAndAtivoTrue(cargo.getId()))
                    .thenReturn(Optional.empty());

            Usuario usuario = MockUtils.criarUsuarioAdmin();
            DadosAtualizacaoCargo dados = new DadosAtualizacaoCargo("Novo Nome");

            assertThrows(EntityNotFoundException.class, () -> cargoService.atualizar(cargo.getId(), dados, usuario));
        }

        @Test
        void testAtualizarComUsuarioNaoAdmin() {
            Cargo cargo = MockUtils.idPadrao(new Cargo());
            cargo.setNome("Antigo");
            cargo.setAtivo(true);

            when(cargoRepository.findByIdAndAtivoTrue(cargo.getId()))
                    .thenReturn(Optional.of(cargo));

            Usuario usuario = MockUtils.criarUsuario(TipoUsuarioEnum.GESTOR);
            DadosAtualizacaoCargo dados = new DadosAtualizacaoCargo("Novo Nome");

            assertThrows(AutorizacaoException.class, () -> cargoService.atualizar(cargo.getId(), dados, usuario));
        }

        @Test
        void testAtualizarComNomeInvalido() {
            Cargo cargo = MockUtils.idPadrao(new Cargo());
            cargo.setNome(null);
            cargo.setAtivo(true);

            when(cargoRepository.findByIdAndAtivoTrue(cargo.getId()))
                    .thenReturn(Optional.of(cargo));

            Usuario usuario = MockUtils.criarUsuarioAdmin();
            DadosAtualizacaoCargo dados = new DadosAtualizacaoCargo("Novo Nome");

            assertThrows(ValidacaoException.class, () -> cargoService.atualizar(cargo.getId(), dados, usuario));
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
            Cargo cargo = MockUtils.idPadrao(new Cargo());
            when(cargoRepository.findByIdAndAtivoTrue(cargo.getId()))
                    .thenReturn(Optional.empty());

            Usuario usuario = MockUtils.criarUsuarioAdmin();

            assertThrows(EntityNotFoundException.class, () -> cargoService.deletar(cargo.getId(), usuario));
        }

        @Test
        void testDeletarComUsuarioTester() {
            Cargo cargo = MockUtils.idPadrao(new Cargo());
            cargo.setAtivo(true);

            when(cargoRepository.findByIdAndAtivoTrue(cargo.getId()))
                    .thenReturn(Optional.of(cargo));

            Usuario usuario = MockUtils.criarUsuario(TipoUsuarioEnum.TESTER);

            assertThrows(AutorizacaoException.class, () -> cargoService.deletar(cargo.getId(), usuario));
        }
    }

    @Test
    void testListarComUsuarioNaoAutorizado() {
        Usuario usuario = MockUtils.criarUsuario(TipoUsuarioEnum.GESTOR);
        Pageable pageable = PageRequest.of(0, 10);

        assertThrows(AutorizacaoException.class, () -> cargoService.listar(pageable, usuario, true));
    }
}
