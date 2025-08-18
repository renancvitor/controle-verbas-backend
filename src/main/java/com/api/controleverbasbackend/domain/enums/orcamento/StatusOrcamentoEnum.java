package com.api.controleverbasbackend.domain.enums.orcamento;

public enum StatusOrcamentoEnum {
    ENVIADO(1),
    APROVADO(2),
    REPROVADO(3);

    private final int id;

    StatusOrcamentoEnum(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static StatusOrcamentoEnum fromId(int id) {
        for (StatusOrcamentoEnum statusOrcamentoEnum : values()) {
            if (statusOrcamentoEnum.id == id) {
                return statusOrcamentoEnum;
            }
        }
        throw new IllegalArgumentException("ID inválido: " + id);
    }
}
