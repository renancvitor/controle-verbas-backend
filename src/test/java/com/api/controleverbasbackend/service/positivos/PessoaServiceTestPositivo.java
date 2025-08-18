package com.api.controleverbasbackend.service.positivos;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.api.controleverbasbackend.domain.entity.cargo.Cargo;
import com.api.controleverbasbackend.domain.entity.departamento.Departamento;
import com.api.controleverbasbackend.domain.entity.pessoa.Pessoa;
import com.api.controleverbasbackend.domain.entity.usuario.Usuario;
import com.api.controleverbasbackend.domain.enums.usuario.TipoUsuarioEnum;
import com.api.controleverbasbackend.dto.pessoa.*;
import com.api.controleverbasbackend.dto.usuario.DadosCadastroUsuario;
import com.api.controleverbasbackend.repository.CargoRepository;
import com.api.controleverbasbackend.repository.DepartamentoRepository;
import com.api.controleverbasbackend.repository.PessoaRepository;
import com.api.controleverbasbackend.service.PessoaService;
import com.api.controleverbasbackend.service.UsuarioService;
import com.api.controleverbasbackend.utils.MockUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class PessoaServiceTestPositivo {

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

    private Usuario usuarioAdmin;
    private Departamento departamento;
    private Cargo cargo;
    private Pessoa pessoa;

    @BeforeEach
    void setUp() {
        cargo = MockUtils.idPadrao(new Cargo());
        cargo.setNome("Dev");

        departamento = MockUtils.idPadrao(new Departamento());
        departamento.setNome("TI");

        pessoa = MockUtils.idPadrao(new Pessoa());
        pessoa.setNome("Fulano");
        pessoa.setCargo(cargo);
        pessoa.setDepartamento(departamento);

        usuarioAdmin = MockUtils.criarUsuarioAdmin();
    }

    @Test
    void testAtivar() {
        pessoa.setAtivo(false);
        when(pessoaRepository.findByIdAndAtivoFalse(1L)).thenReturn(Optional.of(pessoa));

        pessoaService.ativar(1L, usuarioAdmin);

        assertTrue(pessoa.getAtivo());
    }

    @Test
    void testAtualizar() {
        Departamento departamento = MockUtils.idPadrao(new Departamento());
        departamento.setNome("TI");
        Cargo cargo = MockUtils.idPadrao(new Cargo());
        cargo.setNome("Desenvolvedor");

        Pessoa pessoa = MockUtils.idPadrao(new Pessoa());
        pessoa.setNome("Fulano");
        pessoa.setCargo(cargo);
        pessoa.setDepartamento(departamento);

        Pessoa pessoaSpy = Mockito.spy(pessoa);

        doAnswer(invocation -> {
            pessoaSpy.setCargo(cargo);
            pessoaSpy.setDepartamento(departamento);
            return null;
        }).when(pessoaSpy).atualizar(any(DadosAtualizacaoPessoa.class),
                any(CargoRepository.class), any(DepartamentoRepository.class));

        DadosAtualizacaoPessoa dadosAtualizacao = new DadosAtualizacaoPessoa(
                departamento.getId(),
                cargo.getId());

        when(pessoaRepository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.of(pessoaSpy));

        Usuario usuarioAdmin = MockUtils.criarUsuarioAdmin();

        DadosDetalhamentoPessoa resultado = pessoaService.atualizar(1L, dadosAtualizacao, usuarioAdmin);

        assertEquals("Fulano", resultado.nome());
        verify(pessoaRepository).findByIdAndAtivoTrue(1L);
        verify(pessoaSpy).atualizar(dadosAtualizacao, cargoRepository, departamentoRepository);
    }

    @Test
    void testCadastrar() {
        DadosCadastroPessoa dadosPessoa = new DadosCadastroPessoa(
                "Fulano", "12345678900", "email@email.com",
                departamento.getId(), cargo.getId());
        DadosCadastroUsuario dadosUsuario = new DadosCadastroUsuario("12345678900", "senha123",
                TipoUsuarioEnum.COMUM.getId());

        when(departamentoRepository.findByIdAndAtivoTrue(departamento.getId())).thenReturn(Optional.of(departamento));
        when(cargoRepository.findByIdAndAtivoTrue(cargo.getId())).thenReturn(Optional.of(cargo));

        DadosDetalhamentoPessoa resultado = pessoaService.cadastrar(dadosPessoa, dadosUsuario, usuarioAdmin);

        assertNotNull(resultado);
        verify(pessoaRepository).save(any(Pessoa.class));
        verify(usuarioService).cadastrar(any(Pessoa.class), any(DadosCadastroUsuario.class));
    }

    @Test
    void testDeletar() {
        when(pessoaRepository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.of(pessoa));

        pessoaService.deletar(1L, usuarioAdmin);

        assertFalse(pessoa.getAtivo());
    }

    @Test
    void testListarComAtivoTrue() {
        Page<Pessoa> pagina = new PageImpl<>(List.of(pessoa));
        when(pessoaRepository.findAllByAtivo(true, PageRequest.of(0, 10))).thenReturn(pagina);

        Page<DadosListagemPessoa> resultado = pessoaService.listar(PageRequest.of(0, 10), usuarioAdmin, true);

        assertEquals(1, resultado.getTotalElements());
        verify(pessoaRepository).findAllByAtivo(true, PageRequest.of(0, 10));
    }

    @Test
    void testListarSemFiltro() {
        Page<Pessoa> pagina = new PageImpl<>(List.of(pessoa));
        when(pessoaRepository.findAll(PageRequest.of(0, 10))).thenReturn(pagina);

        Page<DadosListagemPessoa> resultado = pessoaService.listar(PageRequest.of(0, 10), usuarioAdmin, null);

        assertEquals(1, resultado.getTotalElements());
        verify(pessoaRepository).findAll(PageRequest.of(0, 10));
    }
}