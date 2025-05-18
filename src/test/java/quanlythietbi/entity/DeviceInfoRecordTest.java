package quanlythietbi.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeviceInfoRecordTest {

    private static final Logger logger = LoggerFactory.getLogger(DeviceInfoRecordTest.class);

    @Test
    public void testDeviceInfoRecordSuccess() {
        DeviceInfoRecord record = new DeviceInfoRecord(1, "Test Device", "Laptop", "SN123", "Available");
        logger.info(record.toString());
        assertEquals(1, record.id());
        assertEquals("Test Device", record.name());
        assertEquals("Laptop", record.type());
        assertEquals("SN123", record.serialNumber());
        assertEquals("Available", record.status());
    }

    @Test
    public void testDeviceInfoRecordWithNullRequiredField() {
        assertThrows(
            NullPointerException.class,
            () -> new DeviceInfoRecord(1, null, "Laptop", "SN123", "Available"),
            "Device name cannot be null"
        );
    }
}
