package com.api.controleverbasbackend.service.positivos;

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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.api.controleverbasbackend.domain.cargo.Cargo;
import com.api.controleverbasbackend.domain.usuario.Usuario;
import com.api.controleverbasbackend.dto.cargo.DadosAtualizacaoCargo;
import com.api.controleverbasbackend.dto.cargo.DadosCadastroCargo;
import com.api.controleverbasbackend.dto.cargo.DadosDetalhamentoCargo;
import com.api.controleverbasbackend.dto.cargo.DadosListagemCargo;
import com.api.controleverbasbackend.infra.mensageria.kafka.LogProducer;
import com.api.controleverbasbackend.repository.CargoRepository;
import com.api.controleverbasbackend.service.CargoService;
import com.api.controleverbasbackend.utils.MockUtils;

@SpringBootTest
@ActiveProfiles("test")
public class CargoServiceTestPositivo {

    @MockitoBean
    private LogProducer logProducer;

    @MockitoBean
    private CargoRepository cargoRepository;

    @Autowired
    private CargoService cargoService;

    @Test
    void testAtivar() {
        Cargo cargo = MockUtils.idPadrao(new Cargo());
        cargo.setAtivo(false);

        Usuario usuario = MockUtils.criarUsuarioAdmin();

        when(cargoRepository.findByIdAndAtivoFalse(cargo.getId()))
                .thenReturn(Optional.of(cargo));

        cargoService.ativar(cargo.getId(), usuario);

        assertTrue(cargo.getAtivo());
    }

    @Test
    void testAtualizar() {
        String nomeAtual = "Cargo Antigo";
        String novoNome = "Administrador";

        Cargo cargo = MockUtils.idPadrao(new Cargo());
        cargo.setAtivo(false);
        cargo.setNome(nomeAtual);
        cargo.setAtivo(true);

        Usuario usuario = MockUtils.criarUsuarioAdmin();

        when(cargoRepository.findByIdAndAtivoTrue(cargo.getId()))
                .thenReturn(Optional.of(cargo));

        DadosAtualizacaoCargo dados = new DadosAtualizacaoCargo(novoNome);

        DadosDetalhamentoCargo resultado = cargoService.atualizar(cargo.getId(), dados, usuario);

        assertEquals(novoNome, resultado.nome());
    }

    @Test
    void testCadastrar() {
        Usuario usuario = MockUtils.criarUsuarioAdmin();

        DadosCadastroCargo dados = new DadosCadastroCargo("Administrador");

        DadosDetalhamentoCargo resultado = cargoService.cadastrar(dados, usuario);

        assertNotNull(resultado);
        assertEquals("Administrador", resultado.nome());

        verify(cargoRepository, times(1)).save(any(Cargo.class));
    }

    @Test
    void testDeletar() {
        Cargo cargo = MockUtils.idPadrao(new Cargo());
        cargo.setAtivo(true);

        when(cargoRepository.findByIdAndAtivoTrue(cargo.getId()))
                .thenReturn(Optional.of(cargo));

        Usuario usuario = MockUtils.criarUsuarioAdmin();

        cargoService.deletar(cargo.getId(), usuario);

        assertFalse(cargo.getAtivo());
    }

    @Test
    void testListar() {
        Usuario usuario = MockUtils.criarUsuarioAdmin();

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
