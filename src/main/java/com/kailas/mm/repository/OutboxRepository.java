package com.kailas.mm.repository;

import com.kailas.mm.model.entity.sql.OutboxAlertEntity;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface OutboxRepository extends JpaRepository<OutboxAlertEntity, String> {
    List<OutboxAlertEntity> findByStatus(String status);

    @Query("SELECT o FROM OutboxAlertEntity o WHERE o.status = :status AND o.attemptCount < :maxAttempts AND o.createdAt < :threshold ORDER BY o.createdAt ASC")
    List<OutboxAlertEntity> findOldFailedAlerts(@Param("status") String status,
            @Param("maxAttempts") int maxAttempts,
            @Param("threshold") Instant threshold,
            Pageable pageable);

}