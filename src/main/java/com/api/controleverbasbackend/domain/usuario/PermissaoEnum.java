package com.api.controleverbasbackend.domain.usuario;

public enum PermissaoEnum {
    GERENCIAR_USUARIOS(1),
    REALIZAR_SOLICITAOES(2),
    VISUALIZAR_TODAS_SOLICITACOES(3),
    APROVAR_REPROVAR_SOLICITACOES(4);

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
