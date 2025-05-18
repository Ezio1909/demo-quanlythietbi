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
        LocalDateTime expiration = now.plusYears(1);
        DeviceAssignmentRecord record = new DeviceAssignmentRecord(
            100, 1, 2, "Test Device", "John Doe", "IT", 
            now, null, expiration, "Active"
        );
        
        assertEquals(100, record.id());
        assertEquals(1, record.employeeId());
        assertEquals(2, record.deviceId());
        assertEquals("Test Device", record.deviceName());
        assertEquals("John Doe", record.employeeName());
        assertEquals("IT", record.department());
        assertEquals(now, record.assignedAt());
        assertNull(record.returnedAt());
        assertEquals(expiration, record.expirationDate());
        assertEquals("Active", record.status());
    }

    @Test
    public void testDeviceAssignmentInfoRecordWithNullRequiredFields() {
        LocalDateTime now = LocalDateTime.now();
        
        assertThrows(NullPointerException.class, () -> 
            new DeviceAssignmentRecord(null, null, null, "Test", "John", "IT", 
                null, null, null, "Active")
        );
        
        // Test with null employeeId
        assertThrows(NullPointerException.class, () -> 
            new DeviceAssignmentRecord(1, null, 2, "Test", "John", "IT", 
                now, null, now.plusYears(1), "Active")
        );
        
        // Test with null deviceId
        assertThrows(NullPointerException.class, () -> 
            new DeviceAssignmentRecord(1, 2, null, "Test", "John", "IT", 
                now, null, now.plusYears(1), "Active")
        );
        
        // Test with null assignedAt
        assertThrows(NullPointerException.class, () -> 
            new DeviceAssignmentRecord(1, 2, 3, "Test", "John", "IT", 
                null, null, now.plusYears(1), "Active")
        );
        
        // Test with null expirationDate
        assertThrows(NullPointerException.class, () -> 
            new DeviceAssignmentRecord(1, 2, 3, "Test", "John", "IT", 
                now, null, null, "Active")
        );
    }
}