-- Sample data for N+1 problem demonstration
-- This data will be loaded automatically when using H2 in-memory database
-- For MySQL, you may need to run this script manually or configure spring.datasource.initialization-mode=always

-- Insert 5 Departments
INSERT INTO department (id, name) VALUES (1, 'Engineering');
INSERT INTO department (id, name) VALUES (2, 'Marketing');
INSERT INTO department (id, name) VALUES (3, 'Sales');
INSERT INTO department (id, name) VALUES (4, 'Human Resources');
INSERT INTO department (id, name) VALUES (5, 'Finance');

-- Insert 20 Employees distributed across departments

-- Engineering Department (6 employees)
INSERT INTO employee (id, name, email, salary, department_id) VALUES (1, 'John Smith', 'john.smith@company.com', 85000.00, 1);
INSERT INTO employee (id, name, email, salary, department_id) VALUES (2, 'Jane Doe', 'jane.doe@company.com', 90000.00, 1);
INSERT INTO employee (id, name, email, salary, department_id) VALUES (3, 'Bob Johnson', 'bob.johnson@company.com', 75000.00, 1);
INSERT INTO employee (id, name, email, salary, department_id) VALUES (4, 'Alice Williams', 'alice.williams@company.com', 95000.00, 1);
INSERT INTO employee (id, name, email, salary, department_id) VALUES (5, 'Charlie Brown', 'charlie.brown@company.com', 72000.00, 1);
INSERT INTO employee (id, name, email, salary, department_id) VALUES (6, 'Diana Prince', 'diana.prince@company.com', 88000.00, 1);

-- Marketing Department (4 employees)
INSERT INTO employee (id, name, email, salary, department_id) VALUES (7, 'Mike Wilson', 'mike.wilson@company.com', 65000.00, 2);
INSERT INTO employee (id, name, email, salary, department_id) VALUES (8, 'Sarah Davis', 'sarah.davis@company.com', 70000.00, 2);
INSERT INTO employee (id, name, email, salary, department_id) VALUES (9, 'Tom Martinez', 'tom.martinez@company.com', 68000.00, 2);
INSERT INTO employee (id, name, email, salary, department_id) VALUES (10, 'Emily Garcia', 'emily.garcia@company.com', 72000.00, 2);

-- Sales Department (5 employees)
INSERT INTO employee (id, name, email, salary, department_id) VALUES (11, 'David Lee', 'david.lee@company.com', 60000.00, 3);
INSERT INTO employee (id, name, email, salary, department_id) VALUES (12, 'Lisa Anderson', 'lisa.anderson@company.com', 75000.00, 3);
INSERT INTO employee (id, name, email, salary, department_id) VALUES (13, 'Chris Taylor', 'chris.taylor@company.com', 55000.00, 3);
INSERT INTO employee (id, name, email, salary, department_id) VALUES (14, 'Amy Thomas', 'amy.thomas@company.com', 62000.00, 3);
INSERT INTO employee (id, name, email, salary, department_id) VALUES (15, 'Kevin Jackson', 'kevin.jackson@company.com', 58000.00, 3);

-- Human Resources Department (2 employees)
INSERT INTO employee (id, name, email, salary, department_id) VALUES (16, 'Nancy White', 'nancy.white@company.com', 70000.00, 4);
INSERT INTO employee (id, name, email, salary, department_id) VALUES (17, 'Peter Harris', 'peter.harris@company.com', 65000.00, 4);

-- Finance Department (3 employees)
INSERT INTO employee (id, name, email, salary, department_id) VALUES (18, 'Rachel Clark', 'rachel.clark@company.com', 80000.00, 5);
INSERT INTO employee (id, name, email, salary, department_id) VALUES (19, 'Steven Lewis', 'steven.lewis@company.com', 82000.00, 5);
INSERT INTO employee (id, name, email, salary, department_id) VALUES (20, 'Karen Robinson', 'karen.robinson@company.com', 78000.00, 5);
