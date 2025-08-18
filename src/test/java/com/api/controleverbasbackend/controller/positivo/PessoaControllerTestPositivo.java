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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import com.api.controleverbasbackend.controller.PessoaController;
import com.api.controleverbasbackend.domain.entity.pessoa.Pessoa;
import com.api.controleverbasbackend.domain.entity.usuario.Usuario;
import com.api.controleverbasbackend.dto.pessoa.DadosAtualizacaoPessoa;
import com.api.controleverbasbackend.dto.pessoa.DadosCadastroPessoa;
import com.api.controleverbasbackend.dto.pessoa.DadosCadastroPessoaUsuario;
import com.api.controleverbasbackend.dto.pessoa.DadosDetalhamentoPessoa;
import com.api.controleverbasbackend.dto.pessoa.DadosListagemPessoa;
import com.api.controleverbasbackend.dto.usuario.DadosCadastroUsuario;
import com.api.controleverbasbackend.service.PessoaService;
import com.api.controleverbasbackend.utils.MockUtils;

@ExtendWith(MockitoExtension.class)
public class PessoaControllerTestPositivo {

    @InjectMocks
    private PessoaController pessoaController;

    @Mock
    private PessoaService pessoaService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = MockUtils.criarUsuarioAdmin();
    }

    @Test
    void testAtivar() {
        var response = pessoaController.ativar(8L, usuario);

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
        verify(pessoaService).ativar(8L, usuario);
    }

    @SuppressWarnings("null")
    @Test
    void testAtualizar() {
        Pessoa pessoa = MockUtils.criarPessoaCompleta();
        var atualizacao = new DadosAtualizacaoPessoa(1L, 1L);
        var detalhes = new DadosDetalhamentoPessoa(pessoa);

        when(pessoaService.atualizar(eq(5L), eq(atualizacao), eq(usuario)))
                .thenReturn(detalhes);

        var response = pessoaController.atualizar(5L, atualizacao, usuario);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().nome()).isEqualTo("Maria");
        verify(pessoaService).atualizar(eq(5L), eq(atualizacao), eq(usuario));
    }

    @SuppressWarnings("null")
    @Test
    void testCadastrar() {
        DadosCadastroPessoa dadosPessoa = MockUtils.criarDadosCadastroPessoa();
        DadosCadastroUsuario dadosUsuario = MockUtils.criarDadosCadastroUsuario();
        Pessoa pessoa = MockUtils.criarPessoaCompleta();

        var dadosCadastro = new DadosCadastroPessoaUsuario(dadosPessoa, dadosUsuario);
        var detalhes = new DadosDetalhamentoPessoa(pessoa);

        when(pessoaService.cadastrar(eq(dadosCadastro.pessoa()), eq(dadosCadastro.usuario()), eq(usuario)))
                .thenReturn(detalhes);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath("");

        var response = pessoaController.cadastrar(dadosCadastro, uriBuilder, usuario);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getHeaders().getLocation()).isNotNull();
        assertThat(response.getHeaders().getLocation().toString()).isEqualTo("/pessoas/1");
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().nome()).isEqualTo("Maria");
        verify(pessoaService).cadastrar(eq(dadosCadastro.pessoa()), eq(dadosCadastro.usuario()), eq(usuario));
    }

    @Test
    void testDeletar() {
        var response = pessoaController.deletar(7L, usuario);

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
        verify(pessoaService).deletar(7L, usuario);
    }

    @SuppressWarnings("null")
    @Test
    void testListar() {
        Pessoa pessoa = MockUtils.criarPessoaCompleta();
        var dto = new DadosListagemPessoa(pessoa);
        Page<DadosListagemPessoa> page = new PageImpl<>(List.of(dto), PageRequest.of(0, 10), 1);

        when(pessoaService.listar(any(Pageable.class), eq(usuario), anyBoolean()))
                .thenReturn(page);

        ResponseEntity<Page<DadosListagemPessoa>> response = pessoaController.listar(true, PageRequest.of(0, 10),
                usuario);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalElements()).isEqualTo(1);
        assertThat(response.getBody().getContent().get(0).nome()).isEqualTo("Maria");
        verify(pessoaService).listar(any(Pageable.class), eq(usuario), eq(true));
    }
}
