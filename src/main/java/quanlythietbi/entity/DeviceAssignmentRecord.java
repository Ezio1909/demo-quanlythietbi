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
    String status
) {
    public DeviceAssignmentRecord {
        Objects.requireNonNull(id);
        Objects.requireNonNull(employeeId);
        Objects.requireNonNull(deviceId);
    }
} 