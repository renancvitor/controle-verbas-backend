package com.api.controleverbasbackend.service.positivos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
import org.springframework.test.context.ActiveProfiles;

import com.api.controleverbasbackend.domain.entity.orcamento.Orcamento;
import com.api.controleverbasbackend.domain.entity.orcamento.StatusOrcamentoEntidade;
import com.api.controleverbasbackend.domain.enums.orcamento.StatusOrcamentoEnum;
import com.api.controleverbasbackend.domain.enums.usuario.TipoUsuarioEnum;
import com.api.controleverbasbackend.domain.entity.usuario.Usuario;
import com.api.controleverbasbackend.dto.orcamento.DadosCadastroOrcamento;
import com.api.controleverbasbackend.dto.orcamento.DadosDetalhamentoOrcamento;
import com.api.controleverbasbackend.dto.orcamento.DadosListagemOrcamento;
import com.api.controleverbasbackend.repository.OrcamentoRepository;
import com.api.controleverbasbackend.repository.StatusOrcamentoRepository;
import com.api.controleverbasbackend.service.OrcamentoService;
import com.api.controleverbasbackend.utils.MockUtils;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class OrcamentoServiceTestPositivo {

    @Mock
    private OrcamentoRepository orcamentoRepository;

    @Mock
    private StatusOrcamentoRepository statusOrcamentoRepository;

    @InjectMocks
    private OrcamentoService orcamentoService;

    private Orcamento orcamento;
    private Usuario usuarioGestor;
    private StatusOrcamentoEntidade statusAprovado;
    private StatusOrcamentoEntidade statusReprovado;

    @BeforeEach
    void setup() {
        orcamento = MockUtils.idPadrao(new Orcamento());
        orcamento.setSolicitante(MockUtils.criarUsuario(TipoUsuarioEnum.COMUM));

        usuarioGestor = MockUtils.criarUsuario(TipoUsuarioEnum.GESTOR);

        statusAprovado = new StatusOrcamentoEntidade();
        statusAprovado.setId(StatusOrcamentoEnum.APROVADO.getId());
        statusAprovado.setNome("APROVADO");

        statusReprovado = new StatusOrcamentoEntidade();
        statusReprovado.setId(StatusOrcamentoEnum.REPROVADO.getId());
        statusReprovado.setNome("REPROVADO");
    }

    @Test
    void testAprovar() {
        lenient().when(orcamentoRepository.getReferenceById(orcamento.getId())).thenReturn(orcamento);
        lenient().when(statusOrcamentoRepository.findById(StatusOrcamentoEnum.APROVADO.getId()))
                .thenReturn(Optional.of(statusAprovado));

        DadosDetalhamentoOrcamento retorno = orcamentoService.aprovar(orcamento.getId(), usuarioGestor);

        assertEquals(statusAprovado, orcamento.getStatusOrcamentoEntidade());
        assertEquals(usuarioGestor, orcamento.getGestor());
        assertNotNull(orcamento.getDataAnalise());
        assertEquals(LocalDate.now(), orcamento.getDataAnalise());

        assertEquals(statusAprovado.getNome(), retorno.status());
        assertEquals(usuarioGestor.getPessoa().getNome(), retorno.gestorNome());
        assertEquals(orcamento.getDataAnalise(), retorno.dataAnalise());

        verify(orcamentoRepository, times(1)).save(orcamento);
    }

    @Test
    void testCadastrar() {
        DadosCadastroOrcamento dados = new DadosCadastroOrcamento(
                "Fornecedor Teste",
                "Descrição Teste",
                "Pix",
                BigDecimal.valueOf(1000),
                "Observações gerais");

        Usuario usuario = MockUtils.criarUsuario(TipoUsuarioEnum.GESTOR);

        StatusOrcamentoEntidade statusEnviado = new StatusOrcamentoEntidade();
        statusEnviado.setId(StatusOrcamentoEnum.ENVIADO.getId());
        statusEnviado.setNome("ENVIADO");

        when(statusOrcamentoRepository.findById(StatusOrcamentoEnum.ENVIADO.getId()))
                .thenReturn(Optional.of(statusEnviado));

        DadosDetalhamentoOrcamento retorno = orcamentoService.cadastrar(dados, usuario);

        assertEquals(usuario.getPessoa().getNome(), retorno.solicitanteNome());
        assertEquals(statusEnviado.getNome(), retorno.status());
        assertEquals(dados.fornecedor(), retorno.fornecedor());
        assertEquals(dados.descricao(), retorno.descricao());
        assertEquals(dados.formaPagamento(), retorno.formaPagamento());
        assertEquals(dados.valorTotal(), retorno.valorTotal());
        assertEquals(dados.observacoesGerais(), retorno.observacoesGerais());

        verify(orcamentoRepository, times(1)).save(any(Orcamento.class));
    }

    @Test
    void testLiberar() {
        orcamento.setStatusOrcamentoEntidade(statusAprovado);

        lenient().when(statusOrcamentoRepository.findById(StatusOrcamentoEnum.APROVADO.getId()))
                .thenReturn(Optional.of(statusAprovado));
        lenient().when(orcamentoRepository.findByIdAndVerbaLiberadaFalse(orcamento.getId()))
                .thenReturn(Optional.of(orcamento));

        DadosDetalhamentoOrcamento retorno = orcamentoService.liberarVerba(orcamento.getId(), usuarioGestor);

        assertTrue(orcamento.getVerbaLiberada());
        assertEquals(usuarioGestor, orcamento.getTesoureiro());
        assertNotNull(orcamento.getDataLiberacaoVerba());
        assertEquals(LocalDate.now(), orcamento.getDataLiberacaoVerba());

        assertEquals(statusAprovado.getNome(), retorno.status());
        assertEquals(usuarioGestor.getPessoa().getNome(), retorno.tesoureiroNome());
        assertEquals(usuarioGestor, orcamento.getTesoureiro());
        assertEquals(orcamento.getDataLiberacaoVerba(), retorno.dataLiberacaoVerba());
    }

    @Test
    void testListar() {
        Usuario usuarioAdmin = MockUtils.criarUsuario(TipoUsuarioEnum.ADMIN);

        Orcamento orcamento1 = new Orcamento();
        orcamento1.setId(1L);
        orcamento1.setSolicitante(MockUtils.criarUsuario(TipoUsuarioEnum.COMUM));
        orcamento1.setStatusOrcamentoEntidade(statusAprovado);

        Orcamento orcamento2 = new Orcamento();
        orcamento2.setId(2L);
        orcamento2.setSolicitante(MockUtils.criarUsuario(TipoUsuarioEnum.COMUM));
        orcamento2.setStatusOrcamentoEntidade(statusReprovado);

        List<Orcamento> orcamentos = Arrays.asList(orcamento1, orcamento2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Orcamento> orcamentoPage = new PageImpl<>(orcamentos, pageable, orcamentos.size());

        when(orcamentoRepository.findAll(pageable)).thenReturn(orcamentoPage);

        Page<DadosListagemOrcamento> resultado = orcamentoService.listar(pageable, usuarioAdmin, Optional.empty());

        assertNotNull(resultado);
        assertEquals(orcamentos.size(), resultado.getContent().size());
        assertEquals(orcamento1.getId(), resultado.getContent().get(0).id());
        assertEquals(orcamento2.getId(), resultado.getContent().get(1).id());

        verify(orcamentoRepository, times(1)).findAll(pageable);
    }

    @Test
    void testReprovar() {
        lenient().when(orcamentoRepository.getReferenceById(orcamento.getId())).thenReturn(orcamento);
        lenient().when(statusOrcamentoRepository.findById(StatusOrcamentoEnum.REPROVADO.getId()))
                .thenReturn(Optional.of(statusReprovado));

        DadosDetalhamentoOrcamento retorno = orcamentoService.reprovar(orcamento.getId(), usuarioGestor);

        assertEquals(statusReprovado, orcamento.getStatusOrcamentoEntidade());
        assertEquals(usuarioGestor, orcamento.getGestor());
        assertNotNull(orcamento.getDataAnalise());
        assertEquals(LocalDate.now(), orcamento.getDataAnalise());

        assertEquals(statusReprovado.getNome(), retorno.status());
        assertEquals(usuarioGestor.getPessoa().getNome(), retorno.gestorNome());
        assertEquals(orcamento.getDataAnalise(), retorno.dataAnalise());

        verify(orcamentoRepository, times(1)).save(orcamento);
    }
}
