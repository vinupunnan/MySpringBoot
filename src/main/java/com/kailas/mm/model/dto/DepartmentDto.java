package com.kailas.mm.model.dto;

import com.kailas.mm.entity.Department;
import com.kailas.mm.model.projection.DepartmentProjection;

import java.io.Serializable;

/**
 * DTO for Department entity with employee count.
 */
public class DepartmentDto implements Serializable {

    private Long id;
    private String name;
    private Long employeeCount;

    public DepartmentDto() {
    }

    /**
     * Constructor from Department entity.
     */
    public DepartmentDto(Department department) {
        this.id = department.getId();
        this.name = department.getName();
        this.employeeCount = (long) department.getEmployees().size();
    }

    /**
     * Constructor from DepartmentProjection.
     */
    public DepartmentDto(DepartmentProjection projection) {
        this.id = projection.getId();
        this.name = projection.getName();
        this.employeeCount = projection.getEmployeeCount();
    }

    public DepartmentDto(Long id, String name, Long employeeCount) {
        this.id = id;
        this.name = name;
        this.employeeCount = employeeCount;
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

    public Long getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(Long employeeCount) {
        this.employeeCount = employeeCount;
    }
}
