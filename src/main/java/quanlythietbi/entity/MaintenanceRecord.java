package quanlythietbi.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MaintenanceRecord(
    Integer id,
    Integer deviceId,
    String deviceName,  // For UI display, joined from devices table
    String maintenanceType,
    String description,
    LocalDateTime reportedAt,
    LocalDateTime scheduledFor,
    LocalDateTime completedAt,
    BigDecimal cost,
    String status,
    String notes
) {} 