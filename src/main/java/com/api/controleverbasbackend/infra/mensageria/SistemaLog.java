package com.api.controleverbasbackend.infra.mensageria;

import java.time.LocalDateTime;

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

    private String tipo;

    private String usuario;

    private LocalDateTime dataHora;

    private String mensagem;

    @PrePersist
    public void prePersist() {
        dataHora = LocalDateTime.now();
    }
}
