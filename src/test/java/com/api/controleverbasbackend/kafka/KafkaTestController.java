package com.api.controleverbasbackend.kafka;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.controleverbasbackend.domain.entity.sistemalog.SistemaLog;
import com.api.controleverbasbackend.infra.messaging.LogProducer;

@RestController
public class KafkaTestController {

    private final LogProducer logProducer;

    public KafkaTestController(LogProducer logProducer) {
        this.logProducer = logProducer;
    }

    @PostMapping("/kafka/enviar")
    public ResponseEntity<String> enviarMensagem(@RequestParam String mensagem) {
        SistemaLog log = new SistemaLog();
        log.setTipo("INFO");
        log.setUsuario("sistema");
        log.setDataHora(LocalDateTime.now());
        log.setMensagem(mensagem);

        logProducer.enviarLog(log);
        return ResponseEntity.ok("Mensagem enviada para o Kafka: " + mensagem);
    }
}
