package com.api.controleverbasbackend.service.positivos;

import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import com.api.controleverbasbackend.domain.pessoa.Pessoa;
import com.api.controleverbasbackend.domain.usuario.Usuario;
import com.api.controleverbasbackend.domain.usuario.TipoUsuarioEnum;
import com.api.controleverbasbackend.domain.usuario.TipoUsuarioEntidade;
import com.api.controleverbasbackend.dto.usuario.*;
import com.api.controleverbasbackend.repository.UsuarioRepository;
import com.api.controleverbasbackend.repository.TipoUsuarioRepository;
import com.api.controleverbasbackend.service.UsuarioService;
import com.api.controleverbasbackend.utils.MockUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTestPositivo {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private TipoUsuarioRepository tipoUsuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioAdmin;
    private Pessoa pessoa;

    @BeforeEach
    void setup() {
        usuarioAdmin = MockUtils.criarUsuarioAdmin();
        usuarioAdmin.setId(1L);

        pessoa = new Pessoa();
        pessoa.setId(1L);
    }

    @Test
    void testAtivar() {
        Usuario usuario = MockUtils.criarUsuario(TipoUsuarioEnum.COMUM);
        usuario.setId(1L);
        usuario.setAtivo(false);

        when(usuarioRepository.findByIdAndAtivoFalse(1L)).thenReturn(Optional.of(usuario));

        usuarioService.ativar(1L, usuarioAdmin);

        assertTrue(usuario.getAtivo());
    }

    @Test
    void testAtualizarSenha() {
        String senhaCriptografada = "senhaCripto";
        Usuario usuario = MockUtils.criarUsuario(TipoUsuarioEnum.COMUM);
        usuario.setId(1L);
        usuario.setSenha(senhaCriptografada);

        DadosAtualizacaoUsuarioSenha dados = new DadosAtualizacaoUsuarioSenha(
                "senhaAtual", "NovaSenha@1", "NovaSenha@1");

        when(usuarioRepository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senhaAtual", senhaCriptografada)).thenReturn(true);
        when(passwordEncoder.encode("NovaSenha@1")).thenReturn("novaSenhaCripto");

        DadosDetalhamentoUsuario resultado = usuarioService.atualizarSenha(1L, dados, usuario);

        assertEquals(usuario.getId(), resultado.id());
        assertFalse(usuario.getPrimeiroAcesso());
    }

    @Test
    void testAtualizarUsuarioTipo() {
        Usuario usuario = MockUtils.criarUsuario(TipoUsuarioEnum.COMUM);
        usuario.setId(1L);

        DadosAtualizacaoUsuarioTipo dados = new DadosAtualizacaoUsuarioTipo(TipoUsuarioEnum.ADMIN.getId());

        when(usuarioRepository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.of(usuario));
        TipoUsuarioEntidade tipoUsuario = new TipoUsuarioEntidade();
        tipoUsuario.setId(TipoUsuarioEnum.ADMIN.getId());
        when(tipoUsuarioRepository.findById(TipoUsuarioEnum.ADMIN.getId())).thenReturn(Optional.of(tipoUsuario));

        DadosDetalhamentoUsuario resultado = usuarioService.atualizarUsuarioTipo(1L, dados, usuarioAdmin);

        assertEquals(usuario.getId(), resultado.id());
    }

    @Test
    void testCadastrar() {
        DadosCadastroUsuario dados = new DadosCadastroUsuario("123456", "123456", TipoUsuarioEnum.COMUM.getId());
        when(passwordEncoder.encode(dados.senha())).thenReturn("senhaCriptografada");
        TipoUsuarioEntidade tipoUsuario = new TipoUsuarioEntidade();
        tipoUsuario.setId(TipoUsuarioEnum.COMUM.getId());
        when(tipoUsuarioRepository.findById(TipoUsuarioEnum.COMUM.getId())).thenReturn(Optional.of(tipoUsuario));

        usuarioService.cadastrar(pessoa, dados);

        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void testDeletar() {
        Usuario usuario = MockUtils.criarUsuario(TipoUsuarioEnum.COMUM);
        usuario.setId(1L);

        when(usuarioRepository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.of(usuario));

        usuarioService.deletar(1L, usuarioAdmin);

        assertFalse(usuario.getAtivo());
    }

    @Test
    void testListar() {
        Page<Usuario> usuarios = new PageImpl<>(List.of(MockUtils.criarUsuario(TipoUsuarioEnum.COMUM)));
        when(usuarioRepository.findAll(PageRequest.of(0, 10))).thenReturn(usuarios);

        Page<DadosListagemUsuario> resultado = usuarioService.listar(PageRequest.of(0, 10), usuarioAdmin, null);

        assertEquals(1, resultado.getTotalElements());
        verify(usuarioRepository).findAll(PageRequest.of(0, 10));
    }
}
