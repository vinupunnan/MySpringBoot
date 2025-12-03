package com.kailas.mm.model.dto;

import com.kailas.mm.entity.Department;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO for Department entity with list of employees.
 */
public class DepartmentWithEmployeesDto implements Serializable {

    private Long id;
    private String name;
    private List<EmployeeDto> employees;

    public DepartmentWithEmployeesDto() {
    }

    /**
     * Constructor from Department entity.
     * Maps all employees to EmployeeDto objects.
     */
    public DepartmentWithEmployeesDto(Department department) {
        this.id = department.getId();
        this.name = department.getName();
        this.employees = department.getEmployees().stream()
                .map(EmployeeDto::new)
                .collect(Collectors.toList());
    }

    public DepartmentWithEmployeesDto(Long id, String name, List<EmployeeDto> employees) {
        this.id = id;
        this.name = name;
        this.employees = employees;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EmployeeDto> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeDto> employees) {
        this.employees = employees;
    }
}
