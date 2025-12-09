package com.kailas.mm.model.entity.sql;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Alert_Batch_Tracker")
public class AlertBatchTracker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "dc_id")
    private String dcId;
    @Column(name = "batch_hash")
    private String batchHash;
    @Column(name = "alert_count")
    private int alertCount;
    @Column(name = "status")
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDcId() {
        return dcId;
    }

    public void setDcId(String dcId) {
        this.dcId = dcId;
    }

    public String getBatchHash() {
        return batchHash;
    }

    public void setBatchHash(String batchHash) {
        this.batchHash = batchHash;
    }

    public int getAlertCount() {
        return alertCount;
    }

    public void setAlertCount(int alertCount) {
        this.alertCount = alertCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    private LocalDateTime createdAt = LocalDateTime.now();
}
