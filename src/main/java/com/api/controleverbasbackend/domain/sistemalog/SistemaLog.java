package com.api.controleverbasbackend.domain.sistemalog;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sistema_logs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SistemaLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo; // INSERT, PRE_UPDATE, POST_UPDATE, DELETE

    private String entidade; // Ex: "Usuario", "Pessoa", etc.

    private String usuario;

    private LocalDateTime dataHora;

    @Column(columnDefinition = "TEXT")
    private String payload; // Estado do objeto em JSON (antes ou depois)

    @Column(columnDefinition = "TEXT")
    private String mensagem; // Ex: "Usuário atualizado com sucesso"

    @PrePersist
    public void prePersist() {
        dataHora = LocalDateTime.now();
    }
}
