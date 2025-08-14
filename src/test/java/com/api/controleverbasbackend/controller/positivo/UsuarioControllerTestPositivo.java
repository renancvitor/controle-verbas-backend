package com.api.controleverbasbackend.controller.positivo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.api.controleverbasbackend.controller.UsuarioController;
import com.api.controleverbasbackend.domain.usuario.Usuario;
import com.api.controleverbasbackend.dto.usuario.DadosAtualizacaoUsuarioSenha;
import com.api.controleverbasbackend.dto.usuario.DadosDetalhamentoUsuario;
import com.api.controleverbasbackend.dto.usuario.DadosListagemUsuario;
import com.api.controleverbasbackend.service.UsuarioService;
import com.api.controleverbasbackend.utils.MockUtils;

@ExtendWith(SpringExtension.class)
public class UsuarioControllerTestPositivo {

    @InjectMocks
    private UsuarioController usuarioController;

    @Mock
    private UsuarioService usuarioService;

    private Usuario usuario;

    private final Long ID = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = MockUtils.criarUsuarioAdmin();
    }

    @Test
    void testAtivar() {
        ResponseEntity<Void> response = usuarioController.ativar(ID, usuario);

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
        verify(usuarioService).ativar(ID, usuario);
    }

    @SuppressWarnings("null")
    @Test
    void testAtualizarSenha() {
        DadosAtualizacaoUsuarioSenha dadosSenha = new DadosAtualizacaoUsuarioSenha(
                "senhaAntiga123",
                "novaSenha123",
                "novaSenha123");

        DadosDetalhamentoUsuario detalhes = new DadosDetalhamentoUsuario(
                1L,
                "111.111.111-11",
                "joao@email.com",
                "ADMIN");

        when(usuarioService.atualizarSenha(eq(1L), eq(dadosSenha), eq(usuario)))
                .thenReturn(detalhes);

        ResponseEntity<DadosDetalhamentoUsuario> response = usuarioController.atualizarSenha(1L, dadosSenha, usuario);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().nomeTipoUsuario()).isEqualTo("ADMIN");

        verify(usuarioService).atualizarSenha(eq(1L), eq(dadosSenha), eq(usuario));
    }

    @Test
    void testDeletar() {
        ResponseEntity<Void> response = usuarioController.deletar(ID, usuario);

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
        verify(usuarioService).deletar(ID, usuario);
    }

    @SuppressWarnings("null")
    @Test
    void testListar() {
        DadosListagemUsuario dto = new DadosListagemUsuario(ID, "joao@email.com", "João Teste", null, null, true);
        Page<DadosListagemUsuario> page = new PageImpl<>(List.of(dto), PageRequest.of(0, 10), 1);

        when(usuarioService.listar(any(Pageable.class), eq(usuario), anyBoolean()))
                .thenReturn(page);

        ResponseEntity<Page<DadosListagemUsuario>> response = usuarioController.listar(true, PageRequest.of(0, 10),
                usuario);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalElements()).isEqualTo(1);
        assertThat(response.getBody().getContent().get(0).nomePessoa()).isEqualTo("João Teste");

        verify(usuarioService).listar(any(Pageable.class), eq(usuario), eq(true));
    }
}
