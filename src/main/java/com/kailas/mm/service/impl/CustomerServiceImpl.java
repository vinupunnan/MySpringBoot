package com.kailas.mm.service.impl;


import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.TableResult;
import com.kailas.mm.model.dto.CustomerDto;
import com.kailas.mm.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

   // private final BigQuery bigQuery;
    @Autowired
    BigQuery bigQuery;

    @Autowired
//    public CustomerServiceImpl(BigQuery bigQuery) {
//        this.bigQuery = bigQuery;
//    }

    public List<CustomerDto> getAllCustomers() {
        String query = "SELECT cust_id, first_name, email_id FROM bigqueryproject-425617.train_db.customer_master";
        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();

        TableResult result;
        try {
            result = bigQuery.query(queryConfig);
        } catch (InterruptedException e) {
            throw new RuntimeException("Query interrupted", e);
        }

        List<CustomerDto> customers = new ArrayList<>();
        result.iterateAll().forEach(row -> {
            CustomerDto customer = new CustomerDto();
            customer.setId(row.get("cust_id").getLongValue());
            customer.setName(row.get("first_name").getStringValue());
            customer.setEmail(row.get("email_id").getStringValue());
            customers.add(customer);
        });

        return customers;
    }
}
