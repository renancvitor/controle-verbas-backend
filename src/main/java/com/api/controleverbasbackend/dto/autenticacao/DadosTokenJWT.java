package com.api.controleverbasbackend.dto.autenticacao;

import com.api.controleverbasbackend.dto.usuario.DadosResumidoUsuario;

public record DadosTokenJWT(String token, DadosResumidoUsuario usuario) {
}
