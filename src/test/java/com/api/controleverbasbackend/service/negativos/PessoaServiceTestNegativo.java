package com.api.controleverbasbackend.service.negativos;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.api.controleverbasbackend.domain.entity.pessoa.Pessoa;
import com.api.controleverbasbackend.domain.entity.usuario.Usuario;
import com.api.controleverbasbackend.domain.enums.usuario.TipoUsuarioEnum;
import com.api.controleverbasbackend.repository.PessoaRepository;
import com.api.controleverbasbackend.repository.DepartamentoRepository;
import com.api.controleverbasbackend.repository.CargoRepository;
import com.api.controleverbasbackend.service.PessoaService;
import com.api.controleverbasbackend.service.UsuarioService;
import com.api.controleverbasbackend.utils.MockUtils;

import jakarta.persistence.EntityNotFoundException;

import com.api.controleverbasbackend.dto.pessoa.DadosCadastroPessoa;
import com.api.controleverbasbackend.dto.usuario.DadosCadastroUsuario;
import com.api.controleverbasbackend.exception.AutorizacaoException;
import com.api.controleverbasbackend.dto.pessoa.DadosAtualizacaoPessoa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class PessoaServiceTestNegativo {

    @Mock
    private PessoaRepository pessoaRepository;

    @Mock
    private DepartamentoRepository departamentoRepository;

    @Mock
    private CargoRepository cargoRepository;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private PessoaService pessoaService;

    private Usuario usuarioTester;
    private Usuario usuarioComum;
    private Pessoa pessoa;

    @BeforeEach
    void setup() {
        usuarioTester = MockUtils.criarUsuario(TipoUsuarioEnum.TESTER);
        usuarioComum = MockUtils.criarUsuario(TipoUsuarioEnum.COMUM);

        pessoa = new Pessoa();
        pessoa.setId(1L);
    }

    @Nested
    class AtivarTestes {
        @Test
        void ativar_DeveFalharParaUsuarioTester() {
            when(pessoaRepository.findByIdAndAtivoFalse(1L)).thenReturn(Optional.of(pessoa));

            assertThrows(AutorizacaoException.class,
                    () -> pessoaService.ativar(1L, usuarioTester));
        }

        @Test
        void ativar_DeveFalharParaUsuarioNaoAdmin() {
            when(pessoaRepository.findByIdAndAtivoFalse(1L)).thenReturn(Optional.of(pessoa));

            assertThrows(AutorizacaoException.class,
                    () -> pessoaService.ativar(1L, usuarioComum));
        }

        @Test
        void ativar_DeveFalharSePessoaNaoExistir() {
            when(pessoaRepository.findByIdAndAtivoFalse(1L)).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class,
                    () -> pessoaService.ativar(1L, MockUtils.criarUsuarioAdmin()));
        }
    }

    @Nested
    class AtualizarTestes {
        @Test
        void atualizar_DeveFalharParaUsuarioTester() {
            DadosAtualizacaoPessoa dadosAtualizacao = mock(DadosAtualizacaoPessoa.class);

            when(pessoaRepository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.of(pessoa));

            assertThrows(AutorizacaoException.class,
                    () -> pessoaService.atualizar(1L, dadosAtualizacao, usuarioTester));
        }

        @Test
        void atualizar_DeveFalharParaUsuarioNaoAdmin() {
            DadosAtualizacaoPessoa dadosAtualizacao = mock(DadosAtualizacaoPessoa.class);

            when(pessoaRepository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.of(pessoa));

            assertThrows(AutorizacaoException.class,
                    () -> pessoaService.atualizar(1L, dadosAtualizacao, usuarioComum));
        }

        @Test
        void atualizar_DeveFalharSePessoaNaoExistir() {
            DadosAtualizacaoPessoa dadosAtualizacao = mock(DadosAtualizacaoPessoa.class);

            when(pessoaRepository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class,
                    () -> pessoaService.atualizar(1L, dadosAtualizacao, MockUtils.criarUsuarioAdmin()));
        }
    }

    @Test
    void cadastrar_DeveFalharParaUsuarioTester() {
        DadosCadastroPessoa dadosPessoa = mock(DadosCadastroPessoa.class);
        DadosCadastroUsuario dadosUsuario = mock(DadosCadastroUsuario.class);

        assertThrows(AutorizacaoException.class,
                () -> pessoaService.cadastrar(dadosPessoa, dadosUsuario, usuarioTester));
    }

    @Nested
    class DeletarTestes {
        @Test
        void deletar_DeveFalharParaUsuarioTester() {
            when(pessoaRepository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.of(pessoa));

            assertThrows(AutorizacaoException.class,
                    () -> pessoaService.deletar(1L, usuarioTester));
        }

        @Test
        void deletar_DeveFalharParaUsuarioNaoAdmin() {
            when(pessoaRepository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.of(pessoa));

            assertThrows(AutorizacaoException.class,
                    () -> pessoaService.deletar(1L, usuarioComum));
        }

        @Test
        void deletar_DeveFalharSePessoaNaoExistir() {
            when(pessoaRepository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class,
                    () -> pessoaService.deletar(1L, MockUtils.criarUsuarioAdmin()));
        }
    }

    @Test
    void listar_DeveFalharParaUsuarioComum() {
        assertThrows(AutorizacaoException.class,
                () -> pessoaService.listar(null, usuarioComum, null));
    }
}
