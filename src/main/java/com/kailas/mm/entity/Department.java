package com.kailas.mm.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Department entity representing a department with multiple employees.
 * Used to demonstrate the N+1 problem in JPA.
 */
@Entity
@Table(name = "department")
// Uncomment the following line to enable batch fetching (Solution 4)
// @org.hibernate.annotations.BatchSize(size = 10)
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    /**
     * One-to-many relationship with employees.
     * Uses LAZY fetch to avoid loading employees unless explicitly accessed.
     * This is where the N+1 problem typically occurs when iterating over departments
     * and accessing their employees.
     */
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // Uncomment for batch fetching solution
    // @org.hibernate.annotations.BatchSize(size = 10)
    private List<Employee> employees = new ArrayList<>();

    public Department() {
    }

    public Department(String name) {
        this.name = name;
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

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
        employee.setDepartment(this);
    }

    public void removeEmployee(Employee employee) {
        employees.remove(employee);
        employee.setDepartment(null);
    }
}
