package com.kailas.mm.repository;

import com.kailas.mm.entity.FailedKafkaMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FailedKafkaMessageRepository extends JpaRepository<FailedKafkaMessage, Long> {
    List<FailedKafkaMessage> findByRetryCountLessThan(Integer maxRetries);
}
