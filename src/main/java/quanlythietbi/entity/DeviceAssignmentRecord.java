package quanlythietbi.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public record DeviceAssignmentRecord(
    Integer id,
    Integer employeeId,
    Integer deviceId,
    String deviceName,
    String employeeName,
    String department,
    LocalDateTime assignedAt,
    LocalDateTime returnedAt,
    LocalDateTime expirationDate,
    String status
) {
    public DeviceAssignmentRecord {
        // id can be null for new records
        Objects.requireNonNull(employeeId, "Employee ID cannot be null");
        Objects.requireNonNull(deviceId, "Device ID cannot be null");
        Objects.requireNonNull(assignedAt, "Assigned date cannot be null");
        Objects.requireNonNull(expirationDate, "Expiration date cannot be null");
    }
} 