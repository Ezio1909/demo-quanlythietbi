package quanlythietbi.service.assignment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import quanlythietbi.connector.IConnectionManager;
import quanlythietbi.entity.DeviceAssignmentRecord;

public class DeviceAssignmentDAOImpl implements DeviceAssignmentDAO {
    private final IConnectionManager connectionManager;
    
    private static final String COL_ID = "id";
    private static final String COL_EMPLOYEE_ID = "employee_id";
    private static final String COL_DEVICE_ID = "device_id";
    private static final String COL_DEVICE_NAME = "device_name";
    private static final String COL_EMPLOYEE_NAME = "employee_name";
    private static final String COL_DEPARTMENT = "department";
    private static final String COL_ASSIGNED_AT = "assigned_at";
    private static final String COL_RETURNED_AT = "returned_at";
    private static final String COL_EXPIRATION_DATE = "expiration_date";
    private static final String COL_STATUS = "status";

    public DeviceAssignmentDAOImpl(IConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public List<DeviceAssignmentRecord> findAll() throws SQLException {
        return connectionManager.doTask(conn -> {
            List<DeviceAssignmentRecord> result = new ArrayList<>();
            PreparedStatement stmt = conn.prepareStatement("""
                SELECT a.*, d.name as device_name, e.name as employee_name, e.department 
                FROM device_assignments a
                JOIN devices d ON a.device_id = d.id
                JOIN employees e ON a.employee_id = e.id
            """);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(mapResultSetToRecord(rs));
            }
            return result;
        });
    }

    @Override
    public Optional<DeviceAssignmentRecord> findById(int id) throws SQLException {
        return connectionManager.doTask(conn -> {
            PreparedStatement stmt = conn.prepareStatement("""
                SELECT a.*, d.name as device_name, e.name as employee_name, e.department 
                FROM device_assignments a
                JOIN devices d ON a.device_id = d.id
                JOIN employees e ON a.employee_id = e.id
                WHERE a.id = ?
            """);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToRecord(rs));
            }
            return Optional.empty();
        });
    }

    @Override
    public List<DeviceAssignmentRecord> findByEmployeeId(int employeeId) throws SQLException {
        return connectionManager.doTask(conn -> {
            List<DeviceAssignmentRecord> result = new ArrayList<>();
            PreparedStatement stmt = conn.prepareStatement("""
                SELECT a.*, d.name as device_name, e.name as employee_name, e.department 
                FROM device_assignments a
                JOIN devices d ON a.device_id = d.id
                JOIN employees e ON a.employee_id = e.id
                WHERE a.employee_id = ?
            """);
            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(mapResultSetToRecord(rs));
            }
            return result;
        });
    }

    @Override
    public List<DeviceAssignmentRecord> findByDeviceId(int deviceId) throws SQLException {
        return connectionManager.doTask(conn -> {
            List<DeviceAssignmentRecord> result = new ArrayList<>();
            PreparedStatement stmt = conn.prepareStatement("""
                SELECT a.*, d.name as device_name, e.name as employee_name, e.department 
                FROM device_assignments a
                JOIN devices d ON a.device_id = d.id
                JOIN employees e ON a.employee_id = e.id
                WHERE a.device_id = ?
            """);
            stmt.setInt(1, deviceId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(mapResultSetToRecord(rs));
            }
            return result;
        });
    }

    @Override
    public List<DeviceAssignmentRecord> findActiveAssignments() throws SQLException {
        return connectionManager.doTask(conn -> {
            List<DeviceAssignmentRecord> result = new ArrayList<>();
            PreparedStatement stmt = conn.prepareStatement("""
                SELECT a.*, d.name as device_name, e.name as employee_name, e.department 
                FROM device_assignments a
                JOIN devices d ON a.device_id = d.id
                JOIN employees e ON a.employee_id = e.id
                WHERE a.returned_at IS NULL
            """);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(mapResultSetToRecord(rs));
            }
            return result;
        });
    }

    @Override
    public void insert(DeviceAssignmentRecord assignment) throws SQLException {
        connectionManager.doTask(conn -> {
            PreparedStatement stmt = conn.prepareStatement("""
                INSERT INTO device_assignments 
                (employee_id, device_id, assigned_at, expiration_date, status) 
                VALUES (?, ?, ?, ?, ?)
            """);
            stmt.setInt(1, assignment.employeeId());
            stmt.setInt(2, assignment.deviceId());
            stmt.setTimestamp(3, Timestamp.valueOf(assignment.assignedAt()));
            stmt.setTimestamp(4, Timestamp.valueOf(assignment.expirationDate()));
            stmt.setString(5, "Active");
            stmt.executeUpdate();
            return null;
        });
    }

    @Override
    public void update(DeviceAssignmentRecord assignment) throws SQLException {
        connectionManager.doTask(conn -> {
            PreparedStatement stmt = conn.prepareStatement("""
                UPDATE device_assignments 
                SET employee_id = ?, device_id = ?, status = ?
                WHERE id = ?
            """);
            stmt.setInt(1, assignment.employeeId());
            stmt.setInt(2, assignment.deviceId());
            stmt.setString(3, assignment.status());
            stmt.setInt(4, assignment.id());
            stmt.executeUpdate();
            return null;
        });
    }

    @Override
    public void deleteById(int id) throws SQLException {
        connectionManager.doTask(conn -> {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM device_assignments WHERE id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return null;
        });
    }

    @Override
    public void returnDevice(int assignmentId) throws SQLException {
        connectionManager.doTask(conn -> {
            PreparedStatement stmt = conn.prepareStatement("""
                UPDATE device_assignments 
                SET returned_at = ?, status = 'Returned'
                WHERE id = ?
            """);
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(2, assignmentId);
            stmt.executeUpdate();
            return null;
        });
    }

    private DeviceAssignmentRecord mapResultSetToRecord(ResultSet rs) throws SQLException {
        return new DeviceAssignmentRecord(
            rs.getInt(COL_ID),
            rs.getInt(COL_EMPLOYEE_ID),
            rs.getInt(COL_DEVICE_ID),
            rs.getString(COL_DEVICE_NAME),
            rs.getString(COL_EMPLOYEE_NAME),
            rs.getString(COL_DEPARTMENT),
            rs.getTimestamp(COL_ASSIGNED_AT).toLocalDateTime(),
            Optional.ofNullable(rs.getTimestamp(COL_RETURNED_AT))
                .map(Timestamp::toLocalDateTime)
                .orElse(null),
            rs.getTimestamp(COL_EXPIRATION_DATE).toLocalDateTime(),
            rs.getString(COL_STATUS)
        );
    }
} 