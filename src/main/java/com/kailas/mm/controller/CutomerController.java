package com.kailas.mm.controller;

import com.kailas.mm.model.dto.CustomerDto;
import com.kailas.mm.model.dto.ItemDto;
import com.kailas.mm.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CutomerController {
    @Autowired
    CustomerService customerService;
    @GetMapping("/")
    public ResponseEntity<List<CustomerDto>> getAllItems(){
        List<CustomerDto> customerList = customerService.getAllCustomers();
        return new ResponseEntity<>(customerList, HttpStatus.OK);
    }
}
