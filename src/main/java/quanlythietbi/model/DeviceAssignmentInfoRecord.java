package quanlythietbi.model;

import java.time.LocalDateTime;
import java.util.Objects;

public record DeviceAssignmentInfoRecord(
    Integer id,
    Integer employeeId,
    Integer deviceId,
    LocalDateTime assignedAt,
    LocalDateTime returnedAt // Can be null
) {
    public DeviceAssignmentInfoRecord {
        Objects.requireNonNull(id);
    }
}