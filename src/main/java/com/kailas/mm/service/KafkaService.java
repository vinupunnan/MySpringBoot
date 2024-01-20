package com.kailas.mm.service;

import com.kailas.mm.model.dto.ItemDto;

public interface KafkaService {
    void sentToKafka(ItemDto itemDto);
}
