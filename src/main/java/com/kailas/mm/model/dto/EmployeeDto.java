package com.kailas.mm.model.dto;

import com.kailas.mm.entity.Employee;

import java.io.Serializable;

/**
 * DTO for Employee entity.
 */
public class EmployeeDto implements Serializable {

    private Long id;
    private String name;
    private String email;

    public EmployeeDto() {
    }

    /**
     * Constructor from Employee entity.
     */
    public EmployeeDto(Employee employee) {
        this.id = employee.getId();
        this.name = employee.getName();
        this.email = employee.getEmail();
    }

    public EmployeeDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
