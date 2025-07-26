package com.api.controleverbasbackend.domain.usuario;

public enum TipoUsuarioEnum {

    ADMIN(1),
    GESTOR(2),
    TESOUREIRO(3),
    COMUM(4),
    TESTER(5);

    private final int id;

    TipoUsuarioEnum(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static TipoUsuarioEnum fromId(int id) {
        for (TipoUsuarioEnum tipoUsuario : values()) {
            if (tipoUsuario.id == id) {
                return tipoUsuario;
            }
        }
        throw new IllegalArgumentException("ID inválido: " + id);
    }
}
