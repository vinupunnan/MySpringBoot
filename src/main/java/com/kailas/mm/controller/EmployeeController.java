package com.kailas.mm.controller;

import com.kailas.mm.model.dto.EmployeeDto;
import com.kailas.mm.model.dto.ItemDto;
import com.kailas.mm.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;
    @GetMapping("/")
    public ResponseEntity<List<EmployeeDto>> getAl(){
        List<EmployeeDto> employList =employeeService.getAEmployees();
        return new ResponseEntity<>(employList, HttpStatus.OK);
    }
}
