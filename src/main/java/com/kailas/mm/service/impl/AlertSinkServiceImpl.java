package com.kailas.mm.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kailas.mm.exception.ItemNotFoundException;
import com.kailas.mm.kafkaop.KafkaOutboxPublisher;
import com.kailas.mm.model.dto.AlertDto;
import com.kailas.mm.model.entity.sql.Item;
import com.kailas.mm.model.entity.sql.OutboxAlertEntity;
import com.kailas.mm.repository.ItemRepository;
import com.kailas.mm.repository.OutboxRepository;
import com.kailas.mm.service.AlertSinkService;
import com.kailas.mm.service.KafkaService;
import com.kailas.mm.utils.HashUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
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

        OutboxAlertEntity entity = new OutboxAlertEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setDcId(alertDto.getDcId());
        entity.setCreatedAt(Instant.now());
        entity.setStatus("RECEIVED");
        Map<String, Object> payload = new HashMap<>();
        payload.put("dcId", alertDto.getDcId());
        payload.put("data", alertDto.getData());
        entity.setPayload(payload);
        outboxRepository.save(entity);
       boolean published = kafkaOutboxPublisher.tryPublish(entity);

        if (published) {
            entity.setStatus("PUBLISHED");
            entity.setLastAttempt(Instant.now());
            outboxRepository.save(entity);
        }

//        List<Map<String, String>> dataList = alertDto.getData();
//        try {
//            saveBatchHash(alertDto);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }

//        List<String> invalidItems = new ArrayList<>();
//
//
//
//        for (Map<String, String> m : dataList) {
//            String itemCode = m.get("itemCode");
//            Optional<Item> optItem = Optional.ofNullable(itemRepository.findByItemCode(itemCode));
//            if (optItem.isPresent()) {
//                m.put("dcId",alertDto.getDcId());
//                kafkaService.sentToKafka("AlertTopic", m);
//            } else {
//                invalidItems.add(itemCode);
//            }
//        }
//        if (!invalidItems.isEmpty()) {
//            throw new ItemNotFoundException("Item Not Found", invalidItems);
//        }

    }

    private void saveBatchHash(AlertDto alertDto) throws JsonProcessingException, NoSuchAlgorithmException {
        List<Map<String, String>> alertDetails = alertDto.getData();
        String dcId = alertDto.getDcId();
        Map<String, String> hashToJson = new LinkedHashMap<>(alertDto.getData().size());
        for (Map<String, String> alert : alertDetails) {
            String alertData = HashUtils.canonicalJson(alert);
            String hash = HashUtils.sha256(dcId + alertData);
            hashToJson.putIfAbsent(hash, alertData);
        }
    }
}
