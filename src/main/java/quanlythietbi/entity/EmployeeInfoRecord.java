package quanlythietbi.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public record EmployeeInfoRecord(
    Integer id,
    String name,
    String email,
    String department,
    String position,
    String phone,
    LocalDate hireDate,
    String employeeIdNumber,
    Integer managerId,
    String status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public EmployeeInfoRecord {
        // ID can be null for new records
        Objects.requireNonNull(name, "Employee name cannot be null");
        Objects.requireNonNull(email, "Employee email cannot be null");
        Objects.requireNonNull(department, "Employee department cannot be null");
        // Optional fields are allowed to be null
    }
    
    // Constructor for basic employee creation
    public EmployeeInfoRecord(String name, String email, String department) {
        this(null, name, email, department,
             null, null, null, null, null,
             "Active", LocalDateTime.now(), LocalDateTime.now());
    }
    
    // Constructor for minimal employee creation with ID
    public EmployeeInfoRecord(Integer id, String name, String email, String department) {
        this(id, name, email, department,
             null, null, null, null, null,
             "Active", LocalDateTime.now(), LocalDateTime.now());
    }
}