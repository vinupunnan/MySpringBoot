package com.kailas.mm.service.impl;

import com.kailas.mm.entity.Department;
import com.kailas.mm.model.dto.DepartmentDto;
import com.kailas.mm.model.dto.DepartmentWithEmployeesDto;
import com.kailas.mm.model.projection.DepartmentProjection;
import com.kailas.mm.repository.DepartmentRepository;
import com.kailas.mm.service.DepartmentService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of DepartmentService demonstrating the N+1 problem and solutions.
 * 
 * The N+1 problem occurs when:
 * 1. You fetch a list of N parent entities (1 query)
 * 2. For each parent, you access a lazy-loaded child collection
 * 3. JPA executes N additional queries to load the children
 * 
 * Total: N + 1 queries instead of 1 optimal query
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    /**
     * DEMONSTRATES THE N+1 PROBLEM
     * 
     * This method shows the N+1 problem in action:
     * 1. First query: SELECT * FROM department (fetches N departments)
     * 2. For each department, when we access getEmployees():
     *    SELECT * FROM employee WHERE department_id = ? (N queries)
     * 
     * Total: N + 1 queries
     * 
     * Example with 5 departments:
     * - 1 query for all departments
     * - 5 queries for employees (one per department)
     * - Total: 6 queries
     * 
     * Watch the console logs to see each query being executed!
     */
    @Override
    @Transactional(readOnly = true)
    public List<DepartmentWithEmployeesDto> getAllDepartmentsWithNPlusOneProblem() {
        logger.info("=== DEMONSTRATING N+1 PROBLEM ===");
        logger.info("Query 1: Fetching all departments...");
        
        // This executes 1 query to fetch all departments
        List<Department> departments = departmentRepository.findAll();
        
        logger.info("Found {} departments", departments.size());
        logger.info("Now accessing employees for each department (watch for N additional queries)...");
        
        // Converting to DTO triggers lazy loading of employees
        // Each getEmployees() call will execute a separate query!
        return departments.stream()
                .map(dept -> {
                    logger.info("Loading employees for department: {} (ID: {})", dept.getName(), dept.getId());
                    return new DepartmentWithEmployeesDto(dept);
                })
                .collect(Collectors.toList());
    }

    /**
     * SOLUTION 1: JOIN FETCH
     * 
     * Uses JPQL JOIN FETCH to load departments and employees in a single query:
     * SELECT DISTINCT d FROM Department d LEFT JOIN FETCH d.employees
     * 
     * This generates a single SQL query with JOIN:
     * SELECT d.*, e.* FROM department d 
     * LEFT OUTER JOIN employee e ON d.id = e.department_id
     * 
     * Pros:
     * - Single query, most efficient for loading full entity graphs
     * - No additional configuration needed
     * 
     * Cons:
     * - Can cause cartesian product issues with multiple collections
     * - Returns duplicate parent rows (use DISTINCT)
     * - Query becomes complex with multiple JOINs
     */
    @Override
    @Transactional(readOnly = true)
    public List<DepartmentWithEmployeesDto> getAllDepartmentsWithJoinFetch() {
        logger.info("=== SOLUTION 1: JOIN FETCH ===");
        logger.info("Executing single query with JOIN FETCH...");
        
        // Single query with JOIN FETCH
        List<Department> departments = departmentRepository.findAllWithEmployees();
        
        logger.info("Fetched {} departments with employees in a single query", departments.size());
        
        // No additional queries when accessing employees - already loaded!
        return departments.stream()
                .map(DepartmentWithEmployeesDto::new)
                .collect(Collectors.toList());
    }

    /**
     * SOLUTION 2: @EntityGraph
     * 
     * Uses @EntityGraph annotation to define fetch plan declaratively.
     * Achieves the same result as JOIN FETCH but more flexible:
     * 
     * @EntityGraph(attributePaths = {"employees"})
     * 
     * Pros:
     * - Declarative and reusable
     * - Can be combined with any query method
     * - Cleaner code than JPQL with JOIN FETCH
     * - Can define named entity graphs on entities
     * 
     * Cons:
     * - Same cartesian product issues as JOIN FETCH
     * - Slightly more verbose for simple cases
     */
    @Override
    @Transactional(readOnly = true)
    public List<DepartmentWithEmployeesDto> getAllDepartmentsWithEntityGraph() {
        logger.info("=== SOLUTION 2: @EntityGraph ===");
        logger.info("Executing query with @EntityGraph...");
        
        // Single query using EntityGraph
        List<Department> departments = departmentRepository.findAllWithEmployeesEntityGraph();
        
        logger.info("Fetched {} departments with employees using EntityGraph", departments.size());
        
        // No additional queries - employees already loaded via EntityGraph
        return departments.stream()
                .map(DepartmentWithEmployeesDto::new)
                .collect(Collectors.toList());
    }

    /**
     * SOLUTION 3: DTO PROJECTION
     * 
     * Uses interface-based projection to select only required fields:
     * SELECT d.id, d.name, COUNT(e.id) FROM department d 
     * LEFT JOIN employee e ON d.id = e.department_id
     * GROUP BY d.id, d.name
     * 
     * Pros:
     * - Most efficient when full entity data is not needed
     * - Reduces memory footprint
     * - No lazy loading issues
     * - Clean and type-safe interface
     * 
     * Cons:
     * - Cannot modify or save projected data
     * - Need to define projection interface/DTO
     * - Less flexible than loading full entities
     * 
     * Best for: Read-only scenarios, reports, dashboards
     */
    @Override
    @Transactional(readOnly = true)
    public List<DepartmentDto> getAllDepartmentsWithProjection() {
        logger.info("=== SOLUTION 3: DTO PROJECTION ===");
        logger.info("Executing optimized projection query...");
        
        // Single optimized query returning only needed data
        List<DepartmentProjection> projections = departmentRepository.findAllDepartmentsWithEmployeeCount();
        
        logger.info("Fetched {} department projections", projections.size());
        
        // Convert projection to DTO
        return projections.stream()
                .map(DepartmentDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Get a single department by ID with employees loaded.
     * Uses JOIN FETCH for efficient loading.
     */
    @Override
    @Transactional(readOnly = true)
    public DepartmentWithEmployeesDto getDepartmentById(Long id) {
        logger.info("Fetching department with ID: {}", id);
        
        return departmentRepository.findByIdWithEmployees(id)
                .map(DepartmentWithEmployeesDto::new)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
    }
}
