package com.api.controleverbasbackend.service.positivos;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.api.controleverbasbackend.domain.departamento.Departamento;
import com.api.controleverbasbackend.domain.usuario.TipoUsuarioEntidade;
import com.api.controleverbasbackend.domain.usuario.TipoUsuarioEnum;
import com.api.controleverbasbackend.domain.usuario.Usuario;
import com.api.controleverbasbackend.infra.mensageria.kafka.LogProducer;
import com.api.controleverbasbackend.repository.DepartamentoRepository;
import com.api.controleverbasbackend.service.DepartamentoService;

@SpringBootTest
@ActiveProfiles("test")
public class DepartamentoServiceTestPositivo {

    @MockBean
    private LogProducer logProducer;

    @MockBean
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private DepartamentoService departamentoService;

    @Test
    void testAtivar() {
        Long departamentoId = 1L;

        Departamento departamento = new Departamento();
        departamento.setId(departamentoId);
        departamento.setAtivo(false);

        when(departamentoRepository.findByIdAndAtivoFalse(departamentoId))
                .thenReturn(Optional.of(departamento));

        TipoUsuarioEntidade tipoUsuarioEntidade = new TipoUsuarioEntidade();
        tipoUsuarioEntidade.setId(TipoUsuarioEnum.ADMIN.getId());

        Usuario usuario = new Usuario();
        usuario.setTipoUsuario(tipoUsuarioEntidade);

        departamentoService.ativar(departamentoId, usuario);

        assertTrue(departamento.getAtivo());
    }

    @Test
    void testAtualizar() {

    }

    @Test
    void testCadastrar() {

    }

    @Test
    void testDeletar() {

    }

    @Test
    void testListar() {

    }
}
