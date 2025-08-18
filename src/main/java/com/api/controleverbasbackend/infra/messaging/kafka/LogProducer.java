package com.api.controleverbasbackend.infra.messaging.kafka;

import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.api.controleverbasbackend.domain.entity.sistemalog.SistemaLog;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Profile("dev")
@Service
public class LogProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(LogProducer.class);

    public LogProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public void enviarLog(SistemaLog log) {
        try {
            String json = objectMapper.writeValueAsString(log);
            kafkaTemplate.send("sistema-log", json);
        } catch (Exception e) {
            logger.error("Erro ao enviar log para Kafka", e);
        }
    }
}