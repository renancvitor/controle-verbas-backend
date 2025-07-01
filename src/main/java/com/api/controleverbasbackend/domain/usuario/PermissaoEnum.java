package com.api.controleverbasbackend.domain.usuario;

public enum PermissaoEnum {
    GERENCIAR_USUARIOS(1),
    REALIZAR_SOLICITAOES(2),
    VISUALIZAR_TODOS_ORCAMENTOS(3),
    APROVAR_REPROVAR_ORCAMENTOS(4);

    private final int id;

    PermissaoEnum(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static PermissaoEnum fromId(int id) {
        for (PermissaoEnum permissaoEnum : values()) {
            if (permissaoEnum.id == id) {
                return permissaoEnum;
            }
        }
        throw new IllegalArgumentException("ID inválido: " + id);
    }
}
