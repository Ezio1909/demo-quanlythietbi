package quanlythietbi.entity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class DeviceAssignmentInfoRecordTest {

    @Test
    public void testDeviceAssignmentInfoRecordSuccess() {
        LocalDateTime now = LocalDateTime.now();
        DeviceAssignmentRecord record = new DeviceAssignmentRecord(100, 1, 2, "Test Device", "John Doe", "IT", now, null, "Active");
        assertEquals(100, record.id());
        assertEquals(1, record.employeeId());
        assertEquals(2, record.deviceId());
        assertEquals("Test Device", record.deviceName());
        assertEquals("John Doe", record.employeeName());
        assertEquals("IT", record.department());
        assertEquals(now, record.assignedAt());
        assertNull(record.returnedAt());
        assertEquals("Active", record.status());
    }

    @Test
    public void testDeviceAssignmentInfoRecordWithNullId() {
        assertThrows(NullPointerException.class,
            () -> new DeviceAssignmentRecord(null, null, null, null, null, null, null, null, null));
    }
}