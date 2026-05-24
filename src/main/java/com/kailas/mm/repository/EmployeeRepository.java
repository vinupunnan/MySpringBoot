package com.kailas.mm.repository;

import com.kailas.mm.model.entity.sql.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    List<Employee> findBySalary(int sal);

    @Query(value = "select e from Employee e where e.salary > :salary")
    List<Employee> findBySalaryByJpql(@Param("salary") int sal);

    @Query(value = "select * from employees  where salary > ?", nativeQuery = true)
    List<Employee> findByNormalQuery(int sal);
}
