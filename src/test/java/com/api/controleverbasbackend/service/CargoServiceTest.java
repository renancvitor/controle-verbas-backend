package com.api.controleverbasbackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.api.controleverbasbackend.domain.cargo.Cargo;
import com.api.controleverbasbackend.domain.usuario.TipoUsuarioEntidade;
import com.api.controleverbasbackend.domain.usuario.TipoUsuarioEnum;
import com.api.controleverbasbackend.domain.usuario.Usuario;
import com.api.controleverbasbackend.dto.cargo.DadosAtualizacaoCargo;
import com.api.controleverbasbackend.dto.cargo.DadosDetalhamentoCargo;
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

    }
}
