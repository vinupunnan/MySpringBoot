//package com.kailas.mm.service;
//
//import com.kailas.mm.kafkaop.KafkaOutboxPublisher;
//import com.kailas.mm.model.entity.sql.OutboxAlertEntity;
//import com.kailas.mm.repository.OutboxRepository;
//import io.github.resilience4j.circuitbreaker.CircuitBreaker;
//import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.Instant;
//import java.util.List;
//
//@Service
//public class AlertRecoveryScheduler {
//
//    private static final Logger logger = LoggerFactory.getLogger(AlertRecoveryScheduler.class);
//
//    @Autowired
//    private OutboxRepository outboxRepository;
//
//    @Autowired
//    private KafkaOutboxPublisher kafkaOutboxPublisher;
//
//    @Autowired
//    private CircuitBreakerRegistry circuitBreakerRegistry;
//
//    @Value("${alert.recovery.batch-size:100}")
//    private int batchSize;
//
//    @Value("${alert.recovery.max-attempts:5}")
//    private int maxAttempts;
//
//    @Value("${alert.recovery.throttle-ms:100}")
//    private long throttleDelayMs;
//
//    @Value("${alert.recovery.min-age-seconds:6}")
//    private int minAgeSeconds;
//ObjectMapper mapper = new ObjectMapper();
/// / From a String
//MyClass obj = mapper.readValue(jsonString, MyClass.class);
//// From a File
//MyClass objFromFile = mapper.readValue(new File("data.json"), MyClass.class);
//    /**
//     * Scheduled task that runs every 5 minutes to retry publishing failed alerts
//     *
//     * Key Features:
//     * - Only picks alerts older than minAgeSeconds (6 sec) to avoid race with API
//     * - Checks circuit breaker state before starting
//     * - Stops if circuit opens during processing
//     * - Throttles between messages to avoid overwhelming Kafka
//     * - Preserves original timestamp in Kafka message
//     */
//    @Scheduled(fixedDelay = 300000, initialDelay = 60000) // 5 min = 300,000 ms, 1 min initial delay
//    @Transactional
//    public void recoverFailedAlerts() {
//        // Check circuit breaker state first - don't run if OPEN
//        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("kafkaPublisher");
//        CircuitBreaker.State state = circuitBreaker.getState();
//
//        if (state == CircuitBreaker.State.OPEN) {
//            logger.info("Circuit breaker is OPEN. Skipping alert recovery.");
//            return;
//        }
//
//        logger.info("Starting alert recovery process... (Circuit Breaker: {})", state);
//
//        // Only pick alerts older than minAgeSeconds to avoid race with API
//        Instant threshold = Instant.now().minusSeconds(minAgeSeconds);
//        Pageable pageable = PageRequest.of(0, batchSize);
//
//        List<OutboxAlertEntity> failedAlerts = outboxRepository.findOldFailedAlerts
//                ("RECEIVED", maxAttempts, threshold, pageable);
//
//
//        if (failedAlerts.isEmpty()) {
//            logger.debug("No failed alerts to recover (older than {} seconds)", minAgeSeconds);
//            return;
//        }
//
//        logger.info("Found {} failed alerts to retry (older than {} seconds)",
//                failedAlerts.size(), minAgeSeconds);
//
//        int successCount = 0;
//        int failureCount = 0;
//        int maxedOutCount = 0;
//        int skippedCount = 0;
//
//        for (OutboxAlertEntity entity : failedAlerts) {
//            // Check circuit before each message - stop if opens during recovery
//            if (circuitBreaker.getState() == CircuitBreaker.State.OPEN) {
//                logger.warn("Circuit breaker opened during recovery. Stopping batch to prioritize live traffic.");
//                skippedCount = failedAlerts.size() - (successCount + failureCount + maxedOutCount);
//                break;
//            }
//
//            try {
//                // Original timestamp is already in entity.payload from when it was first saved
//                boolean published = kafkaOutboxPublisher.tryPublish(entity);
//
//                if (published) {
//                    entity.setStatus("PUBLISHED");
//                    entity.setLastAttempt(Instant.now());
//                    outboxRepository.save(entity);
//                    successCount++;
//                    logger.debug("Successfully recovered alert {} (original: {})",
//                            entity.getId(), entity.getCreatedAt());
//
//                    // Throttle between publishes to avoid overwhelming Kafka
//                    if (throttleDelayMs > 0 && (successCount + failureCount + maxedOutCount) < failedAlerts.size()) {
//                        Thread.sleep(throttleDelayMs);
//                    }
//                } else {
//                    // Increment attempt count
//                    entity.setAttemptCount(entity.getAttemptCount() + 1);
//                    entity.setLastAttempt(Instant.now());
//
//                    // Check if max attempts reached
//                    if (entity.getAttemptCount() >= maxAttempts) {
//                        entity.setStatus("FAILED");
//                        maxedOutCount++;
//                        logger.warn("Alert {} has reached max attempts ({}), marking as FAILED. Original time: {}",
//                                entity.getId(), maxAttempts, entity.getCreatedAt());
//                    }
//
//                    outboxRepository.save(entity);
//                    failureCount++;
//                }
//            } catch (InterruptedException e) {
//                logger.warn("Recovery process interrupted: {}", e.getMessage());
//                Thread.currentThread().interrupt();
//                break;
//            } catch (Exception e) {
//                logger.error("Error processing alert {} during recovery: {}",
//                        entity.getId(), e.getMessage());
//
//                // Update attempt count even on unexpected errors
//                entity.setAttemptCount(entity.getAttemptCount() + 1);
//                entity.setLastAttempt(Instant.now());
//                if (entity.getAttemptCount() >= maxAttempts) {
//                    entity.setStatus("FAILED");
//                    maxedOutCount++;
//                }
//                outboxRepository.save(entity);
//                failureCount++;
//            }
//        }
//
//        logger.info("Alert recovery completed. Success: {}, Failed: {}, Maxed Out: {}, Skipped: {}",
//                successCount, failureCount, maxedOutCount, skippedCount);
//
//        if (maxedOutCount > 0) {
//            logger.warn("{} alerts marked as FAILED after {} attempts. Manual intervention may be required.",
//                    maxedOutCount, maxAttempts);
//        }
//    }
//}
