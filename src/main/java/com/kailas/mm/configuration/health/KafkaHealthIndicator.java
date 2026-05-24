package com.kailas.mm.configuration.health;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component("kafka")
public class KafkaHealthIndicator implements HealthIndicator {

    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final int TIMEOUT_SECONDS = 5;

    @Override
    public Health health() {
        try (AdminClient adminClient = AdminClient.create(
                Map.of(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS))) {

            adminClient.listTopics().names().get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            return Health.up()
                    .withDetail("bootstrap.servers", BOOTSTRAP_SERVERS)
                    .build();

        } catch (Exception e) {
            return Health.down()
                    .withDetail("bootstrap.servers", BOOTSTRAP_SERVERS)
                    .withException(e)
                    .build();
        }
    }
}
