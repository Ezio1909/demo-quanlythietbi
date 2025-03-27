package quanlythietbi.model;

import java.util.Objects;

public record EmployeeInfoRecord(
    Integer id,
    String name,
    String email,
    String department
) {
    public EmployeeInfoRecord {
        Objects.requireNonNull(id);
    }
}