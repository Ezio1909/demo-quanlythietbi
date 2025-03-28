package quanlythietbi.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmployeeInfoRecordTest {

    @Test
    public void testEmployeeInfoRecordSuccess() {
        EmployeeInfoRecord record = new EmployeeInfoRecord(1, "Alice", "alice@example.com", "HR");
        assertEquals(1, record.id());
        assertEquals("Alice", record.name());
        assertEquals("alice@example.com", record.email());
        assertEquals("HR", record.department());
    }

    @Test
    public void testEmployeeInfoRecordWithNullId() {
        assertThrows(NullPointerException.class,
            () -> new EmployeeInfoRecord(null, null, null, null));
    }
}