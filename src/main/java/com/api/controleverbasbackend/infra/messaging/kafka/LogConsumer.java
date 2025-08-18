package com.api.controleverbasbackend.infra.messaging.kafka;

import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.api.controleverbasbackend.domain.entity.sistemalog.SistemaLog;
import com.api.controleverbasbackend.repository.SistemaLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Profile("dev")
@Component
public class LogConsumer {

    private final SistemaLogRepository sistemaLogRepository;
    private final ObjectMapper objectMapper;

    public LogConsumer(SistemaLogRepository sistemaLogRepository) {
        this.sistemaLogRepository = sistemaLogRepository;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @KafkaListener(topics = "sistema-log", groupId = "grupo-controle-verbas")
    public void consumirLog(String mensagem) {
        System.out.println("Mensagem recebida do Kafka: " + mensagem);

        try {
            SistemaLog log = objectMapper.readValue(mensagem, SistemaLog.class);
            sistemaLogRepository.save(log);
        } catch (Exception e) {
            System.err.println("Erro ao processar log Kafka: " + e.getMessage());
        }
    }
}