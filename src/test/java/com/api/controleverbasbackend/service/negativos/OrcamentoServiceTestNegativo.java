package com.api.controleverbasbackend.service.negativos;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.api.controleverbasbackend.domain.entity.orcamento.Orcamento;
import com.api.controleverbasbackend.domain.entity.orcamento.StatusOrcamentoEntidade;
import com.api.controleverbasbackend.domain.enums.orcamento.StatusOrcamentoEnum;
import com.api.controleverbasbackend.domain.enums.usuario.TipoUsuarioEnum;
import com.api.controleverbasbackend.domain.entity.usuario.Usuario;
import com.api.controleverbasbackend.dto.orcamento.DadosCadastroOrcamento;
import com.api.controleverbasbackend.exception.AutorizacaoException;
import com.api.controleverbasbackend.exception.ValidacaoException;
import com.api.controleverbasbackend.repository.OrcamentoRepository;
import com.api.controleverbasbackend.repository.StatusOrcamentoRepository;
import com.api.controleverbasbackend.service.OrcamentoService;
import com.api.controleverbasbackend.utils.MockUtils;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class OrcamentoServiceTestNegativo {

        @Mock
        private OrcamentoRepository orcamentoRepository;

        @Mock
        private StatusOrcamentoRepository statusOrcamentoRepository;

        @InjectMocks
        private OrcamentoService orcamentoService;

        private Usuario usuarioTester;
        private Usuario usuarioComum;
        private Usuario usuarioGestor;
        private Orcamento orcamento;

        @BeforeEach
        void setup() {
                usuarioTester = MockUtils.criarUsuario(TipoUsuarioEnum.TESTER);
                usuarioComum = MockUtils.criarUsuario(TipoUsuarioEnum.COMUM);
                usuarioGestor = MockUtils.criarUsuario(TipoUsuarioEnum.GESTOR);

                orcamento = new Orcamento();
                orcamento.setStatusOrcamentoEntidade(
                                new StatusOrcamentoEntidade(StatusOrcamentoEnum.ENVIADO.getId(), "ENVIADO"));
                orcamento.setSolicitante(usuarioComum);
        }

        @Nested
        class AprovarTestes {

                @Test
                void testAprovarOrcamentoUsuarioTesterDeveFalhar() {
                        Assertions.assertThrows(AutorizacaoException.class,
                                        () -> orcamentoService.aprovar(1L, usuarioTester));
                }

                @Test
                void testAprovarOrcamentoUsuarioNaoGestorDeveFalhar() {
                        Assertions.assertThrows(AutorizacaoException.class,
                                        () -> orcamentoService.aprovar(1L, usuarioComum));
                }

                @Test
                void testAprovarOrcamentoStatusAprovadoNaoEncontrado() {
                        when(orcamentoRepository.getReferenceById(1L)).thenReturn(orcamento);
                        when(statusOrcamentoRepository.findById(StatusOrcamentoEnum.APROVADO.getId()))
                                        .thenReturn(Optional.empty());

                        Assertions.assertThrows(ValidacaoException.class,
                                        () -> orcamentoService.aprovar(1L, usuarioGestor));
                }
        }

        @Nested
        class CadastrarTestes {
                @Test
                void testCadastrarOrcamentoUsuarioTesterDeveFalhar() {
                        DadosCadastroOrcamento dados = new DadosCadastroOrcamento(
                                        "Fornecedor X",
                                        "Descrição Y",
                                        "À vista",
                                        BigDecimal.TEN,
                                        null);

                        Assertions.assertThrows(AutorizacaoException.class,
                                        () -> orcamentoService.cadastrar(dados, usuarioTester));
                }

                @Test
                void testCadastrarOrcamentoStatusNaoEncontrado() {
                        DadosCadastroOrcamento dados = new DadosCadastroOrcamento(
                                        "Fornecedor X",
                                        "Descrição Y",
                                        "À vista",
                                        BigDecimal.TEN,
                                        null);

                        when(statusOrcamentoRepository.findById(StatusOrcamentoEnum.ENVIADO.getId()))
                                        .thenReturn(Optional.empty());

                        Assertions.assertThrows(ValidacaoException.class,
                                        () -> orcamentoService.cadastrar(dados, usuarioComum));
                }
        }

        @Nested
        class LiberarTestes {

                @Test
                void testLiberarVerbaOrcamentoNaoEncontrado() {
                        when(orcamentoRepository.findByIdAndVerbaLiberadaFalse(1L)).thenReturn(Optional.empty());

                        Assertions.assertThrows(EntityNotFoundException.class,
                                        () -> orcamentoService.liberarVerba(1L, usuarioGestor));
                }

                @Test
                void testLiberarVerbaUsuarioTesterDeveFalhar() {
                        when(orcamentoRepository.findByIdAndVerbaLiberadaFalse(1L)).thenReturn(Optional.of(orcamento));

                        Assertions.assertThrows(AutorizacaoException.class,
                                        () -> orcamentoService.liberarVerba(1L, usuarioTester));
                }

                @Test
                void testLiberarVerbaUsuarioNaoAutorizadoDeveFalhar() {
                        Usuario usuarioComumNaoAutorizado = MockUtils.criarUsuario(TipoUsuarioEnum.COMUM);
                        when(orcamentoRepository.findByIdAndVerbaLiberadaFalse(1L)).thenReturn(Optional.of(orcamento));

                        Assertions.assertThrows(AutorizacaoException.class,
                                        () -> orcamentoService.liberarVerba(1L, usuarioComumNaoAutorizado));
                }

                @Test
                void testLiberarVerbaOrcamentoNaoAprovadoDeveFalhar() {
                        orcamento.setStatusOrcamentoEntidade(
                                        new StatusOrcamentoEntidade(StatusOrcamentoEnum.ENVIADO.getId(), "ENVIADO"));
                        when(orcamentoRepository.findByIdAndVerbaLiberadaFalse(1L)).thenReturn(Optional.of(orcamento));

                        Assertions.assertThrows(ValidacaoException.class,
                                        () -> orcamentoService.liberarVerba(1L, usuarioGestor));
                }
        }

        @Nested
        class ReprovarTestes {

                @Test
                void testReprovarOrcamentoUsuarioTesterDeveFalhar() {
                        Assertions.assertThrows(AutorizacaoException.class,
                                        () -> orcamentoService.reprovar(1L, usuarioTester));
                }

                @Test
                void testReprovarOrcamentoUsuarioNaoGestorDeveFalhar() {
                        Assertions.assertThrows(AutorizacaoException.class,
                                        () -> orcamentoService.reprovar(1L, usuarioComum));
                }

                @Test
                void testReprovarOrcamentoStatusReprovadoNaoEncontrado() {
                        when(orcamentoRepository.getReferenceById(1L)).thenReturn(orcamento);
                        when(statusOrcamentoRepository.findById(StatusOrcamentoEnum.REPROVADO.getId()))
                                        .thenReturn(Optional.empty());

                        Assertions.assertThrows(ValidacaoException.class,
                                        () -> orcamentoService.reprovar(1L, usuarioGestor));
                }
        }
}
