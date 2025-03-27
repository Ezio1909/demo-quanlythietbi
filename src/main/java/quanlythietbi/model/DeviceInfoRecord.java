package quanlythietbi.model;

import java.util.Objects;

public record DeviceInfoRecord(
    Integer deviceId,
    String deviceName,
    String deviceType
) {
    public DeviceInfoRecord {
        Objects.requireNonNull(deviceId);
    }
}
