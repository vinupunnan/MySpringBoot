# Circuit Breaker Flow Diagram - Visual Guide

## 🎬 Complete Flow: From Alert Arrival to Recovery

```
┌─────────────────────────────────────────────────────────────────────┐
│                    NEW ALERT ARRIVES VIA API                        │
│                  POST /alerts/sync {dcId, data}                     │
└─────────────────────┬───────────────────────────────────────────────┘
                      │
                      ↓
         ┌────────────────────────────┐
         │ AlertSyncController        │
         │   postAlerts()             │
         └────────────┬───────────────┘
                      │
                      ↓
         ┌────────────────────────────┐
         │ AlertSinkServiceImpl       │
         │   saveAlerts()             │
         └────────────┬───────────────┘
                      │
                      │ STEP 1: SAVE TO DATABASE (ALWAYS!)
                      ↓
         ┌────────────────────────────────────────┐
         │  Database: alerts_outbox table         │
         │  ✓ id: "abc-123"                      │
         │  ✓ dcId: "DC1"                        │
         │  ✓ status: "RECEIVED"                 │
         │  ✓ createdAt: 2026-02-27 10:00:00    │
         │  ✓ attemptCount: 0                    │
         │  ✓ payload: {originalTimestamp, ...}  │
         └────────────┬───────────────────────────┘
                      │
                      │ STEP 2: TRY TO PUBLISH TO KAFKA
                      ↓
         ┌────────────────────────────┐
         │ KafkaOutboxPublisher       │
         │   @CircuitBreaker          │ ← CIRCUIT BREAKER INTERCEPTS HERE!
         │   tryPublish(entity)       │
         └────────────┬───────────────┘
                      │
                      │ Circuit Breaker Checks State
                      ↓
              ┌───────────────┐
              │ Circuit State?│
              └───────┬───────┘
                      │
        ┌─────────────┼─────────────┐
        │             │             │
        ↓             ↓             ↓
   ┌─────────┐  ┌──────────┐  ┌─────────┐
   │ CLOSED  │  │HALF_OPEN │  │  OPEN   │
   │  🟢     │  │   🟡     │  │   🔴    │
   └────┬────┘  └────┬─────┘  └────┬────┘
        │            │             │
        │            │             │
        ↓            ↓             ↓
   Try Kafka    Test Kafka    Skip Kafka
        │            │             │
        ↓            ↓             ↓
   ┌─────────────────────────────────────┐
   │         Publish Attempt             │
   └────────────┬────────────────────────┘
                │
        ┌───────┴────────┐
        │                │
        ↓                ↓
   ✓ SUCCESS        ✗ FAILURE
        │                │
        │                │
        ↓                ↓
   Update DB        Update DB
   status =         status =
   "PUBLISHED"      "RECEIVED"
   lastAttempt      lastAttempt
                    attemptCount++
        │                │
        │                │
        ↓                ↓
   ┌─────────────────────────────┐
   │    Return to API            │
   │    (200 OK response)        │
   └─────────────────────────────┘
                │
                │
        Alert saved, API returns fast!
        If failed, scheduler will retry


═══════════════════════════════════════════════════════════════════
                    5 MINUTES LATER...
═══════════════════════════════════════════════════════════════════

         ┌────────────────────────────┐
         │  Scheduler Wakes Up        │
         │  @Scheduled(every 5 min)   │
         └────────────┬───────────────┘
                      │
                      │ STEP 1: CHECK CIRCUIT BREAKER
                      ↓
         ┌────────────────────────────┐
         │ circuitBreakerRegistry     │
         │   .circuitBreaker(...)     │
         │   .getState()              │
         └────────────┬───────────────┘
                      │
              ┌───────┴───────┐
              │               │
              ↓               ↓
         ┌─────────┐     ┌─────────┐
         │  OPEN   │     │ CLOSED/ │
         │   🔴    │     │HALF_OPEN│
         └────┬────┘     └────┬────┘
              │               │
              ↓               ↓
         Skip Recovery   Continue Recovery
         Log: "Circuit        │
         is OPEN"             │
              │               │
              │               ↓
              │    ┌──────────────────────┐
              │    │ Query Database for:  │
              │    │ - status = RECEIVED  │
              │    │ - attemptCount < 5   │
              │    │ - age > 6 seconds    │
              │    │ LIMIT 100            │
              │    └──────────┬───────────┘
              │               │
              │               ↓
              │    ┌──────────────────────┐
              │    │ Found alerts to retry│
              │    └──────────┬───────────┘
              │               │
              │               │ For each alert:
              │               ↓
              │    ┌──────────────────────┐
              │    │ Check circuit again  │
              │    │ (might open during)  │
              │    └──────────┬───────────┘
              │               │
              │        ┌──────┴──────┐
              │        │             │
              │        ↓             ↓
              │    Still OK?     Opened?
              │        │             │
              │        ↓             ↓
              │    Retry Publish  Stop Recovery
              │        │             │
              │        ↓             │
              │    ┌─────────┐      │
              │    │Success? │      │
              │    └────┬────┘      │
              │         │           │
              │    ┌────┴────┐      │
              │    │         │      │
              │    ↓         ↓      │
              │  ✓ Yes    ✗ No     │
              │    │         │      │
              │    ↓         ↓      │
              │ PUBLISHED attemptCount++│
              │    │         │      │
              │    │    ┌────┴────┐ │
              │    │    │         │ │
              │    │    ↓         ↓ │
              │    │ Still < 5? = 5?│
              │    │    │         │ │
              │    │    ↓         ↓ │
              │    │  Retry    FAILED│
              │    │  later           │
              │    │    │         │ │
              │    │    ↓         │ │
              │    │ Sleep 100ms  │ │
              │    │ (throttle)   │ │
              │    │              │ │
              │    └──────────────┘ │
              │                     │
              └─────────────────────┘
                      │
                      ↓
         ┌────────────────────────────┐
         │ Log Recovery Stats:        │
         │ - Success: X               │
         │ - Failed: Y                │
         │ - Maxed Out: Z             │
         │ - Skipped: W               │
         └────────────────────────────┘
```

---

## 🔄 Circuit Breaker State Transitions

```
                    Normal Operation
                          │
                          ↓
              ┌───────────────────────┐
              │      CLOSED 🟢        │
              │                       │
              │ All calls go through  │
              │ Counting successes    │
              │ and failures          │
              └───────┬───────────────┘
                      │
                      │ 5 failures out of last 10 calls
                      │ (50% threshold reached)
                      ↓
              ┌───────────────────────┐
              │       OPEN 🔴         │
              │                       │
              │ All calls fail fast   │
              │ via fallback method   │
              │                       │
              │ ⏰ Wait 60 seconds... │
              └───────┬───────────────┘
                      │
                      │ After 60 seconds
                      │ (automatic transition)
                      ↓
              ┌───────────────────────┐
              │    HALF_OPEN 🟡       │
              │                       │
              │ Allow 5 test calls    │
              │ to check if Kafka     │
              │ has recovered         │
              └───────┬───────────────┘
                      │
          ┌───────────┴───────────┐
          │                       │
          ↓                       ↓
    All 5 succeed           Any failures
          │                       │
          ↓                       ↓
   Back to CLOSED 🟢      Back to OPEN 🔴
   (Normal operation)    (Wait 60s again)
```

---

## 📊 Failure Counting Logic

```
Sliding Window (last 10 calls):

Call #1:  ✓  Success
Call #2:  ✓  Success
Call #3:  ✓  Success
Call #4:  ✗  Failure  ← Kafka starts failing
Call #5:  ✗  Failure
Call #6:  ✗  Failure
Call #7:  ✗  Failure
Call #8:  ✗  Failure
          ━━━━━━━━━━━━━━━━━━━━━━━━━━━━
          Calculation:
          - Last 10 calls: 3 success, 5 failures = 50% failure rate
          - Threshold: 50%
          - Result: 50% >= 50% → OPEN CIRCUIT! 🔴
          ━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Call #9:  ⚡ Circuit OPEN → fallback → immediate fail
Call #10: ⚡ Circuit OPEN → fallback → immediate fail
Call #11: ⚡ Circuit OPEN → fallback → immediate fail
          (All super fast, no timeout delays!)

After 60 seconds → HALF_OPEN 🟡

Test #1:  ✓  Success  ← Kafka is back!
Test #2:  ✓  Success
Test #3:  ✓  Success
Test #4:  ✓  Success
Test #5:  ✓  Success
          ━━━━━━━━━━━━━━━━━━━━━━━━━━━━
          5 out of 5 succeeded!
          → CLOSE CIRCUIT! 🟢
          ━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Call #12: ✓ Circuit CLOSED → normal operation resumed
```

---

## 🏃 Fast Fail Example (Time Comparison)

### WITHOUT Circuit Breaker:
```
Time      Event                          Duration
─────────────────────────────────────────────────
10:00:00  Alert arrives                  0ms
10:00:00  Try to publish to Kafka...     
          [waiting...]
          [waiting...]
          [connection timeout...]
10:00:30  Kafka timeout!                 30,000ms ❌
10:00:30  Return error to user           

Total: 30 seconds per alert!
```

### WITH Circuit Breaker (after opening):
```
Time      Event                          Duration
─────────────────────────────────────────────────
10:00:00  Alert arrives                  0ms
10:00:00  Circuit breaker checks: OPEN
10:00:00  Execute fallback method        1ms ⚡
10:00:00  Return to user                 

Total: 1 millisecond per alert!
```

**That's 30,000x faster!** 🚀

---

## 🔍 Real Database States

### When Circuit is CLOSED (Kafka Working):
```sql
SELECT * FROM alerts_outbox;

id       | status    | attemptCount | created_at          | last_attempt
─────────┼───────────┼──────────────┼─────────────────────┼─────────────────────
abc-123  | PUBLISHED | 0            | 2026-02-27 10:00:00 | 2026-02-27 10:00:00
def-456  | PUBLISHED | 0            | 2026-02-27 10:00:01 | 2026-02-27 10:00:01
ghi-789  | PUBLISHED | 0            | 2026-02-27 10:00:02 | 2026-02-27 10:00:02
```
✓ All alerts published immediately

### When Circuit OPENS (Kafka Down):
```sql
SELECT * FROM alerts_outbox;

id       | status    | attemptCount | created_at          | last_attempt
─────────┼───────────┼──────────────┼─────────────────────┼─────────────────────
abc-123  | PUBLISHED | 0            | 2026-02-27 10:00:00 | 2026-02-27 10:00:00
def-456  | PUBLISHED | 0            | 2026-02-27 10:00:01 | 2026-02-27 10:00:01
ghi-789  | PUBLISHED | 0            | 2026-02-27 10:00:02 | 2026-02-27 10:00:02
jkl-111  | RECEIVED  | 1            | 2026-02-27 10:00:03 | 2026-02-27 10:00:03
mno-222  | RECEIVED  | 1            | 2026-02-27 10:00:04 | 2026-02-27 10:00:04
pqr-333  | RECEIVED  | 1            | 2026-02-27 10:00:05 | 2026-02-27 10:00:05
```
⚠️ New alerts stay in RECEIVED status

### After Scheduler Retries (5 minutes later):
```sql
SELECT * FROM alerts_outbox;

id       | status    | attemptCount | created_at          | last_attempt
─────────┼───────────┼──────────────┼─────────────────────┼─────────────────────
abc-123  | PUBLISHED | 0            | 2026-02-27 10:00:00 | 2026-02-27 10:00:00
def-456  | PUBLISHED | 0            | 2026-02-27 10:00:01 | 2026-02-27 10:00:01
ghi-789  | PUBLISHED | 0            | 2026-02-27 10:00:02 | 2026-02-27 10:00:02
jkl-111  | RECEIVED  | 2            | 2026-02-27 10:00:03 | 2026-02-27 10:05:00
mno-222  | RECEIVED  | 2            | 2026-02-27 10:00:04 | 2026-02-27 10:05:00
pqr-333  | RECEIVED  | 2            | 2026-02-27 10:00:05 | 2026-02-27 10:05:00
```
⚠️ attemptCount incremented, still RECEIVED (Kafka still down)

### After Circuit Closes (Kafka Recovers):
```sql
SELECT * FROM alerts_outbox;

id       | status    | attemptCount | created_at          | last_attempt
─────────┼───────────┼──────────────┼─────────────────────┼─────────────────────
abc-123  | PUBLISHED | 0            | 2026-02-27 10:00:00 | 2026-02-27 10:00:00
def-456  | PUBLISHED | 0            | 2026-02-27 10:00:01 | 2026-02-27 10:00:01
ghi-789  | PUBLISHED | 0            | 2026-02-27 10:00:02 | 2026-02-27 10:00:02
jkl-111  | PUBLISHED | 3            | 2026-02-27 10:00:03 | 2026-02-27 10:15:00
mno-222  | PUBLISHED | 3            | 2026-02-27 10:00:04 | 2026-02-27 10:15:00
pqr-333  | PUBLISHED | 3            | 2026-02-27 10:00:05 | 2026-02-27 10:15:00
```
✓ All alerts eventually published!

---

## 🎯 Key Takeaways

1. **Circuit Breaker = Smart Protection**
   - Monitors every Kafka publish call
   - Opens after 50% failure rate
   - Prevents cascading failures

2. **Three States Working Together**
   - CLOSED: Normal operation
   - OPEN: Fast fail mode
   - HALF_OPEN: Testing recovery

3. **Database + Circuit Breaker = Zero Loss**
   - Save first, publish second
   - If publish fails, scheduler retries
   - Original timestamp preserved

4. **Automatic Everything**
   - Auto opens when failures detected
   - Auto tests recovery after 60s
   - Auto closes when Kafka recovers

5. **Race Condition Solved**
   - Scheduler waits 6 seconds
   - Checks circuit before recovery
   - Stops if circuit opens mid-batch

**Your application is bulletproof!** 🛡️
