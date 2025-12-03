package com.kailas.mm.model.projection;

/**
 * Projection interface for Department with employee count.
 * Used with Spring Data JPA interface-based projection for optimized queries.
 */
public interface DepartmentProjection {

    /**
     * Get department ID.
     */
    Long getId();

    /**
     * Get department name.
     */
    String getName();

    /**
     * Get the count of employees in this department.
     */
    Long getEmployeeCount();
}
