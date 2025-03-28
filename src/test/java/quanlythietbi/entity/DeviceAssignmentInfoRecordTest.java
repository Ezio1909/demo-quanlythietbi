package quanlythietbi.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class DeviceAssignmentInfoRecordTest {

    @Test
    public void testDeviceAssignmentInfoRecordSuccess() {
        LocalDateTime now = LocalDateTime.now();
        DeviceAssignmentInfoRecord record = new DeviceAssignmentInfoRecord(100, 1, 2, now, null);
        assertEquals(100, record.id());
        assertEquals(1, record.employeeId());
        assertEquals(2, record.deviceId());
        assertEquals(now, record.assignedAt());
        assertNull(record.returnedAt());
    }

    @Test
    public void testDeviceAssignmentInfoRecordWithNullId() {
        assertThrows(NullPointerException.class,
            () -> new DeviceAssignmentInfoRecord(null, 1, 2, null, null));
    }
}