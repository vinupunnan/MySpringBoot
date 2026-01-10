package com.kailas.mm.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "failed_kafka_messages")
public class FailedKafkaMessage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "topic")
    private String topic;
    
    @Column(name = "message_key")
    private String messageKey;
    
    @Column(name = "message_payload", columnDefinition = "TEXT")
    private String messagePayload;
    
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
    
    @Column(name = "retry_count")
    private Integer retryCount;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "last_retry_at")
    private LocalDateTime lastRetryAt;
    
    public FailedKafkaMessage() {
        this.createdAt = LocalDateTime.now();
        this.retryCount = 0;
    }
    
    public FailedKafkaMessage(String topic, String messageKey, String messagePayload, String errorMessage) {
        this.topic = topic;
        this.messageKey = messageKey;
        this.messagePayload = messagePayload;
        this.errorMessage = errorMessage;
        this.createdAt = LocalDateTime.now();
        this.retryCount = 0;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTopic() {
        return topic;
    }
    
    public void setTopic(String topic) {
        this.topic = topic;
    }
    
    public String getMessageKey() {
        return messageKey;
    }
    
    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }
    
    public String getMessagePayload() {
        return messagePayload;
    }
    
    public void setMessagePayload(String messagePayload) {
        this.messagePayload = messagePayload;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public Integer getRetryCount() {
        return retryCount;
    }
    
    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getLastRetryAt() {
        return lastRetryAt;
    }
    
    public void setLastRetryAt(LocalDateTime lastRetryAt) {
        this.lastRetryAt = lastRetryAt;
    }
}
