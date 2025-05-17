package quanlythietbi.entity;

import java.util.Objects;

public record DeviceInfoRecord(
    Integer id,
    String name,
    String type,
    String serialNumber,
    String status
) {
    public DeviceInfoRecord {
        // id can be null for new devices
        Objects.requireNonNull(name, "Device name cannot be null");
        Objects.requireNonNull(type, "Device type cannot be null");
        Objects.requireNonNull(serialNumber, "Serial number cannot be null");
        Objects.requireNonNull(status, "Status cannot be null");
    }
}