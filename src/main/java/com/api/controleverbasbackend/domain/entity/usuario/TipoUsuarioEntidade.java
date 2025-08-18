package com.api.controleverbasbackend.domain.entity.usuario;

import java.util.HashSet;
import java.util.Set;

import com.api.controleverbasbackend.domain.enums.usuario.TipoUsuarioEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tipos_usuarios")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class TipoUsuarioEntidade {

    @Id
    private Integer id;

    @Column(nullable = false, unique = true)
    private String nome;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tipos_usuarios_permissoes", joinColumns = @JoinColumn(name = "tipo_usuario_id"), inverseJoinColumns = @JoinColumn(name = "permissao_id"))
    private Set<PermissaoEntidade> permissoes;

    public static TipoUsuarioEntidade fromEnum(TipoUsuarioEnum tipoUsuarioEnum) {
        return new TipoUsuarioEntidade(tipoUsuarioEnum.getId(), tipoUsuarioEnum.name(), new HashSet<>());
    }
}
