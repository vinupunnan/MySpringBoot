package com.kailas.mm.model.entity.sql;

import com.kailas.mm.utils.MapToJsonConverter;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "alerts_outbox")
public class OutboxAlertEntity {
    @Id
    private String id;

    private String dcId;

    @Column(columnDefinition = "JSON")
    @Convert(converter = MapToJsonConverter.class)
    private Map<String, Object> payload;


    private String status;

    private Instant createdAt;

    private Instant lastAttempt;

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getLastAttempt() {
        return lastAttempt;
    }

    public void setLastAttempt(Instant lastAttempt) {
        this.lastAttempt = lastAttempt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDcId() {
        return dcId;
    }

    public void setDcId(String dcId) {
        this.dcId = dcId;
    }
}
