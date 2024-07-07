package com.kailas.mm.service;

import com.kailas.mm.model.dto.CustomerDto;

import java.util.List;

public interface CustomerService {
    List<CustomerDto> getAllCustomers();
}
