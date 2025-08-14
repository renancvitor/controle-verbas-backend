package com.api.controleverbasbackend.controller.positivo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.api.controleverbasbackend.controller.AutenticacaoController;
import com.api.controleverbasbackend.dto.autenticacao.DadosLogin;
import com.api.controleverbasbackend.dto.autenticacao.DadosTokenJWT;
import com.api.controleverbasbackend.repository.UsuarioRepository;
import com.api.controleverbasbackend.service.AutenticacaoService;
import com.api.controleverbasbackend.service.TokenService;

@WebMvcTest(AutenticacaoController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AutenticacaoControllerTestPositivo {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AutenticacaoService autenticacaoService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    @Test
    void autenticacao_DeveRetornarTokenJWT() throws Exception {
        DadosTokenJWT dadosToken = mock(DadosTokenJWT.class);

        when(autenticacaoService.autenticacao(any(DadosLogin.class), any(AuthenticationManager.class)))
                .thenReturn(dadosToken);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"admin@test.com\",\"senha\":\"Senha@123\"}"))
                .andExpect(status().isOk());

        verify(autenticacaoService).autenticacao(any(DadosLogin.class), any(AuthenticationManager.class));
    }
}
