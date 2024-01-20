package com.kailas.mm.service.impl;

import com.kailas.mm.model.dto.ItemDto;
import com.kailas.mm.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaServiceImpl implements KafkaService {
  //  @Autowired
   // private KafkaTemplate kafkaTemplate;
    @Override
    public void sentToKafka(ItemDto itemDto) {

//            kafkaTemplate.send("ItemTopic", itemDto);

    }
}
