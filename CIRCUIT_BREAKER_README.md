# Circuit Breaker Implementation for Kafka Alert Publishing

## Overview

This implementation adds a **Circuit Breaker pattern** to handle Kafka unavailability gracefully when publishing alerts. It ensures **zero message loss** by using an outbox pattern with automatic retry mechanism.

## Key Features

1. **Circuit Breaker Protection**: Automatically detects Kafka failures and opens circuit to prevent cascading failures
2. **Outbox Pattern**: All alerts are saved to database before attempting Kafka publish
3. **Automatic Recovery**: Scheduled job retries failed alerts every 5 minutes in batches
4. **Batch Processing**: Processes 100 alerts at a time to avoid overwhelming Kafka on recovery
5. **Max Retry Limit**: Alerts are marked as FAILED after 10 attempts to prevent infinite retries
6. **Comprehensive Logging**: Full visibility into circuit state and recovery operations

## Architecture

### Flow Diagram

```
Alert Received
    ↓
Save to Database (Status: RECEIVED)
    ↓
Try Publish to Kafka (with Circuit Breaker)
    ↓
   Success?
   ↙     ↘
 YES      NO
  ↓        ↓
Update    Update
Status:   lastAttempt
PUBLISHED attemptCount++
          Status: RECEIVED
          
          ↓ (Every 5 minutes)
          
    Scheduler finds RECEIVED alerts
          ↓
    Retry in batches of 100
          ↓
       Success?
       ↙     ↘
     YES      NO
      ↓        ↓
   Status:   attemptCount++
  PUBLISHED  (if count >= 10)
             Status: FAILED
```

## Configuration

### Circuit Breaker Settings (`application-dev.properties`)

```properties
# Circuit Breaker Configuration for Kafka
resilience4j.circuitbreaker.instances.kafkaPublisher.sliding-window-type=COUNT_BASED
resilience4j.circuitbreaker.instances.kafkaPublisher.sliding-window-size=10
resilience4j.circuitbreaker.instances.kafkaPublisher.minimum-number-of-calls=5
resilience4j.circuitbreaker.instances.kafkaPublisher.failure-rate-threshold=50
resilience4j.circuitbreaker.instances.kafkaPublisher.wait-duration-in-open-state=60s
resilience4j.circuitbreaker.instances.kafkaPublisher.permitted-number-of-calls-in-half-open-state=5
resilience4j.circuitbreaker.instances.kafkaPublisher.automatic-transition-from-open-to-half-open-enabled=true
resilience4j.circuitbreaker.instances.kafkaPublisher.register-health-indicator=true

# Alert Recovery Scheduler Configuration
alert.recovery.batch-size=100
alert.recovery.max-attempts=10
```

### Circuit Breaker States

- **CLOSED**: Normal operation, all calls go through
- **OPEN**: Kafka is down, circuit is open, calls fail fast via fallback
- **HALF_OPEN**: Testing if Kafka recovered, limited calls allowed

### Thresholds

- **Failure Rate**: 50% failures out of last 10 calls opens circuit
- **Wait Duration**: Circuit stays open for 60 seconds before trying again
- **Half-Open Calls**: Allows 5 test calls to verify Kafka recovery

## Database Schema

### Updated `OutboxAlertEntity` Table

```sql
CREATE TABLE alerts_outbox (
    id VARCHAR(255) PRIMARY KEY,
    dc_id VARCHAR(255),
    payload JSON,
    status VARCHAR(50),          -- RECEIVED, PUBLISHED, FAILED
    created_at TIMESTAMP,
    last_attempt TIMESTAMP,
    attempt_count INT DEFAULT 0  -- New field for retry tracking
);
```

## Components

### 1. KafkaOutboxPublisher
- Wraps Kafka send with `@CircuitBreaker` annotation
- Implements fallback method for when circuit is open
- Provides detailed logging of publish attempts

### 2. AlertSinkServiceImpl
- Saves alert to database with status "RECEIVED"
- Attempts immediate publish via circuit breaker
- Updates status to "PUBLISHED" on success
- Tracks `lastAttempt` and `attemptCount` on failure

### 3. AlertRecoveryScheduler
- Runs every 5 minutes (`@Scheduled(fixedDelay = 300000)`)
- Queries alerts with status "RECEIVED" and attemptCount < 10
- Processes in batches of 100
- Updates status to "PUBLISHED" on success
- Marks as "FAILED" after 10 attempts

### 4. OutboxRepository
- Added `findByStatusAndAttemptCountLessThan()` method
- Supports `Pageable` for batch retrieval

## Monitoring

### Actuator Health Endpoint

Circuit breaker state is exposed via Spring Boot Actuator:

```bash
GET http://localhost:9292/actuator/health/circuitBreakers
```

Response:
```json
{
  "status": "UP",
  "details": {
    "kafkaPublisher": {
      "status": "UP",
      "details": {
        "state": "CLOSED",
        "failureRate": "0.0%",
        "slowCallRate": "0.0%"
      }
    }
  }
}
```

### Log Messages

**Successful publish:**
```
DEBUG KafkaOutboxPublisher - Successfully published alert abc-123 to Kafka
```

**Circuit breaker activated:**
```
WARN KafkaOutboxPublisher - Circuit breaker activated for alert abc-123. Kafka is likely down.
```

**Recovery process:**
```
INFO AlertRecoveryScheduler - Starting alert recovery process...
INFO AlertRecoveryScheduler - Found 150 failed alerts to retry
INFO AlertRecoveryScheduler - Alert recovery completed. Success: 100, Failed: 50, Maxed Out: 0
```

**Max attempts reached:**
```
WARN AlertRecoveryScheduler - Alert abc-123 has reached max attempts (10), marking as FAILED
```

## Testing

### Simulate Kafka Failure

1. Stop Kafka broker
2. Send alerts via API
3. Observe alerts saved with status "RECEIVED"
4. Check circuit breaker opens after threshold

### Verify Recovery

1. Start Kafka broker
2. Wait 5 minutes for scheduler
3. Check logs for recovery process
4. Verify alerts moved to "PUBLISHED" status

### Query Failed Alerts

```sql
-- Find all pending alerts
SELECT * FROM alerts_outbox WHERE status = 'RECEIVED';

-- Find alerts nearing max attempts
SELECT * FROM alerts_outbox WHERE status = 'RECEIVED' AND attempt_count >= 8;

-- Find permanently failed alerts
SELECT * FROM alerts_outbox WHERE status = 'FAILED';
```

## Tuning Parameters

### For Faster Recovery
```properties
alert.recovery.batch-size=200  # Larger batches
# Reduce scheduler delay in code: @Scheduled(fixedDelay = 120000)  # 2 minutes
```

### For More Resilient Circuit
```properties
resilience4j.circuitbreaker.instances.kafkaPublisher.failure-rate-threshold=70  # 70%
resilience4j.circuitbreaker.instances.kafkaPublisher.minimum-number-of-calls=10
```

### For More Retry Attempts
```properties
alert.recovery.max-attempts=20  # Allow more retries before giving up
```

## Handling Failed Alerts

Alerts marked as "FAILED" after 10 attempts require manual intervention:

1. **Investigate root cause**: Check Kafka logs, network issues, etc.
2. **Manual retry**: Reset status and attempt count
   ```sql
   UPDATE alerts_outbox 
   SET status = 'RECEIVED', attempt_count = 0 
   WHERE id = 'failed-alert-id';
   ```
3. **Dead letter queue**: Consider implementing DLQ for persistent failures

## Dependencies

Already included in `pom.xml`:
- `resilience4j-spring-boot3` (v2.3.0)
- `resilience4j-circuitbreaker` (v2.3.0)
- `spring-boot-starter-actuator`
- `spring-kafka`

## Benefits

1. ✅ **Zero Message Loss**: All alerts persisted before Kafka publish
2. ✅ **Automatic Recovery**: No manual intervention needed when Kafka recovers
3. ✅ **Circuit Breaker Protection**: Prevents resource exhaustion during outages
4. ✅ **Batch Processing**: Controlled recovery rate to avoid overwhelming Kafka
5. ✅ **Observability**: Health indicators and comprehensive logging
6. ✅ **Configurable**: Easy to tune for different requirements
7. ✅ **Production Ready**: Max retry limits prevent infinite loops

## Future Enhancements

1. **Dead Letter Queue**: Kafka topic for permanently failed alerts
2. **Metrics Dashboard**: Grafana dashboard for circuit breaker metrics
3. **Alert Notifications**: Notify operations when circuit opens
4. **Exponential Backoff**: Increase delay between retries
5. **Priority Queue**: Process critical alerts first during recovery
