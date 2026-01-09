package com.kailas.mm.kafkaop;

import com.kailas.mm.model.entity.sql.OutboxAlertEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaOutboxPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaOutboxPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public boolean tryPublish(OutboxAlertEntity entity) {
        try {
            kafkaTemplate.send("alerts", entity.getDcId(), entity.getPayload());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
