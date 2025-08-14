package com.api.controleverbasbackend.controller.negativo;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.api.controleverbasbackend.controller.UsuarioController;
import com.api.controleverbasbackend.domain.usuario.Usuario;
import com.api.controleverbasbackend.service.UsuarioService;
import com.api.controleverbasbackend.utils.MockUtils;

@ExtendWith(SpringExtension.class)
public class UsuarioControllerTestNegativo {

    @InjectMocks
    private UsuarioController usuarioController;

    @Mock
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = MockUtils.criarUsuarioAdmin();
    }

    @Test
    void testAtivar() {
        doThrow(new RuntimeException("Usuário não encontrado"))
                .when(usuarioService).ativar(eq(999L), eq(usuario));

        assertThatThrownBy(() -> usuarioController.ativar(999L, usuario))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuário não encontrado");

        verify(usuarioService).ativar(eq(999L), eq(usuario));
    }

    @Test
    void testDeletar() {
        doThrow(new RuntimeException("Usuário não encontrado"))
                .when(usuarioService).deletar(eq(999L), eq(usuario));

        assertThatThrownBy(() -> usuarioController.deletar(999L, usuario))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuário não encontrado");

        verify(usuarioService).deletar(eq(999L), eq(usuario));
    }
}
