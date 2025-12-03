package com.kailas.mm.service;

import com.kailas.mm.model.dto.DepartmentDto;
import com.kailas.mm.model.dto.DepartmentWithEmployeesDto;

import java.util.List;

/**
 * Service interface for Department operations.
 * Defines methods demonstrating the N+1 problem and various solutions.
 */
public interface DepartmentService {

    /**
     * Demonstrates the N+1 problem.
     * This method calls the standard findAll() and then accesses employees,
     * causing N+1 queries to be executed.
     *
     * @return list of departments with employees (causes N+1 queries)
     */
    List<DepartmentWithEmployeesDto> getAllDepartmentsWithNPlusOneProblem();

    /**
     * Solution 1: Uses JOIN FETCH to load employees in a single query.
     *
     * @return list of departments with employees (single query)
     */
    List<DepartmentWithEmployeesDto> getAllDepartmentsWithJoinFetch();

    /**
     * Solution 2: Uses @EntityGraph to eagerly fetch employees.
     *
     * @return list of departments with employees (single query with EntityGraph)
     */
    List<DepartmentWithEmployeesDto> getAllDepartmentsWithEntityGraph();

    /**
     * Solution 3: Uses DTO projection to get department with employee count.
     * Most efficient when full employee data is not needed.
     *
     * @return list of department DTOs with employee count (optimized query)
     */
    List<DepartmentDto> getAllDepartmentsWithProjection();

    /**
     * Get a single department by ID with employees.
     *
     * @param id department ID
     * @return department with employees
     */
    DepartmentWithEmployeesDto getDepartmentById(Long id);
}
