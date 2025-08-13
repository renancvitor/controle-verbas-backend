package com.api.controleverbasbackend.service.negativos;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import com.api.controleverbasbackend.domain.pessoa.Pessoa;
import com.api.controleverbasbackend.domain.usuario.Usuario;
import com.api.controleverbasbackend.domain.usuario.TipoUsuarioEnum;
import com.api.controleverbasbackend.dto.usuario.*;
import com.api.controleverbasbackend.infra.exception.AutorizacaoException;
import com.api.controleverbasbackend.infra.exception.ValidacaoException;
import com.api.controleverbasbackend.repository.UsuarioRepository;
import com.api.controleverbasbackend.repository.TipoUsuarioRepository;
import com.api.controleverbasbackend.service.UsuarioService;
import com.api.controleverbasbackend.utils.MockUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTestNegativo {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private TipoUsuarioRepository tipoUsuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioAdmin;
    private Usuario usuarioComum;
    private Pessoa pessoa;

    @BeforeEach
    void setup() {
        usuarioAdmin = MockUtils.criarUsuarioAdmin();
        usuarioAdmin.setId(1L);

        usuarioComum = MockUtils.criarUsuario(TipoUsuarioEnum.COMUM);
        usuarioComum.setId(2L);

        pessoa = new Pessoa();
        pessoa.setId(1L);
    }

    @Test
    void ativar_DeveLancarAutorizacaoException_SeUsuarioNaoAdmin() {
        Usuario usuario = MockUtils.criarUsuario(TipoUsuarioEnum.COMUM);
        usuario.setId(2L);

        when(usuarioRepository.findByIdAndAtivoFalse(2L)).thenReturn(Optional.of(usuario));

        AutorizacaoException exception = assertThrows(AutorizacaoException.class,
                () -> usuarioService.ativar(2L, usuarioComum));

        assertEquals("Apenas o admin pode ativar um usuário.", exception.getMessage());
    }

    @Nested
    class AtualizarTestes {
        @Test
        void atualizarSenha_DeveLancarValidacaoException_SeSenhaAtualIncorreta() {
            Usuario usuario = MockUtils.criarUsuario(TipoUsuarioEnum.COMUM);
            usuario.setId(2L);
            usuario.setSenha("senhaCripto");

            DadosAtualizacaoUsuarioSenha dados = new DadosAtualizacaoUsuarioSenha("senhaErrada", "NovaSenha@1",
                    "NovaSenha@1");

            when(usuarioRepository.findByIdAndAtivoTrue(2L)).thenReturn(Optional.of(usuario));
            when(passwordEncoder.matches("senhaErrada", "senhaCripto")).thenReturn(false);

            ValidacaoException exception = assertThrows(ValidacaoException.class,
                    () -> usuarioService.atualizarSenha(2L, dados, usuario));

            assertEquals("A senha atual está incorreta.", exception.getMessage());
        }

        @Test
        void atualizarSenha_DeveLancarValidacaoException_SeConfirmacaoNaoCoincide() {
            Usuario usuario = MockUtils.criarUsuario(TipoUsuarioEnum.COMUM);
            usuario.setId(2L);
            usuario.setSenha("senhaCripto");

            DadosAtualizacaoUsuarioSenha dados = new DadosAtualizacaoUsuarioSenha("senhaCripto", "NovaSenha@1",
                    "OutraSenha@1");

            when(usuarioRepository.findByIdAndAtivoTrue(2L)).thenReturn(Optional.of(usuario));
            when(passwordEncoder.matches("senhaCripto", "senhaCripto")).thenReturn(true);

            ValidacaoException exception = assertThrows(ValidacaoException.class,
                    () -> usuarioService.atualizarSenha(2L, dados, usuario));

            assertEquals("A nova senha e a confirmação não coincidem.", exception.getMessage());
        }

        @Test
        void atualizarUsuarioTipo_DeveLancarAutorizacaoException_SeUsuarioNaoAdmin() {
            Usuario usuario = MockUtils.criarUsuario(TipoUsuarioEnum.COMUM);
            usuario.setId(2L);

            DadosAtualizacaoUsuarioTipo dados = new DadosAtualizacaoUsuarioTipo(TipoUsuarioEnum.ADMIN.getId());

            when(usuarioRepository.findByIdAndAtivoTrue(2L)).thenReturn(Optional.of(usuario));

            AutorizacaoException exception = assertThrows(AutorizacaoException.class,
                    () -> usuarioService.atualizarUsuarioTipo(2L, dados, usuarioComum));

            assertEquals("Apenas o admin pode atualizar pessoas.", exception.getMessage());
        }

        @Test
        void atualizarSenha_DeveLancarRuntimeException_SeUsuarioTentarAlterarSenhaDeOutro() {
            Usuario usuario = MockUtils.criarUsuario(TipoUsuarioEnum.COMUM);
            usuario.setId(2L);
            usuario.setSenha("senhaCripto");

            DadosAtualizacaoUsuarioSenha dados = new DadosAtualizacaoUsuarioSenha("senhaCripto", "NovaSenha@1",
                    "NovaSenha@1");

            when(usuarioRepository.findByIdAndAtivoTrue(2L)).thenReturn(Optional.of(usuario));

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> usuarioService.atualizarSenha(2L, dados, usuarioAdmin));

            assertEquals("Você só pode alterar sua própria senha.", exception.getMessage());
        }
    }

    @Test
    void cadastrar_DeveLancarEntityNotFoundException_SePerfilPadraoNaoExistir() {
        when(passwordEncoder.encode(anyString())).thenReturn("senhaCripto");
        when(tipoUsuarioRepository.findById(TipoUsuarioEnum.COMUM.getId())).thenReturn(Optional.empty());

        DadosCadastroUsuario dados = new DadosCadastroUsuario("123456", "123456", TipoUsuarioEnum.COMUM.getId());

        assertThrows(Exception.class, () -> usuarioService.cadastrar(pessoa, dados));
    }

    @Test
    void deletar_DeveLancarAutorizacaoException_SeUsuarioNaoAdmin() {
        Usuario usuario = MockUtils.criarUsuario(TipoUsuarioEnum.COMUM);
        usuario.setId(2L);

        when(usuarioRepository.findByIdAndAtivoTrue(2L)).thenReturn(Optional.of(usuario));

        AutorizacaoException exception = assertThrows(AutorizacaoException.class,
                () -> usuarioService.deletar(2L, usuarioComum));

        assertEquals("Apenas o admin pode deletar um usuário.", exception.getMessage());
    }

    @Test
    void listar_DeveLancarAutorizacaoException_SeUsuarioNaoAdminOuTester() {
        Usuario usuario = MockUtils.criarUsuario(TipoUsuarioEnum.COMUM);

        AutorizacaoException exception = assertThrows(AutorizacaoException.class,
                () -> usuarioService.listar(PageRequest.of(0, 10), usuario, null));

        assertEquals("Apenas o admin pode listar usuários cadastrados.", exception.getMessage());
    }
}
