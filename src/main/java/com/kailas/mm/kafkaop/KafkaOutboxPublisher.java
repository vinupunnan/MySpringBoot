package com.kailas.mm.kafkaop;

import com.kailas.mm.model.entity.sql.OutboxAlertEntity;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaOutboxPublisher {
    private static final Logger logger = LoggerFactory.getLogger(KafkaOutboxPublisher.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaOutboxPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @CircuitBreaker(name = "kafkaPublisher", fallbackMethod = "fallbackPublish")
    public boolean tryPublish(OutboxAlertEntity entity) {
        try {
            kafkaTemplate.send("alerts", entity.getDcId(), entity.getPayload()).get();
            logger.debug("Successfully published alert {} to Kafka", entity.getId());
            return true;
        } catch (Exception e) {
            logger.error("Failed to publish alert {} to Kafka: {}", entity.getId(), e.getMessage());
            throw new RuntimeException("Kafka publishing failed", e);
        }
    }

    private boolean fallbackPublish(OutboxAlertEntity entity, Exception e) {
        logger.warn("Circuit breaker activated for alert {}. Kafka is likely down.", entity.getId());
        return false;
    }
}
