package com.api.controleverbasbackend.domain.usuario;

public enum TipoUsuario {

    ADMIN(1),
    GESTOR(2),
    VISUALIZADOR(3),
    COMUM(4);

    private int id;

    TipoUsuario(int i) {
        this.id = id;
    }

    public static TipoUsuario fromId(int id) {
        for (TipoUsuario tipoUsuario : values()) {
            if (tipoUsuario.id == id) {
                return tipoUsuario;
            }
        }
        throw new IllegalArgumentException("ID de tipo de usuário inválido: " + id);
    }
}
