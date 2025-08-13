package com.api.controleverbasbackend.service.negativos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import com.api.controleverbasbackend.dto.autenticacao.DadosLogin;
import com.api.controleverbasbackend.repository.UsuarioRepository;
import com.api.controleverbasbackend.service.AutenticacaoService;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class AutenticacaoServiceTestNegativo {

    @InjectMocks
    private AutenticacaoService autenticacaoService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    void testAutenticacao() {
        DadosLogin dadosLogin = new DadosLogin("admin@test.com", "senhaErrada");

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(new RuntimeException("Falha de autenticação"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> autenticacaoService.autenticacao(dadosLogin, authenticationManager));

        assertEquals("Falha de autenticação", exception.getMessage());
    }

    @Test
    void testLoadUserByUsername() {
        when(usuarioRepository.findByPessoaEmailAndAtivoTrue("naoexiste@test.com"))
                .thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> autenticacaoService.loadUserByUsername("naoexiste@test.com"));

        assertEquals("Usuário não encontrado.", exception.getMessage());
    }
}
