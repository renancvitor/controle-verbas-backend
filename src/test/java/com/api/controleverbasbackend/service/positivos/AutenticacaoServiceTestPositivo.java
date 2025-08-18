package com.api.controleverbasbackend.service.positivos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import com.api.controleverbasbackend.domain.entity.usuario.Usuario;
import com.api.controleverbasbackend.dto.autenticacao.DadosLogin;
import com.api.controleverbasbackend.dto.autenticacao.DadosTokenJWT;
import com.api.controleverbasbackend.repository.UsuarioRepository;
import com.api.controleverbasbackend.service.AutenticacaoService;
import com.api.controleverbasbackend.service.TokenService;
import com.api.controleverbasbackend.utils.MockUtils;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class AutenticacaoServiceTestPositivo {

    @InjectMocks
    private AutenticacaoService autenticacaoService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    private Usuario usuario;

    @BeforeEach
    void setup() {
        usuario = MockUtils.criarUsuarioAdmin();
        usuario.setId(1L);
        usuario.setSenha("senhaCripto");
        usuario.getPessoa().setEmail("admin@test.com");
    }

    @Test
    void testAutenticacao() {
        DadosLogin dadosLogin = new DadosLogin("admin@test.com", "senha123");

        Authentication authMock = mock(Authentication.class);
        when(authMock.getPrincipal()).thenReturn(usuario);

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authMock);
        when(tokenService.gerarToken(usuario)).thenReturn("jwtSimulado");

        DadosTokenJWT resultado = autenticacaoService.autenticacao(dadosLogin, authenticationManager);

        assertNotNull(resultado);
        assertEquals("jwtSimulado", resultado.token());
        assertEquals(usuario.getId(), resultado.usuario().id());
    }

    @Test
    void testLoadUserByUsername() {
        when(usuarioRepository.findByPessoaEmailAndAtivoTrue("admin@test.com"))
                .thenReturn(Optional.of(usuario));

        UserDetails resultado = autenticacaoService.loadUserByUsername("admin@test.com");

        assertNotNull(resultado);
        assertEquals(usuario.getPessoa().getEmail(), resultado.getUsername());
    }
}
