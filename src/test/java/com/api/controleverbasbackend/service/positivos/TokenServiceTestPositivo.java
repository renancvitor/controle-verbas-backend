package com.api.controleverbasbackend.service.positivos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import com.api.controleverbasbackend.domain.entity.usuario.Usuario;
import com.api.controleverbasbackend.service.TokenService;
import com.api.controleverbasbackend.utils.MockUtils;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class TokenServiceTestPositivo {

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
    void testGerarToken() {
        String token = tokenService.gerarToken(usuario);
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void testGetSubject() {
        String token = tokenService.gerarToken(usuario);
        String subject = tokenService.getSubject(token);
        assertEquals(usuario.getPessoa().getEmail(), subject);
    }
}
