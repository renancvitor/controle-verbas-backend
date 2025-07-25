package com.api.controleverbasbackend.domain.usuario;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.api.controleverbasbackend.domain.pessoa.Pessoa;
import com.api.controleverbasbackend.dto.usuario.DadosAtualizacaoUsuarioTipo;
import com.api.controleverbasbackend.repository.TipoUsuarioRepository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "usuarios")
@Entity(name = "Usuario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String senha;

    @OneToOne(optional = false)
    @JoinColumn(name = "pessoa_id", nullable = false, unique = true)
    private Pessoa pessoa;

    @OneToOne(optional = false)
    @JoinColumn(name = "tipo_usuario_id", nullable = false)
    private TipoUsuarioEntidade tipoUsuario;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(nullable = false, name = "primeiro_acesso")
    private Boolean primeiroAcesso = true;

    public Usuario(String senha, Pessoa pessoa, TipoUsuarioEntidade tipoUsuario) {
        this.senha = senha;
        this.pessoa = pessoa;
        this.tipoUsuario = tipoUsuario;
        this.ativo = true;
        this.primeiroAcesso = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return pessoa.getEmail();
    }

    public void atualizarSenha(String senhaCriptografada) {
        if (senhaCriptografada != null && !senhaCriptografada.isBlank()) {
            this.senha = senhaCriptografada;
        }
    }

    public void atualizarUsuarioTipo(DadosAtualizacaoUsuarioTipo dados,
            TipoUsuarioRepository tipoUsuarioRepository) {
        if (dados.idTipoUsuario() != null) {
            this.tipoUsuario = tipoUsuarioRepository.findById(dados.idTipoUsuario())
                    .orElseThrow(() -> new RuntimeException("Tipo de usuário não encontrado."));
        }
    }
}
