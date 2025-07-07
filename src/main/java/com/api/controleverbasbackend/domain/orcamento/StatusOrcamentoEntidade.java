package com.api.controleverbasbackend.domain.orcamento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "status_orcamentos")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class StatusOrcamentoEntidade {

    @Id
    private Integer id;

    @Column(nullable = false, unique = true)
    private String nome;

    public static StatusOrcamentoEntidade fromEnum(StatusOrcamentoEnum statusOrcamentoEnum) {
        return new StatusOrcamentoEntidade(statusOrcamentoEnum.getId(), statusOrcamentoEnum.name());
    }
}
