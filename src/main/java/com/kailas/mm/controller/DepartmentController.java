package com.kailas.mm.controller;

import com.kailas.mm.model.dto.DepartmentDto;
import com.kailas.mm.model.dto.DepartmentWithEmployeesDto;
import com.kailas.mm.service.DepartmentService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller demonstrating the N+1 problem and solutions.
 * 
 * Endpoints:
 * - GET /departments/n-plus-one    - Demonstrates N+1 problem (watch logs for multiple queries)
 * - GET /departments/join-fetch    - Solution using JOIN FETCH (single query)
 * - GET /departments/entity-graph  - Solution using @EntityGraph (single query)
 * - GET /departments/projection    - Solution using DTO projection (optimized query)
 * - GET /departments/{id}          - Get single department with employees
 * 
 * To observe the difference:
 * 1. Enable SQL logging in application.yml
 * 2. Call each endpoint and observe the SQL queries in the logs
 * 3. Compare the number of queries between n-plus-one and other endpoints
 */
@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * Endpoint demonstrating the N+1 problem.
     * 
     * Call this endpoint and watch the logs to see:
     * - 1 query to fetch all departments
     * - N queries to fetch employees for each department
     * 
     * Example with 5 departments: 6 queries total (1 + 5)
     */
    @GetMapping("/n-plus-one")
    public ResponseEntity<List<DepartmentWithEmployeesDto>> getAllDepartmentsWithNPlusOneProblem() {
        List<DepartmentWithEmployeesDto> departments = departmentService.getAllDepartmentsWithNPlusOneProblem();
        return ResponseEntity.ok(departments);
    }

    /**
     * Endpoint using JOIN FETCH solution.
     * 
     * Call this endpoint and watch the logs to see:
     * - Only 1 query with JOIN to fetch departments and employees together
     */
    @GetMapping("/join-fetch")
    public ResponseEntity<List<DepartmentWithEmployeesDto>> getAllDepartmentsWithJoinFetch() {
        List<DepartmentWithEmployeesDto> departments = departmentService.getAllDepartmentsWithJoinFetch();
        return ResponseEntity.ok(departments);
    }

    /**
     * Endpoint using @EntityGraph solution.
     * 
     * Call this endpoint and watch the logs to see:
     * - Only 1 query using EntityGraph to fetch departments and employees
     */
    @GetMapping("/entity-graph")
    public ResponseEntity<List<DepartmentWithEmployeesDto>> getAllDepartmentsWithEntityGraph() {
        List<DepartmentWithEmployeesDto> departments = departmentService.getAllDepartmentsWithEntityGraph();
        return ResponseEntity.ok(departments);
    }

    /**
     * Endpoint using DTO projection solution.
     * 
     * Call this endpoint and watch the logs to see:
     * - Only 1 optimized query selecting only required columns
     * - Returns department with employee count (not full employee list)
     */
    @GetMapping("/projection")
    public ResponseEntity<List<DepartmentDto>> getAllDepartmentsWithProjection() {
        List<DepartmentDto> departments = departmentService.getAllDepartmentsWithProjection();
        return ResponseEntity.ok(departments);
    }

    /**
     * Get a single department by ID with all employees.
     * Uses JOIN FETCH for efficient loading.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentWithEmployeesDto> getDepartmentById(@PathVariable Long id) {
        DepartmentWithEmployeesDto department = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(department);
    }
}
