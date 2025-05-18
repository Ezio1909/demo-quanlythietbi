package quanlythietbi.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class EmployeeInfoRecordTest {

    @Test
    public void testBasicEmployeeInfoRecordSuccess() {
        // Test basic constructor
        EmployeeInfoRecord record = new EmployeeInfoRecord("Alice", "alice@example.com", "HR");
        assertNull(record.id());
        assertEquals("Alice", record.name());
        assertEquals("alice@example.com", record.email());
        assertEquals("HR", record.department());
        assertNull(record.position());
        assertNull(record.phone());
        assertNull(record.hireDate());
        assertNull(record.employeeIdNumber());
        assertNull(record.managerId());
        assertEquals("Active", record.status());
        assertNotNull(record.createdAt());
        assertNotNull(record.updatedAt());
    }

    @Test
    public void testMinimalEmployeeInfoRecordSuccess() {
        // Test minimal constructor with ID
        EmployeeInfoRecord record = new EmployeeInfoRecord(1, "Alice", "alice@example.com", "HR");
        assertEquals(1, record.id());
        assertEquals("Alice", record.name());
        assertEquals("alice@example.com", record.email());
        assertEquals("HR", record.department());
        assertNull(record.position());
        assertNull(record.phone());
        assertNull(record.hireDate());
        assertNull(record.employeeIdNumber());
        assertNull(record.managerId());
        assertEquals("Active", record.status());
        assertNotNull(record.createdAt());
        assertNotNull(record.updatedAt());
    }

    @Test
    public void testFullEmployeeInfoRecordSuccess() {
        // Test full constructor with all fields
        LocalDateTime now = LocalDateTime.now();
        LocalDate hireDate = LocalDate.of(2022, 1, 15);
        
        EmployeeInfoRecord record = new EmployeeInfoRecord(
            1,
            "Alice",
            "alice@example.com",
            "HR",
            "Manager",
            "123-456-7890",
            hireDate,
            "EMP001",
            2,
            "Active",
            now,
            now
        );

        assertEquals(1, record.id());
        assertEquals("Alice", record.name());
        assertEquals("alice@example.com", record.email());
        assertEquals("HR", record.department());
        assertEquals("Manager", record.position());
        assertEquals("123-456-7890", record.phone());
        assertEquals(hireDate, record.hireDate());
        assertEquals("EMP001", record.employeeIdNumber());
        assertEquals(2, record.managerId());
        assertEquals("Active", record.status());
        assertEquals(now, record.createdAt());
        assertEquals(now, record.updatedAt());
    }

    @Test
    public void testEmployeeInfoRecordWithNullId() {
        // ID can be null for new records
        EmployeeInfoRecord record = new EmployeeInfoRecord(
            null, "Alice", "alice@example.com", "HR",
            null, null, null, null, null,
            null, null, null
        );
        assertNull(record.id());
    }

    @Test
    public void testEmployeeInfoRecordWithNullRequiredFields() {
        assertThrows(NullPointerException.class,
            () -> new EmployeeInfoRecord(
                1, null, "alice@example.com", "HR",
                null, null, null, null, null,
                null, null, null
            ),
            "Employee name cannot be null"
        );

        assertThrows(NullPointerException.class,
            () -> new EmployeeInfoRecord(
                1, "Alice", null, "HR",
                null, null, null, null, null,
                null, null, null
            ),
            "Employee email cannot be null"
        );

        assertThrows(NullPointerException.class,
            () -> new EmployeeInfoRecord(
                1, "Alice", "alice@example.com", null,
                null, null, null, null, null,
                null, null, null
            ),
            "Employee department cannot be null"
        );
    }
}