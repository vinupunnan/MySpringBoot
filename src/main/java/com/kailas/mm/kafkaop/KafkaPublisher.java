package com.kailas.mm.kafkaop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;


@Service
public class KafkaPublisher {

    @Autowired
   private KafkaTemplate<String ,Object> kafkaTemplate;
    public <T> void publishToTopic(String topicName, String key, T value) throws ExecutionException, InterruptedException {
        kafkaTemplate.send(topicName, key, value);
    }
}
