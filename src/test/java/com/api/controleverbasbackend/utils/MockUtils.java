package com.api.controleverbasbackend.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.api.controleverbasbackend.domain.entity.cargo.Cargo;
import com.api.controleverbasbackend.domain.entity.departamento.Departamento;
import com.api.controleverbasbackend.domain.entity.pessoa.Pessoa;
import com.api.controleverbasbackend.domain.entity.usuario.TipoUsuarioEntidade;
import com.api.controleverbasbackend.domain.entity.usuario.Usuario;
import com.api.controleverbasbackend.domain.enums.usuario.TipoUsuarioEnum;
import com.api.controleverbasbackend.dto.orcamento.DadosCadastroOrcamento;
import com.api.controleverbasbackend.dto.orcamento.DadosDetalhamentoOrcamento;
import com.api.controleverbasbackend.dto.orcamento.DadosListagemOrcamento;
import com.api.controleverbasbackend.dto.pessoa.DadosCadastroPessoa;
import com.api.controleverbasbackend.dto.usuario.DadosCadastroUsuario;

public class MockUtils {

    public static final Long ID = 1L;
    public static final String FORNECEDOR = "Fornecedor X";
    public static final String DESCRICAO = "Compra de materiais";
    public static final String FORMA_PAGTO = "PIX";
    public static final BigDecimal VALOR = new BigDecimal("1234.56");
    public static final String OBS = "Observações";
    public static final String SOLICITANTE = "Alice";
    public static final String GESTOR = "Bruno";
    public static final String TESOUREIRO = "Carla";
    public static final LocalDate HOJE = LocalDate.now();

    public static DadosCadastroOrcamento criarDadosCadastro() {
        return new DadosCadastroOrcamento(FORNECEDOR, DESCRICAO, FORMA_PAGTO, VALOR, OBS);
    }

    public static DadosDetalhamentoOrcamento criarDetalhes(String status) {
        return new DadosDetalhamentoOrcamento(
                ID, FORNECEDOR, DESCRICAO, FORMA_PAGTO, VALOR, OBS,
                SOLICITANTE, GESTOR, TESOUREIRO, status, HOJE, null, "NÃO", null);
    }

    public static DadosListagemOrcamento criarListagem() {
        return new DadosListagemOrcamento(
                ID, FORNECEDOR, DESCRICAO, FORMA_PAGTO, VALOR, OBS,
                SOLICITANTE, GESTOR, TESOUREIRO, "APROVADO", HOJE, HOJE, "SIM", HOJE);
    }

    public static Usuario criarUsuarioAdmin() {
        return criarUsuario(TipoUsuarioEnum.ADMIN);
    }

    public static Usuario criarUsuario(TipoUsuarioEnum tipo) {
        TipoUsuarioEntidade tipoUsuario = new TipoUsuarioEntidade();
        tipoUsuario.setId(tipo.getId());

        Pessoa pessoa = new Pessoa();
        pessoa.setNome(tipo.name() + " Teste");

        Usuario usuario = new Usuario();
        usuario.setTipoUsuario(tipoUsuario);
        usuario.setPessoa(pessoa);

        return usuario;
    }

    public static Pessoa criarPessoaCompleta() {
        Departamento departamento = new Departamento();
        departamento.setNome("Departamento X");

        Cargo cargo = new Cargo();
        cargo.setNome("Cargo X");

        Pessoa pessoa = new Pessoa();
        pessoa.setId(1L);
        pessoa.setNome("Maria");
        pessoa.setCpf("111.111.111-11");
        pessoa.setEmail("email@email.com");
        pessoa.setDepartamento(departamento);
        pessoa.setCargo(cargo);
        pessoa.setDataCadastro(LocalDateTime.now());
        pessoa.setAtivo(true);

        return pessoa;
    }

    public static DadosCadastroPessoa criarDadosCadastroPessoa() {
        return new DadosCadastroPessoa(
                "João Teste",
                "111.111.111-11",
                "joao@email.com",
                1L,
                1L);
    }

    public static DadosCadastroUsuario criarDadosCadastroUsuario() {
        return new DadosCadastroUsuario(
                "111.111.111-11",
                "senha123",
                1);
    }

    public static <T> T idPadrao(T entidade) {
        try {
            entidade.getClass()
                    .getMethod("setId", Long.class)
                    .invoke(entidade, 1L);
            return entidade;
        } catch (Exception e) {
            throw new RuntimeException("A entidade " + entidade.getClass().getSimpleName() + " não possui setId(Long)",
                    e);
        }
    }
}
