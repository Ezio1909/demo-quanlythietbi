package quanlythietbi.model;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.jupiter.api.Assertions.*;

public class DeviceInfoRecordTest {

    private static final Logger logger = LoggerFactory.getLogger(DeviceInfoRecordTest.class);

    @Test
    public void testDeviceInfoRecordSuccess() {
        DeviceInfoRecord record = new DeviceInfoRecord(1, "abc", "cde", null, null);
        logger.info(record.toString());
        assertEquals(1, record.id());
        assertEquals("abc", record.name());
        assertEquals("cde", record.type());
    }

    @Test
    public void testDeviceInfoRecordWithNullDeviceId() {
        assertThrows(
            NullPointerException.class,
            () -> new DeviceInfoRecord(null, "abc", "cde", null, null)
        );
    }
}
