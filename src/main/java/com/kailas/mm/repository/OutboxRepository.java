package com.kailas.mm.repository;

import com.kailas.mm.model.entity.sql.OutboxAlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxRepository extends JpaRepository<OutboxAlertEntity, String> {
    List<OutboxAlertEntity> findByStatus(String status);
}