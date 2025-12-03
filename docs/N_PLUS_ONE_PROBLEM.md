# N+1 Problem in JPA/Hibernate

## Table of Contents
1. [What is the N+1 Problem?](#what-is-the-n1-problem)
2. [Understanding the Problem](#understanding-the-problem)
3. [Solutions Overview](#solutions-overview)
4. [Solution 1: JOIN FETCH](#solution-1-join-fetch)
5. [Solution 2: @EntityGraph](#solution-2-entitygraph)
6. [Solution 3: DTO Projection](#solution-3-dto-projection)
7. [Solution 4: Batch Fetching](#solution-4-batch-fetching)
8. [Comparison Table](#comparison-table)
9. [How to Detect N+1 Issues](#how-to-detect-n1-issues)
10. [Best Practices](#best-practices)

---

## What is the N+1 Problem?

The N+1 problem is a common performance anti-pattern in ORM frameworks like JPA/Hibernate. It occurs when your application executes:
- **1 query** to fetch a list of parent entities
- **N additional queries** to fetch related child entities (one for each parent)

This results in **N+1 total queries** instead of an optimal **1-2 queries**.

---

## Understanding the Problem

### Entity Relationships

```java
@Entity
public class Department {
    @Id
    private Long id;
    private String name;
    
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private List<Employee> employees;
}

@Entity
public class Employee {
    @Id
    private Long id;
    private String name;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Department department;
}
```

### The Problem in Action

```java
// Query 1: Fetch all departments
List<Department> departments = departmentRepository.findAll();
// SQL: SELECT * FROM department

// For each department, accessing employees triggers another query
for (Department dept : departments) {
    List<Employee> employees = dept.getEmployees(); // Query 2, 3, 4, 5, 6...
    // SQL: SELECT * FROM employee WHERE department_id = ?
}
```

### SQL Queries Generated (N+1 Problem)

With 5 departments, you'll see these queries:

```sql
-- Query 1: Get all departments
SELECT d.id, d.name FROM department d

-- Query 2: Get employees for department 1
SELECT e.* FROM employee e WHERE e.department_id = 1

-- Query 3: Get employees for department 2
SELECT e.* FROM employee e WHERE e.department_id = 2

-- Query 4: Get employees for department 3
SELECT e.* FROM employee e WHERE e.department_id = 3

-- Query 5: Get employees for department 4
SELECT e.* FROM employee e WHERE e.department_id = 4

-- Query 6: Get employees for department 5
SELECT e.* FROM employee e WHERE e.department_id = 5
```

**Total: 6 queries (1 + 5)** instead of 1!

---

## Solutions Overview

| Solution | Best For | Query Count | Memory Usage |
|----------|----------|-------------|--------------|
| JOIN FETCH | Loading full entity graphs | 1 | Higher |
| @EntityGraph | Declarative fetch plans | 1 | Higher |
| DTO Projection | Read-only, specific fields | 1 | Lower |
| Batch Fetching | Large datasets | 1 + N/batch | Moderate |

---

## Solution 1: JOIN FETCH

### Implementation

```java
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
    @Query("SELECT DISTINCT d FROM Department d LEFT JOIN FETCH d.employees")
    List<Department> findAllWithEmployees();
}
```

### SQL Generated

```sql
SELECT DISTINCT 
    d.id, d.name, 
    e.id, e.name, e.email, e.salary, e.department_id
FROM department d 
LEFT OUTER JOIN employee e ON d.id = e.department_id
```

### Pros
- Single query
- Loads full entity graph
- No additional configuration needed

### Cons
- Can cause Cartesian product with multiple collections
- Use DISTINCT to avoid duplicate parent rows
- Not suitable for pagination

---

## Solution 2: @EntityGraph

### Implementation

```java
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
    @EntityGraph(attributePaths = {"employees"})
    @Query("SELECT d FROM Department d")
    List<Department> findAllWithEmployeesEntityGraph();
}
```

### Alternative: Named Entity Graph

```java
@Entity
@NamedEntityGraph(
    name = "Department.withEmployees",
    attributeNodes = @NamedAttributeNode("employees")
)
public class Department { ... }

// Repository
@EntityGraph(value = "Department.withEmployees", type = EntityGraph.EntityGraphType.LOAD)
List<Department> findAll();
```

### SQL Generated

Same as JOIN FETCH - a single JOIN query.

### Pros
- Declarative and reusable
- Can be combined with any query method
- Supports complex graphs with subgraphs

### Cons
- Same Cartesian product issues as JOIN FETCH
- Slightly more verbose for simple cases

---

## Solution 3: DTO Projection

### Interface-Based Projection

```java
public interface DepartmentProjection {
    Long getId();
    String getName();
    Long getEmployeeCount();
}

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
    @Query("SELECT d.id as id, d.name as name, " +
           "(SELECT COUNT(e) FROM Employee e WHERE e.department = d) as employeeCount " +
           "FROM Department d")
    List<DepartmentProjection> findAllDepartmentsWithEmployeeCount();
}
```

### SQL Generated

```sql
SELECT 
    d.id as id, 
    d.name as name, 
    (SELECT COUNT(e.id) FROM employee e WHERE e.department_id = d.id) as employeeCount
FROM department d
```

### Pros
- Most efficient for read-only scenarios
- Minimal memory footprint
- No lazy loading issues
- Perfect for reports and dashboards

### Cons
- Cannot modify projected data
- Need separate projection class/interface
- Less flexible than full entities

---

## Solution 4: Batch Fetching

### Configuration (Hibernate)

```java
@Entity
public class Department {
    
    @OneToMany(mappedBy = "department")
    @org.hibernate.annotations.BatchSize(size = 10)
    private List<Employee> employees;
}
```

Or globally in `application.yml`:

```yaml
spring:
  jpa:
    properties:
      hibernate:
        default_batch_fetch_size: 10
```

### SQL Generated

```sql
-- Query 1: Get all departments
SELECT * FROM department

-- Query 2: Batch fetch employees for 10 departments at once
SELECT * FROM employee WHERE department_id IN (1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
```

### Pros
- Works with lazy loading
- Reduces N+1 to N/batch + 1
- Minimal code changes

### Cons
- Still multiple queries (just fewer)
- Requires tuning batch size
- Less predictable than explicit fetch

---

## Comparison Table

| Aspect | N+1 Problem | JOIN FETCH | @EntityGraph | Projection | Batch Fetch |
|--------|-------------|------------|--------------|------------|-------------|
| **Query Count** | N+1 | 1 | 1 | 1 | N/batch + 1 |
| **Performance** | Poor | Good | Good | Best | Moderate |
| **Memory** | Low (lazy) | High | High | Lowest | Moderate |
| **Use Case** | Never | Full graph | Full graph | Read-only | Large data |
| **Pagination** | Works | Fails | Fails | Works | Works |
| **Code Changes** | None | Query | Annotation | DTO + Query | Config |

---

## How to Detect N+1 Issues

### 1. Enable SQL Logging

```yaml
# application.yml
spring:
  jpa:
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        use_sql_comments: true

logging:
  level:
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
```

### 2. Use Query Counters

Add Hibernate Statistics:

```java
@Bean
public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
    return hibernateProperties -> {
        hibernateProperties.put("hibernate.generate_statistics", true);
    };
}
```

### 3. Use p6spy or datasource-proxy

Add dependency for detailed SQL logging with timing:

```xml
<dependency>
    <groupId>com.github.gavlyukovskiy</groupId>
    <artifactId>datasource-proxy-spring-boot-starter</artifactId>
    <version>1.8.1</version>
</dependency>
```

### 4. Look for Patterns in Logs

Warning signs:
- Repeated similar SELECT statements
- Same query pattern with different WHERE values
- High query count for simple operations

---

## Best Practices

### 1. Default to LAZY Fetching

```java
@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
private List<Child> children;
```

### 2. Use JOIN FETCH for Known Access Patterns

```java
@Query("SELECT p FROM Parent p LEFT JOIN FETCH p.children WHERE p.id = :id")
Optional<Parent> findByIdWithChildren(Long id);
```

### 3. Consider DTOs for Read-Only Operations

```java
// Best for lists, reports, dashboards
@Query("SELECT new com.example.ParentSummary(p.id, p.name, SIZE(p.children)) FROM Parent p")
List<ParentSummary> findAllSummaries();
```

### 4. Use @EntityGraph for Flexible Fetch Plans

```java
@EntityGraph(attributePaths = {"children", "children.grandchildren"})
List<Parent> findAll();
```

### 5. Enable Batch Fetching as Fallback

```yaml
spring:
  jpa:
    properties:
      hibernate:
        default_batch_fetch_size: 100
```

### 6. Test with Real Data Volumes

N+1 might be acceptable for 10 records but catastrophic for 10,000.

### 7. Monitor in Production

Use APM tools to track query counts and response times.

---

## Testing the Implementation

### Endpoints

| Endpoint | Description |
|----------|-------------|
| `GET /departments/n-plus-one` | Demonstrates N+1 problem |
| `GET /departments/join-fetch` | Solution with JOIN FETCH |
| `GET /departments/entity-graph` | Solution with @EntityGraph |
| `GET /departments/projection` | Solution with DTO projection |
| `GET /departments/{id}` | Single department with employees |

### Test Commands

```bash
# Demonstrate N+1 problem (watch logs for multiple queries)
curl http://localhost:8080/departments/n-plus-one

# Test JOIN FETCH solution (single query)
curl http://localhost:8080/departments/join-fetch

# Test EntityGraph solution (single query)
curl http://localhost:8080/departments/entity-graph

# Test Projection solution (optimized single query)
curl http://localhost:8080/departments/projection

# Get single department
curl http://localhost:8080/departments/1
```

---

## References

- [Hibernate Documentation - Fetching](https://docs.jboss.org/hibernate/orm/5.4/userguide/html_single/Hibernate_User_Guide.html#fetching)
- [Spring Data JPA - Entity Graphs](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.entity-graph)
- [Vlad Mihalcea - N+1 Query Problem](https://vladmihalcea.com/n-plus-1-query-problem/)
