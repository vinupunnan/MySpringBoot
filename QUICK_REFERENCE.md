# Quick Reference Guide - Circuit Breaker Implementation

## ✅ What Was Implemented

### Core Features:
1. **Scheduler runs every 5 minutes** with 1-minute initial delay
2. **Only picks alerts older than 6 seconds** to avoid race with API
3. **Maximum 5 retry attempts** before marking as FAILED
4. **Original timestamp preserved** in Kafka messages
5. **Circuit breaker aware** - stops if Kafka is down
6. **Throttles 100ms** between messages during recovery
7. **Batch processing** - 100 alerts per run

---

## 📝 Quick Test Commands

### Test Race Condition Prevention
```bash
# Send alert
curl -X POST http://localhost:9292/alerts/sync -H "Content-Type: application/json" -d '{"dcId":"DC1","data":{"test":"value"}}'

# Check if scheduler picks it up immediately (it shouldn't if < 6 sec old)
# Check logs for: "No failed alerts to recover (older than 6 seconds)"
```

### Check Alert Status
```sql
SELECT id, status, attempt_count, 
       TIMESTAMPDIFF(SECOND, created_at, NOW()) as age_seconds,
       created_at, last_attempt
FROM alerts_outbox 
WHERE status = 'RECEIVED'
ORDER BY created_at DESC;
```

### View Circuit Breaker State
```bash
curl http://localhost:9292/actuator/health/circuitBreakers
```

### Monitor Recovery Logs
```bash
# Windows PowerShell
Get-Content logs\application.log -Wait | Select-String -Pattern "Circuit|Recovery|alert"

# Or use tail if available
tail -f logs/application.log | grep -E "Circuit|Recovery"
```

---

## 🔧 Configuration Values

| Setting | Value | What It Does |
|---------|-------|--------------|
| `alert.recovery.batch-size` | 100 | Alerts per scheduler run |
| `alert.recovery.max-attempts` | 5 | Max retries before FAILED |
| `alert.recovery.min-age-seconds` | 6 | Minimum age to avoid race |
| `alert.recovery.throttle-ms` | 100 | Delay between publishes |
| Scheduler interval | 5 min | How often scheduler runs |

---

## 🎯 Key Behaviors

### When Kafka is Down:
```
1. API saves alert with status="RECEIVED"
2. API tries to publish → fails
3. Circuit breaker opens after 50% failure rate
4. Scheduler skips recovery (circuit is OPEN)
5. Alert stays in RECEIVED status
```

### When Kafka Comes Back:
```
1. Circuit breaker auto-closes after 60 seconds
2. Scheduler runs (every 5 min)
3. Picks alerts older than 6 seconds
4. Publishes in batches of 100
5. 100ms delay between each
6. Original timestamp sent to Kafka
7. Status changes to PUBLISHED
```

### After 5 Failed Attempts:
```
1. Alert has been retrying for ~25 minutes
2. attemptCount reaches 5
3. Status changed to FAILED
4. Manual intervention required
5. Check Kafka logs for root cause
```

---

## 🐛 Troubleshooting

### Alerts Not Being Recovered
**Check 1:** Circuit breaker state
```bash
curl http://localhost:9292/actuator/health/circuitBreakers
# If OPEN, wait for it to close or fix Kafka
```

**Check 2:** Alert age
```sql
SELECT id, TIMESTAMPDIFF(SECOND, created_at, NOW()) as age_seconds
FROM alerts_outbox WHERE status = 'RECEIVED';
-- Must be >= 6 seconds
```

**Check 3:** Scheduler is running
```bash
# Look for this log every 5 minutes:
# "Starting alert recovery process..."
```

### Too Many FAILED Alerts
**Possible causes:**
- Kafka is unstable (frequent restarts)
- Circuit breaker threshold too aggressive
- Max attempts too low

**Solutions:**
1. Increase max attempts: `alert.recovery.max-attempts=10`
2. Adjust circuit breaker threshold in properties
3. Investigate Kafka stability

### Original Timestamp Missing
**Check:** Payload includes timestamp fields
```java
// In AlertSinkServiceImpl.saveAlerts()
payload.put("originalTimestamp", originalTimestamp.toString());
payload.put("eventTime", originalTimestamp.toEpochMilli());
```

---

## 📊 Database Queries

### Count by Status
```sql
SELECT status, COUNT(*) FROM alerts_outbox GROUP BY status;
```

### Find Stuck Alerts
```sql
SELECT * FROM alerts_outbox 
WHERE status = 'RECEIVED' 
  AND TIMESTAMPDIFF(MINUTE, created_at, NOW()) > 30
ORDER BY created_at ASC;
```

### Recovery Success Rate
```sql
SELECT 
    DATE(last_attempt) as date,
    COUNT(CASE WHEN status='PUBLISHED' AND attempt_count > 0 THEN 1 END) as recovered,
    COUNT(CASE WHEN status='FAILED' THEN 1 END) as failed
FROM alerts_outbox
GROUP BY DATE(last_attempt);
```

---

## 🚀 Performance Impact

### Minimal Overhead:
- Scheduler runs once every 5 minutes
- Max 100 alerts per run
- 100ms throttle = 10 alerts/second
- Total: ~10 seconds per batch

### No Impact on Live Traffic:
- Scheduler checks circuit breaker first
- Stops if circuit opens mid-batch
- Live alerts always take priority

---

## 📋 Files Changed

1. ✅ `application-dev.properties` - Added 4 config lines
2. ✅ `OutboxRepository.java` - Added findOldFailedAlerts() method
3. ✅ `AlertSinkServiceImpl.java` - Added timestamp to payload
4. ✅ `AlertRecoveryScheduler.java` - Complete rewrite with circuit breaker
5. ✅ `KafkaOutboxPublisher.java` - Already had circuit breaker
6. ✅ `MMApplication.java` - Already had @EnableScheduling
7. ✅ `OutboxAlertEntity.java` - Already had attemptCount field

---

## 🎉 Success Indicators

✅ Logs show: "Starting alert recovery process... (Circuit Breaker: CLOSED)"  
✅ Alerts older than 6 seconds are picked up  
✅ New alerts not picked up immediately (< 6 sec)  
✅ Recovery throttles at 10 alerts/second  
✅ Circuit opens when Kafka down  
✅ Recovery stops when circuit opens  
✅ Kafka messages include originalTimestamp  
✅ Alerts marked FAILED after 5 attempts  

---

## 📞 Quick Help

**Where to look:**
- Logs: `logs/application.log`
- Health: `http://localhost:9292/actuator/health`
- Circuit: `http://localhost:9292/actuator/health/circuitBreakers`
- Database: `SELECT * FROM alerts_outbox`

**Common log messages:**
- ✅ "Starting alert recovery process..." → Scheduler running
- ✅ "Found X failed alerts to retry" → Alerts found
- ✅ "Successfully recovered alert" → Alert republished
- ⚠️ "Circuit breaker is OPEN" → Kafka down, recovery skipped
- ⚠️ "Circuit opened during recovery" → Recovery stopped mid-batch
- ❌ "Alert X has reached max attempts" → Alert marked FAILED

---

**Last Updated:** February 15, 2026  
**Status:** ✅ Implementation Complete  
**Ready For:** Testing in Dev Environment
