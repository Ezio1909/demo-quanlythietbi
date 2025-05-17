package quanlythietbi.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public record DeviceInfoRecord(
    Integer id,
    String name,
    String type,
    String serialNumber,
    String status,
    
    // Purchase Information
    LocalDate purchaseDate,
    BigDecimal purchasePrice,
    String supplier,
    LocalDate warrantyExpiry,
    
    // Technical Information
    String model,
    String manufacturer,
    String specifications,
    String firmwareVersion,
    
    // Asset Management
    String assetTag,
    String location,
    String department,
    String category,
    
    // Lifecycle Management
    LocalDate lastInspectionDate,
    LocalDate nextInspectionDate,
    LocalDate endOfLifeDate,
    String condition,
    String notes,
    
    // Timestamps and Audit
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String createdBy,
    String lastModifiedBy
) {
    public DeviceInfoRecord {
        // Required fields
        Objects.requireNonNull(name, "Device name cannot be null");
        Objects.requireNonNull(type, "Device type cannot be null");
        Objects.requireNonNull(serialNumber, "Serial number cannot be null");
        Objects.requireNonNull(status, "Status cannot be null");
        
        // Optional fields are allowed to be null
    }
    
    // Constructor for basic device creation
    public DeviceInfoRecord(String name, String type, String serialNumber) {
        this(null, name, type, serialNumber, "Available",
             null, null, null, null,
             null, null, null, null,
             null, null, null, null,
             null, null, null, "New", null,
             LocalDateTime.now(), LocalDateTime.now(), null, null);
    }
    
    // Constructor for minimal device creation with status
    public DeviceInfoRecord(Integer id, String name, String type, String serialNumber, String status) {
        this(id, name, type, serialNumber, status,
             null, null, null, null,
             null, null, null, null,
             null, null, null, null,
             null, null, null, "New", null,
             LocalDateTime.now(), LocalDateTime.now(), null, null);
    }
}