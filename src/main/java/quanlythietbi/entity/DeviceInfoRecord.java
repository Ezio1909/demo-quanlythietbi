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
        Objects.requireNonNull(id);
    }
}