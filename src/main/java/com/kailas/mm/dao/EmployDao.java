package com.kailas.mm.dao;

import com.kailas.mm.model.entity.sql.Employee;

import java.util.List;

public interface EmployDao {
    List<Employee> getEmployees();
}
