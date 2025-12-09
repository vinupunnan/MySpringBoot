package com.kailas.mm.service.impl;

import com.kailas.mm.dao.EmployDao;
import com.kailas.mm.model.dto.EmployeeDto;
import com.kailas.mm.model.entity.sql.Employee;
import com.kailas.mm.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
@Autowired
    EmployDao employDao;
    @Override
    public List<EmployeeDto> getAEmployees() {
    List<Employee> employList =    employDao.getEmployees();
    List<EmployeeDto> employeeDtoList = employList.stream().map(e->new EmployeeDto(e)).collect(Collectors.toList());
    return employeeDtoList;
    }
}
