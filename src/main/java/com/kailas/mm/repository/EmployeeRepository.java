package com.kailas.mm.repository;

import com.kailas.mm.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Employee entity with basic CRUD operations.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Find all employees by department ID.
     */
    List<Employee> findByDepartmentId(Long departmentId);

    /**
     * Find employees by department ID with department eagerly loaded.
     */
    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.department WHERE e.department.id = :departmentId")
    List<Employee> findByDepartmentIdWithDepartment(Long departmentId);

    /**
     * Find employee by email.
     */
    Employee findByEmail(String email);
}
