package com.api.controleverbasbackend.controller.negativo;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.util.UriComponentsBuilder;

import com.api.controleverbasbackend.controller.PessoaController;
import com.api.controleverbasbackend.domain.entity.usuario.Usuario;
import com.api.controleverbasbackend.dto.pessoa.DadosAtualizacaoPessoa;
import com.api.controleverbasbackend.dto.pessoa.DadosCadastroPessoaUsuario;
import com.api.controleverbasbackend.service.PessoaService;
import com.api.controleverbasbackend.utils.MockUtils;

@ExtendWith(MockitoExtension.class)
public class PessoaControllerTestNegativo {

        @InjectMocks
        private PessoaController pessoaController;

        @Mock
        private PessoaService pessoaService;

        private Usuario usuario;

        private final Long ID_INEXISTENTE = 999L;

        @BeforeEach
        void setUp() {
                usuario = MockUtils.criarUsuarioAdmin();
        }

        @Test
        void testAtivar() {
                doThrow(new RuntimeException("Pessoa não encontrada"))
                                .when(pessoaService).ativar(eq(ID_INEXISTENTE), eq(usuario));

                assertThatThrownBy(() -> pessoaController.ativar(ID_INEXISTENTE, usuario))
                                .isInstanceOf(RuntimeException.class)
                                .hasMessageContaining("Pessoa não encontrada");

                verify(pessoaService).ativar(eq(ID_INEXISTENTE), eq(usuario));
        }

        @Test
        void testAtualizar() {
                var atualizacao = new DadosAtualizacaoPessoa(1L, 1L);

                when(pessoaService.atualizar(eq(ID_INEXISTENTE), eq(atualizacao), eq(usuario)))
                                .thenThrow(new RuntimeException("Pessoa não encontrada"));

                assertThatThrownBy(() -> pessoaController.atualizar(ID_INEXISTENTE, atualizacao, usuario))
                                .isInstanceOf(RuntimeException.class)
                                .hasMessageContaining("Pessoa não encontrada");

                verify(pessoaService).atualizar(eq(ID_INEXISTENTE), eq(atualizacao), eq(usuario));
        }

        @Test
        void testCadastrar() {
                var dadosCadastro = new DadosCadastroPessoaUsuario(
                                MockUtils.criarDadosCadastroPessoa(),
                                MockUtils.criarDadosCadastroUsuario());

                when(pessoaService.cadastrar(eq(dadosCadastro.pessoa()), eq(dadosCadastro.usuario()), eq(usuario)))
                                .thenThrow(new IllegalArgumentException("CPF já cadastrado"));

                UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath("");

                assertThatThrownBy(() -> pessoaController.cadastrar(dadosCadastro, uriBuilder, usuario))
                                .isInstanceOf(IllegalArgumentException.class)
                                .hasMessageContaining("CPF já cadastrado");

                verify(pessoaService).cadastrar(eq(dadosCadastro.pessoa()), eq(dadosCadastro.usuario()), eq(usuario));
        }

        @Test
        void testDeletar() {
                doThrow(new RuntimeException("Pessoa não encontrada"))
                                .when(pessoaService).deletar(eq(ID_INEXISTENTE), eq(usuario));

                assertThatThrownBy(() -> pessoaController.deletar(ID_INEXISTENTE, usuario))
                                .isInstanceOf(RuntimeException.class)
                                .hasMessageContaining("Pessoa não encontrada");

                verify(pessoaService).deletar(eq(ID_INEXISTENTE), eq(usuario));
        }

        @Test
        void testListar() {
                when(pessoaService.listar(any(Pageable.class), eq(usuario), any(Boolean.class)))
                                .thenThrow(new RuntimeException("Erro interno"));

                assertThatThrownBy(() -> pessoaController.listar(true, PageRequest.of(0, 10), usuario))
                                .isInstanceOf(RuntimeException.class)
                                .hasMessageContaining("Erro interno");

                verify(pessoaService).listar(any(Pageable.class), eq(usuario), eq(true));
        }
}
