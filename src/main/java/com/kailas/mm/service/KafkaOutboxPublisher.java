package com.kailas.mm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kailas.mm.entity.FailedKafkaMessage;
import com.kailas.mm.repository.FailedKafkaMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.time.LocalDateTime;

@Service
public class KafkaOutboxPublisher {
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaOutboxPublisher.class);
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    @Autowired
    private FailedKafkaMessageRepository failedKafkaMessageRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    /**
     * Publishes a message to Kafka topic and records failure if unsuccessful
     * 
     * @param topic The Kafka topic to publish to
     * @param key The message key
     * @param message The message object to publish
     * @return true if message was sent successfully, false otherwise
     */
    public boolean publishMessage(String topic, String key, Object message) {
        try {
            String messagePayload = objectMapper.writeValueAsString(message);
            return publishMessage(topic, key, messagePayload);
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize message for topic: {}, key: {}", topic, key, e);
            recordFailedMessage(topic, key, message.toString(), "Serialization error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Publishes a string message to Kafka topic and records failure if unsuccessful
     * 
     * @param topic The Kafka topic to publish to
     * @param key The message key
     * @param messagePayload The message payload as string
     * @return true if message was sent successfully, false otherwise
     */
    public boolean publishMessage(String topic, String key, String messagePayload) {
        final boolean[] success = {true};
        
        try {
            ListenableFuture<SendResult<String, String>> future = 
                kafkaTemplate.send(topic, key, messagePayload);
            
            future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
                @Override
                public void onSuccess(SendResult<String, String> result) {
                    logger.info("Message sent successfully to topic: {}, key: {}, offset: {}", 
                        topic, key, result.getRecordMetadata().offset());
                }
                
                @Override
                public void onFailure(Throwable ex) {
                    logger.error("Failed to send message to topic: {}, key: {}", topic, key, ex);
                    recordFailedMessage(topic, key, messagePayload, ex.getMessage());
                    success[0] = false;
                }
            });
            
            // Wait for the result to ensure we know if it failed
            future.get();
            return success[0];
            
        } catch (Exception e) {
            logger.error("Exception while sending message to topic: {}, key: {}", topic, key, e);
            recordFailedMessage(topic, key, messagePayload, e.getMessage());
            return false;
        }
    }
    
    /**
     * Records a failed Kafka message in the database
     * 
     * @param topic The Kafka topic
     * @param key The message key
     * @param messagePayload The message payload
     * @param errorMessage The error message
     */
    private void recordFailedMessage(String topic, String key, String messagePayload, String errorMessage) {
        try {
            FailedKafkaMessage failedMessage = new FailedKafkaMessage(
                topic, key, messagePayload, errorMessage
            );
            failedKafkaMessageRepository.save(failedMessage);
            logger.info("Recorded failed message for topic: {}, key: {} in database", topic, key);
        } catch (Exception e) {
            logger.error("Failed to record failed message in database for topic: {}, key: {}", 
                topic, key, e);
        }
    }
    
    /**
     * Retries sending a failed message
     * 
     * @param failedMessage The failed message to retry
     * @return true if retry was successful, false otherwise
     */
    public boolean retryFailedMessage(FailedKafkaMessage failedMessage) {
        boolean success = publishMessage(
            failedMessage.getTopic(), 
            failedMessage.getMessageKey(), 
            failedMessage.getMessagePayload()
        );
        
        if (success) {
            // Delete the failed message record as it's now successful
            failedKafkaMessageRepository.delete(failedMessage);
            logger.info("Successfully retried and deleted failed message: {}", failedMessage.getId());
        } else {
            // Update retry count and last retry time
            failedMessage.setRetryCount(failedMessage.getRetryCount() + 1);
            failedMessage.setLastRetryAt(LocalDateTime.now());
            failedKafkaMessageRepository.save(failedMessage);
            logger.warn("Retry failed for message: {}, retry count: {}", 
                failedMessage.getId(), failedMessage.getRetryCount());
        }
        
        return success;
    }
}
