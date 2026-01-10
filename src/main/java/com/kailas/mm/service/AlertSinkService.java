package com.kailas.mm.service;

import com.kailas.mm.entity.Alert;
import com.kailas.mm.repository.AlertRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlertSinkService {
    
    private static final Logger logger = LoggerFactory.getLogger(AlertSinkService.class);
    
    @Autowired
    private AlertRepository alertRepository;
    
    @Autowired
    private KafkaOutboxPublisher kafkaOutboxPublisher;
    
    private static final String ALERT_TOPIC = "alerts-topic";
    
    /**
     * Processes an alert by saving it to the database and publishing to Kafka
     * 
     * @param alert The alert to process
     * @return The processed alert with updated status
     */
    @Transactional
    public Alert processAlert(Alert alert) {
        logger.info("Processing alert: type={}, message={}", alert.getAlertType(), alert.getMessage());
        
        // Save alert to database
        Alert savedAlert = alertRepository.save(alert);
        logger.info("Alert saved to database with id: {}", savedAlert.getId());
        
        // Publish alert to Kafka
        boolean kafkaSuccess = kafkaOutboxPublisher.publishMessage(
            ALERT_TOPIC, 
            savedAlert.getId().toString(), 
            savedAlert
        );
        
        if (kafkaSuccess) {
            savedAlert.setStatus("SENT");
            logger.info("Alert published to Kafka successfully: id={}", savedAlert.getId());
        } else {
            savedAlert.setStatus("FAILED");
            logger.error("Failed to publish alert to Kafka: id={}", savedAlert.getId());
        }
        
        // Update alert status
        return alertRepository.save(savedAlert);
    }
    
    /**
     * Processes multiple alerts
     * 
     * @param alerts List of alerts to process
     * @return List of processed alerts with updated statuses
     */
    @Transactional
    public List<Alert> processAlerts(List<Alert> alerts) {
        logger.info("Processing {} alerts", alerts.size());
        
        for (Alert alert : alerts) {
            processAlert(alert);
        }
        
        return alerts;
    }
    
    /**
     * Retrieves all alerts from the database
     * 
     * @return List of all alerts
     */
    public List<Alert> getAllAlerts() {
        return alertRepository.findAll();
    }
    
    /**
     * Retrieves a specific alert by id
     * 
     * @param id The alert id
     * @return The alert if found, null otherwise
     */
    public Alert getAlertById(Long id) {
        return alertRepository.findById(id).orElse(null);
    }
}
