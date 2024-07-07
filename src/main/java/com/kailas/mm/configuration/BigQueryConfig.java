package com.kailas.mm.configuration;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class BigQueryConfig {

    @Value("${spring.cloud.gcp.credentials.location}")
    private Resource gcpCredentials;



    @Bean
    public BigQuery bigQuery() throws IOException {
        return BigQueryOptions.newBuilder()
             //   .setProjectId(gcpProjectId)
                .setCredentials(ServiceAccountCredentials.fromStream(gcpCredentials.getInputStream()))
                .build()
                .getService();
    }
}
