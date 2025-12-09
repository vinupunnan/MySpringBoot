package com.kailas.mm.service;

import com.kailas.mm.model.dto.ItemDto;

public interface KafkaService {
    void sentToKafka(String topic, Object message);

}
