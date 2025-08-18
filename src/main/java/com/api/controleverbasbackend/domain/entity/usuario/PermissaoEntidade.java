package com.api.controleverbasbackend.domain.entity.usuario;

import com.api.controleverbasbackend.domain.enums.usuario.PermissaoEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "permissoes")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class PermissaoEntidade {

    @Id
    private Integer id;

    @Column(nullable = false, unique = true)
    private String nome;

    public static PermissaoEntidade fromEnum(PermissaoEnum permissaoEnum) {
        return new PermissaoEntidade(permissaoEnum.getId(), permissaoEnum.name());
    }
}
