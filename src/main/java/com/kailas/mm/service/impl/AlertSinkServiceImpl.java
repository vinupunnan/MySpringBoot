package com.kailas.mm.service.impl;

import com.kailas.mm.kafkaop.KafkaOutboxPublisher;
import com.kailas.mm.model.dto.AlertDto;
import com.kailas.mm.model.entity.sql.OutboxAlertEntity;
import com.kailas.mm.repository.ItemRepository;
import com.kailas.mm.repository.OutboxRepository;
import com.kailas.mm.service.AlertSinkService;
import com.kailas.mm.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class AlertSinkServiceImpl implements AlertSinkService {

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    KafkaService kafkaService;

    @Autowired
    OutboxRepository outboxRepository;

    @Autowired
    KafkaOutboxPublisher kafkaOutboxPublisher;

    public void saveAlerts(AlertDto alertDto) {
        Instant originalTimestamp = Instant.now();

        OutboxAlertEntity entity = new OutboxAlertEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setDcId(alertDto.getDcId());
        entity.setCreatedAt(originalTimestamp);
        entity.setStatus("RECEIVED");
        entity.setAttemptCount(0);

        // Include original timestamp in payload for Kafka
        Map<String, Object> payload = new HashMap<>();
        payload.put("dcId", alertDto.getDcId());
        payload.put("data", alertDto.getData());
        payload.put("originalTimestamp", originalTimestamp.toString());
        payload.put("eventTime", originalTimestamp.toEpochMilli());

        entity.setPayload(payload);
        outboxRepository.save(entity);

        boolean published = kafkaOutboxPublisher.tryPublish(entity);

        if (published) {
            entity.setStatus("PUBLISHED");
            entity.setLastAttempt(Instant.now());
            outboxRepository.save(entity);
        } else {
            // Mark attempt even on failure for tracking
            entity.setLastAttempt(Instant.now());
            entity.setAttemptCount(entity.getAttemptCount() + 1);
            outboxRepository.save(entity);
        }

    }

}
