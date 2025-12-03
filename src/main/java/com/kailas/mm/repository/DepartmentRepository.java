package com.kailas.mm.repository;

import com.kailas.mm.entity.Department;
import com.kailas.mm.model.projection.DepartmentProjection;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Department entity with multiple methods demonstrating
 * different approaches to solve the N+1 problem.
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    /**
     * Standard findAll() - This demonstrates the N+1 problem.
     * When you iterate over departments and access employees,
     * JPA will execute N additional queries (one for each department).
     * 
     * Query pattern:
     * 1 query to fetch all departments +
     * N queries to fetch employees for each department = N+1 queries
     */
    List<Department> findAll();

    /**
     * Solution 1: Using JOIN FETCH in JPQL.
     * Fetches departments and their employees in a single query.
     * 
     * Note: This may result in cartesian product for multiple collections.
     * Use with DISTINCT to avoid duplicate departments.
     */
    @Query("SELECT DISTINCT d FROM Department d LEFT JOIN FETCH d.employees")
    List<Department> findAllWithEmployees();

    /**
     * Solution 2: Using @EntityGraph to eagerly fetch employees.
     * This is a declarative way to define fetch plans.
     * 
     * Equivalent to JOIN FETCH but more flexible and reusable.
     */
    @EntityGraph(attributePaths = {"employees"})
    @Query("SELECT d FROM Department d")
    List<Department> findAllWithEmployeesEntityGraph();

    /**
     * Solution 3: Using DTO projection to fetch only required data.
     * This is the most efficient when you don't need full entity data.
     * Avoids loading unnecessary columns and relationships.
     */
    @Query("SELECT d.id as id, d.name as name, SIZE(d.employees) as employeeCount FROM Department d GROUP BY d.id, d.name")
    List<DepartmentProjection> findAllDepartmentsWithEmployeeCount();

    /**
     * Find department by ID with employees eagerly loaded.
     */
    @Query("SELECT d FROM Department d LEFT JOIN FETCH d.employees WHERE d.id = :id")
    Optional<Department> findByIdWithEmployees(Long id);
}
