package com.kailas.mm.dao.impl;

import com.kailas.mm.dao.EmployDao;
import com.kailas.mm.model.entity.sql.Employee;
import com.kailas.mm.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmployeeDaoImpl implements EmployDao {
    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public List<Employee> getEmployees() {
    //    return employeeRepository.findAll();
    //    return  employeeRepository.findBySalary(34000);
     //   return employeeRepository.findBySalaryByJpql(34000);
        return employeeRepository.findByNormalQuery(64000);
    }
}