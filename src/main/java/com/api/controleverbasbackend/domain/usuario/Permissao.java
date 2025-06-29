package com.api.controleverbasbackend.domain.usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "permissoes")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Permissao {

    @Id
    private Integer id;

    @Column(nullable = false, unique = true)
    private String nome;

    public static Permissao fromEnum(PermissaoEnum permissaoEnum) {
        return new Permissao(permissaoEnum.getId(), permissaoEnum.name());
    }
}
