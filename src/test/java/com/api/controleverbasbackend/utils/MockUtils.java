package com.api.controleverbasbackend.utils;

import com.api.controleverbasbackend.domain.usuario.TipoUsuarioEntidade;
import com.api.controleverbasbackend.domain.usuario.TipoUsuarioEnum;
import com.api.controleverbasbackend.domain.usuario.Usuario;

public class MockUtils {

    public static Usuario criarUsuarioAdmin() {
        return criarUsuario(TipoUsuarioEnum.ADMIN);
    }

    public static Usuario criarUsuario(TipoUsuarioEnum tipo) {
        TipoUsuarioEntidade tipoUsuario = new TipoUsuarioEntidade();
        tipoUsuario.setId(tipo.getId());

        Usuario usuario = new Usuario();
        usuario.setTipoUsuario(tipoUsuario);
        return usuario;
    }
}
