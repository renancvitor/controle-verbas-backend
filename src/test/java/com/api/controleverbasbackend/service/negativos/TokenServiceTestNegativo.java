package com.api.controleverbasbackend.service.negativos;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import com.api.controleverbasbackend.domain.usuario.Usuario;
import com.api.controleverbasbackend.service.TokenService;
import com.api.controleverbasbackend.utils.MockUtils;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class TokenServiceTestNegativo {
    @InjectMocks
    private TokenService tokenService;

    private Usuario usuario;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(tokenService, "secret", "segredoTeste");
        usuario = MockUtils.criarUsuarioAdmin();
        usuario.setId(1L);
        usuario.getPessoa().setEmail("admin@test.com");
    }

    @Test
    void testGetSubject() {
        String tokenInvalido = "tokenInvalido";

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> tokenService.getSubject(tokenInvalido));

        assertTrue(exception.getMessage().contains("Token JWT inválido ou expirado."));
    }
}
