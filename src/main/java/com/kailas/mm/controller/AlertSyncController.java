package com.kailas.mm.controller;

import com.kailas.mm.common.BaseResponse;
import com.kailas.mm.entity.Alert;
import com.kailas.mm.entity.FailedKafkaMessage;
import com.kailas.mm.repository.FailedKafkaMessageRepository;
import com.kailas.mm.service.AlertSinkService;
import com.kailas.mm.service.KafkaOutboxPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertSyncController {
    
    private static final Logger logger = LoggerFactory.getLogger(AlertSyncController.class);
    
    @Autowired
    private AlertSinkService alertSinkService;
    
    @Autowired
    private KafkaOutboxPublisher kafkaOutboxPublisher;
    
    @Autowired
    private FailedKafkaMessageRepository failedKafkaMessageRepository;
    
    /**
     * Synchronizes a single alert
     * 
     * @param alert The alert to sync
     * @return Response with the processed alert
     */
    @PostMapping("/sync")
    public ResponseEntity<BaseResponse> syncAlert(@RequestBody Alert alert) {
        logger.info("Received request to sync alert: type={}", alert.getAlertType());
        
        try {
            Alert processedAlert = alertSinkService.processAlert(alert);
            
            BaseResponse response = new BaseResponse(
                HttpStatus.OK.value(),
                "Alert synced successfully",
                processedAlert,
                null
            );
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            logger.error("Error syncing alert", e);
            
            BaseResponse response = new BaseResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Failed to sync alert",
                null,
                e.getMessage()
            );
            
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Synchronizes multiple alerts
     * 
     * @param alerts List of alerts to sync
     * @return Response with the processed alerts
     */
    @PostMapping("/sync/batch")
    public ResponseEntity<BaseResponse> syncAlerts(@RequestBody List<Alert> alerts) {
        logger.info("Received request to sync {} alerts", alerts.size());
        
        try {
            List<Alert> processedAlerts = alertSinkService.processAlerts(alerts);
            
            long successCount = processedAlerts.stream()
                .filter(a -> "SENT".equals(a.getStatus()))
                .count();
            long failedCount = processedAlerts.stream()
                .filter(a -> "FAILED".equals(a.getStatus()))
                .count();
            
            String message = String.format(
                "Processed %d alerts: %d successful, %d failed",
                processedAlerts.size(), successCount, failedCount
            );
            
            BaseResponse response = new BaseResponse(
                HttpStatus.OK.value(),
                message,
                processedAlerts,
                null
            );
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            logger.error("Error syncing alerts", e);
            
            BaseResponse response = new BaseResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Failed to sync alerts",
                null,
                e.getMessage()
            );
            
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Gets all alerts
     * 
     * @return Response with all alerts
     */
    @GetMapping("/all")
    public ResponseEntity<BaseResponse> getAllAlerts() {
        logger.info("Received request to get all alerts");
        
        try {
            List<Alert> alerts = alertSinkService.getAllAlerts();
            
            BaseResponse response = new BaseResponse(
                HttpStatus.OK.value(),
                "Retrieved all alerts",
                alerts,
                null
            );
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            logger.error("Error retrieving alerts", e);
            
            BaseResponse response = new BaseResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Failed to retrieve alerts",
                null,
                e.getMessage()
            );
            
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Gets all failed Kafka messages
     * 
     * @return Response with all failed messages
     */
    @GetMapping("/failed-messages")
    public ResponseEntity<BaseResponse> getFailedMessages() {
        logger.info("Received request to get failed Kafka messages");
        
        try {
            List<FailedKafkaMessage> failedMessages = failedKafkaMessageRepository.findAll();
            
            BaseResponse response = new BaseResponse(
                HttpStatus.OK.value(),
                String.format("Found %d failed messages", failedMessages.size()),
                failedMessages,
                null
            );
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            logger.error("Error retrieving failed messages", e);
            
            BaseResponse response = new BaseResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Failed to retrieve failed messages",
                null,
                e.getMessage()
            );
            
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Retries a specific failed message
     * 
     * @param messageId The id of the failed message to retry
     * @return Response indicating success or failure
     */
    @PostMapping("/failed-messages/{messageId}/retry")
    public ResponseEntity<BaseResponse> retryFailedMessage(@PathVariable Long messageId) {
        logger.info("Received request to retry failed message: id={}", messageId);
        
        try {
            FailedKafkaMessage failedMessage = failedKafkaMessageRepository.findById(messageId)
                .orElse(null);
            
            if (failedMessage == null) {
                BaseResponse response = new BaseResponse(
                    HttpStatus.NOT_FOUND.value(),
                    "Failed message not found",
                    null,
                    null
                );
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            
            boolean success = kafkaOutboxPublisher.retryFailedMessage(failedMessage);
            
            String message = success ? 
                "Message retried successfully" : 
                "Message retry failed, recorded for future retry";
            
            BaseResponse response = new BaseResponse(
                HttpStatus.OK.value(),
                message,
                null,
                null
            );
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            logger.error("Error retrying failed message", e);
            
            BaseResponse response = new BaseResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Failed to retry message",
                null,
                e.getMessage()
            );
            
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Retries all failed messages
     * 
     * @return Response with retry results
     */
    @PostMapping("/failed-messages/retry-all")
    public ResponseEntity<BaseResponse> retryAllFailedMessages() {
        logger.info("Received request to retry all failed messages");
        
        try {
            List<FailedKafkaMessage> failedMessages = failedKafkaMessageRepository.findAll();
            
            int successCount = 0;
            int failedCount = 0;
            
            for (FailedKafkaMessage failedMessage : failedMessages) {
                boolean success = kafkaOutboxPublisher.retryFailedMessage(failedMessage);
                if (success) {
                    successCount++;
                } else {
                    failedCount++;
                }
            }
            
            String message = String.format(
                "Retry completed: %d successful, %d failed",
                successCount, failedCount
            );
            
            BaseResponse response = new BaseResponse(
                HttpStatus.OK.value(),
                message,
                null,
                null
            );
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            logger.error("Error retrying all failed messages", e);
            
            BaseResponse response = new BaseResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Failed to retry messages",
                null,
                e.getMessage()
            );
            
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
