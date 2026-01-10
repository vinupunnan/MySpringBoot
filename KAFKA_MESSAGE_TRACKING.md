# Kafka Message Delivery Tracking Implementation

## Overview

This implementation ensures that all messages sent to Kafka from the `AlertSyncController` are tracked for success or failure. Failed messages are automatically recorded in a database for later retry and monitoring.

## Architecture

The solution consists of three main components:

1. **AlertSyncController** - REST API endpoints for syncing alerts
2. **AlertSinkService** - Business logic for processing alerts
3. **KafkaOutboxPublisher** - Kafka message publisher with failure tracking

## Components

### 1. AlertSyncController

**Location:** `src/main/java/com/kailas/mm/controller/AlertSyncController.java`

REST API endpoints for managing alert synchronization:

- `POST /api/alerts/sync` - Sync a single alert
- `POST /api/alerts/sync/batch` - Sync multiple alerts
- `GET /api/alerts/all` - Get all alerts
- `GET /api/alerts/failed-messages` - Get all failed Kafka messages
- `POST /api/alerts/failed-messages/{messageId}/retry` - Retry a specific failed message
- `POST /api/alerts/failed-messages/retry-all` - Retry all failed messages

### 2. AlertSinkService

**Location:** `src/main/java/com/kailas/mm/service/AlertSinkService.java`

Handles the business logic for processing alerts:

- Saves alerts to the database
- Publishes alerts to Kafka via KafkaOutboxPublisher
- Updates alert status based on Kafka publish success/failure
- Provides methods to retrieve alerts

### 3. KafkaOutboxPublisher

**Location:** `src/main/java/com/kailas/mm/service/KafkaOutboxPublisher.java`

Core service for Kafka message publishing with failure tracking:

**Key Features:**
- Publishes messages to Kafka topics
- Tracks success/failure of each message
- Automatically records failed messages to database
- Provides retry mechanism for failed messages
- Uses Kafka callbacks to ensure delivery confirmation

**How it Works:**
1. Accepts a message object (or JSON string)
2. Sends the message to Kafka asynchronously
3. Registers success/failure callbacks
4. On success: Logs the successful delivery
5. On failure: Records the failed message in the `failed_kafka_messages` table

## Database Schema

### alerts Table

| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT (PK) | Auto-generated ID |
| alert_type | VARCHAR | Type of alert |
| message | VARCHAR | Alert message content |
| status | VARCHAR | Status: "SENT" or "FAILED" |
| created_at | TIMESTAMP | Creation timestamp |

### failed_kafka_messages Table

| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT (PK) | Auto-generated ID |
| topic | VARCHAR | Kafka topic name |
| message_key | VARCHAR | Message key |
| message_payload | TEXT | Full message payload (JSON) |
| error_message | TEXT | Error details |
| retry_count | INTEGER | Number of retry attempts |
| created_at | TIMESTAMP | Creation timestamp |
| last_retry_at | TIMESTAMP | Last retry timestamp |

## Usage Examples

### 1. Sync a Single Alert

```bash
curl -X POST http://localhost:8080/api/alerts/sync \
  -H "Content-Type: application/json" \
  -d '{
    "alertType": "CRITICAL",
    "message": "System overload detected"
  }'
```

**Response:**
```json
{
  "statusCode": 200,
  "message": "Alert synced successfully",
  "data": {
    "id": 1,
    "alertType": "CRITICAL",
    "message": "System overload detected",
    "status": "SENT",
    "createdAt": "2026-01-10T17:30:00"
  },
  "error": null
}
```

### 2. Sync Multiple Alerts

```bash
curl -X POST http://localhost:8080/api/alerts/sync/batch \
  -H "Content-Type: application/json" \
  -d '[
    {
      "alertType": "WARNING",
      "message": "High memory usage"
    },
    {
      "alertType": "INFO",
      "message": "Deployment completed"
    }
  ]'
```

**Response:**
```json
{
  "statusCode": 200,
  "message": "Processed 2 alerts: 2 successful, 0 failed",
  "data": [...],
  "error": null
}
```

### 3. Get Failed Messages

```bash
curl -X GET http://localhost:8080/api/alerts/failed-messages
```

**Response:**
```json
{
  "statusCode": 200,
  "message": "Found 3 failed messages",
  "data": [
    {
      "id": 1,
      "topic": "alerts-topic",
      "messageKey": "123",
      "messagePayload": "{...}",
      "errorMessage": "Connection timeout",
      "retryCount": 0,
      "createdAt": "2026-01-10T17:25:00",
      "lastRetryAt": null
    }
  ],
  "error": null
}
```

### 4. Retry a Failed Message

```bash
curl -X POST http://localhost:8080/api/alerts/failed-messages/1/retry
```

### 5. Retry All Failed Messages

```bash
curl -X POST http://localhost:8080/api/alerts/failed-messages/retry-all
```

## Error Handling

The implementation provides comprehensive error handling:

1. **Serialization Errors**: Caught and logged when converting objects to JSON
2. **Kafka Connection Errors**: Caught and recorded as failed messages
3. **Kafka Send Failures**: Detected via callbacks and recorded
4. **Database Errors**: Logged but don't prevent Kafka publishing attempts

## Message Status Flow

```
Alert Created
    ↓
Saved to Database (status: null)
    ↓
Publish to Kafka
    ↓
    ├─→ Success → Update status to "SENT"
    └─→ Failure → Update status to "FAILED"
                  → Record in failed_kafka_messages table
```

## Monitoring

To monitor failed messages:

1. **Check failed messages count:**
   ```bash
   curl http://localhost:8080/api/alerts/failed-messages
   ```

2. **Query database directly:**
   ```sql
   SELECT COUNT(*) FROM failed_kafka_messages;
   SELECT * FROM failed_kafka_messages WHERE retry_count < 3;
   ```

3. **Check application logs:**
   - Search for "Failed to send message to topic"
   - Search for "Recorded failed message for topic"

## Configuration

### Required Application Properties

Add the following to `application.properties` or `application.yml`:

```yaml
# Kafka Configuration
spring:
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
      acks: all
      retries: 3
```

## Dependencies Added

The following dependencies were added to `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>

<dependency>
    <groupId>com.fasterxml.jackson.datatype</groupId>
    <artifactId>jackson-datatype-jsr310</artifactId>
</dependency>
```

## Benefits

1. **Reliability**: No messages are lost; failed messages are tracked
2. **Observability**: Easy to monitor failed messages
3. **Retry Capability**: Failed messages can be retried manually
4. **Audit Trail**: Complete history of message delivery attempts
5. **Separation of Concerns**: Clean architecture with distinct layers

## Future Enhancements

Potential improvements:

1. Add automatic retry scheduler for failed messages
2. Add configurable maximum retry attempts
3. Add dead letter queue for permanently failed messages
4. Add metrics/monitoring endpoints (Prometheus, Grafana)
5. Add alert notifications for persistent failures
6. Add batch processing for retries
7. Add message deduplication
8. Add circuit breaker pattern for Kafka connectivity issues

## Testing

To test the implementation:

1. Start Kafka locally or point to a Kafka cluster
2. Update application configuration with Kafka broker address
3. Start the Spring Boot application
4. Use the provided curl commands to test endpoints
5. Simulate Kafka failure by stopping Kafka broker
6. Verify failed messages are recorded
7. Restart Kafka and retry failed messages

## Troubleshooting

### Messages Not Being Sent

1. Check Kafka broker connectivity
2. Verify `spring.kafka.bootstrap-servers` configuration
3. Check application logs for connection errors

### Failed Messages Not Being Recorded

1. Verify database connection
2. Check that tables are created (JPA should auto-create)
3. Review application logs for database errors

### Retry Not Working

1. Check that failed message exists in database
2. Verify Kafka is accessible
3. Check retry count hasn't exceeded limits (if implemented)

## Conclusion

This implementation provides a robust solution for tracking Kafka message delivery success and failure. All messages are accounted for, and failed messages can be easily identified and retried, ensuring data integrity and reliability in your messaging infrastructure.
