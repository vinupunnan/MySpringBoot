package com.kailas.mm.configuration;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaProducerConfig {
    @Bean
   public NewTopic createNewTopic(){

        return  TopicBuilder.name("ItemTopic").partitions(4).replicas(4).build();
    }


}
