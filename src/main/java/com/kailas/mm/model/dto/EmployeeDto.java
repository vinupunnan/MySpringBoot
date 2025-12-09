package com.kailas.mm.model.dto;

import com.kailas.mm.model.entity.sql.Employee;
import jakarta.persistence.Column;

public class EmployeeDto {
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String gender;
    public EmployeeDto(Employee employee) {
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
        this.gender = employee.getGender();
        this.employeeCode=employee.getEmployeeCode();
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
