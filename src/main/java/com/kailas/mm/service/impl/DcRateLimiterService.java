package com.kailas.mm.service.impl;


import com.kailas.mm.model.properties.Limit;
import com.kailas.mm.model.properties.RateLimitProperties;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;


;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;;

@Service
public class DcRateLimiterService {

    private final RateLimitProperties properties;
    private final Map<String, Bucket> bucketCache = new ConcurrentHashMap<>();

    public DcRateLimiterService(RateLimitProperties properties) {
        this.properties = properties;
    }

    public Bucket getBucketFor(String dcId) {

        return bucketCache.computeIfAbsent(dcId, id -> {
            Limit limit = properties.getDcs().get(id);

            if (limit == null) {
                limit = properties.getDefaultLimit();
            }

            int capacity = limit.getCapacity();
            int period = limit.getPeriod();

            Refill refill = Refill.greedy(capacity, Duration.ofSeconds(period));
            Bandwidth bandwidth = Bandwidth.classic(capacity, refill);

            return Bucket.builder()
                    .addLimit(bandwidth)
                    .build();
        });
    }
}