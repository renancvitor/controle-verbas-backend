package com.api.controleverbasbackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import com.api.controleverbasbackend.domain.cargo.Cargo;
import com.api.controleverbasbackend.domain.usuario.TipoUsuarioEntidade;
import com.api.controleverbasbackend.domain.usuario.TipoUsuarioEnum;
import com.api.controleverbasbackend.domain.usuario.Usuario;
import com.api.controleverbasbackend.dto.cargo.DadosAtualizacaoCargo;
import com.api.controleverbasbackend.dto.cargo.DadosCadastroCargo;
import com.api.controleverbasbackend.dto.cargo.DadosDetalhamentoCargo;
import com.api.controleverbasbackend.dto.cargo.DadosListagemCargo;
import com.api.controleverbasbackend.infra.mensageria.kafka.LogProducer;
import com.api.controleverbasbackend.repository.CargoRepository;

@SpringBootTest
@ActiveProfiles("test")
public class CargoServiceTest {

    @MockBean
    private LogProducer logProducer;

    @MockBean
    private CargoRepository cargoRepository;

    @Autowired
    private CargoService cargoService;

    @Test
    void testAtivar() {
        Long cargoId = 1L;

        Cargo cargo = new Cargo();
        cargo.setId(cargoId);
        cargo.setAtivo(false);

        when(cargoRepository.findByIdAndAtivoFalse(cargoId))
                .thenReturn(Optional.of(cargo));

        TipoUsuarioEntidade tipoUsuarioEntidade = new TipoUsuarioEntidade();
        tipoUsuarioEntidade.setId(TipoUsuarioEnum.ADMIN.getId());

        Usuario usuario = new Usuario();
        usuario.setTipoUsuario(tipoUsuarioEntidade);

        cargoService.ativar(cargoId, usuario);

        assertTrue(cargo.getAtivo());
    }

    @Test
    void testAtualizar() {
        Long cargoId = 1L;
        String nomeAtual = "Cargo Antigo";
        String novoNome = "Administrador";

        Cargo cargo = new Cargo();
        cargo.setId(cargoId);
        cargo.setNome(nomeAtual);
        cargo.setAtivo(true);

        when(cargoRepository.findByIdAndAtivoTrue(cargoId))
                .thenReturn(Optional.of(cargo));

        TipoUsuarioEntidade tipoUsuarioEntidade = new TipoUsuarioEntidade();
        tipoUsuarioEntidade.setId(TipoUsuarioEnum.ADMIN.getId());

        Usuario usuario = new Usuario();
        usuario.setTipoUsuario(tipoUsuarioEntidade);

        DadosAtualizacaoCargo dados = new DadosAtualizacaoCargo(novoNome);

        DadosDetalhamentoCargo resultado = cargoService.atualizar(cargoId, dados, usuario);

        assertEquals(novoNome, resultado.nome());
    }

    @Test
    void testCadastrar() {
        TipoUsuarioEntidade tipoUsuarioEntidade = new TipoUsuarioEntidade();
        tipoUsuarioEntidade.setId(TipoUsuarioEnum.ADMIN.getId());

        Usuario usuario = new Usuario();
        usuario.setTipoUsuario(tipoUsuarioEntidade);

        DadosCadastroCargo dados = new DadosCadastroCargo("Administrador");

        DadosDetalhamentoCargo resultado = cargoService.cadastrar(dados, usuario);

        assertNotNull(resultado);
        assertEquals("Administrador", resultado.nome());

        verify(cargoRepository, times(1)).save(any(Cargo.class));
    }

    @Test
    void testDeletar() {
        Long cargoId = 1L;

        Cargo cargo = new Cargo();
        cargo.setId(cargoId);
        cargo.setAtivo(true);

        when(cargoRepository.findByIdAndAtivoTrue(cargoId))
                .thenReturn(Optional.of(cargo));

        TipoUsuarioEntidade tipoUsuarioEntidade = new TipoUsuarioEntidade();
        tipoUsuarioEntidade.setId(TipoUsuarioEnum.ADMIN.getId());

        Usuario usuario = new Usuario();
        usuario.setTipoUsuario(tipoUsuarioEntidade);

        cargoService.deletar(cargoId, usuario);

        assertFalse(cargo.getAtivo());
    }

    @Test
    void testListar() {
        TipoUsuarioEntidade tipoUsuarioEntidade = new TipoUsuarioEntidade();
        tipoUsuarioEntidade.setId(TipoUsuarioEnum.ADMIN.getId());

        Usuario usuario = new Usuario();
        usuario.setTipoUsuario(tipoUsuarioEntidade);

        Cargo cargo1 = new Cargo();
        cargo1.setNome("Analista");

        Cargo cargo2 = new Cargo();
        cargo2.setNome("Desenvolvedor");

        Pageable pageable = PageRequest.of(0, 10);
        List<Cargo> cargosAtivos = List.of(cargo1, cargo2);

        when(cargoRepository.findAllByAtivo(true, pageable))
                .thenReturn(new PageImpl<>(cargosAtivos));

        Page<DadosListagemCargo> resultado = cargoService.listar(pageable, usuario, true);

        assertEquals(2, resultado.getTotalElements());
        assertEquals("Analista", resultado.getContent().get(0).nome());
    }
}
