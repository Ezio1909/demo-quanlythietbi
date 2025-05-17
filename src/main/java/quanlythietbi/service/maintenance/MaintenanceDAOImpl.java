package quanlythietbi.service.maintenance;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import quanlythietbi.connector.IConnectionManager;
import quanlythietbi.entity.MaintenanceRecord;

public class MaintenanceDAOImpl implements MaintenanceDAO {
    private final IConnectionManager connectionManager;
    
    private static final String COL_ID = "id";
    private static final String COL_DEVICE_ID = "device_id";
    private static final String COL_DEVICE_NAME = "device_name";
    private static final String COL_TYPE = "maintenance_type";
    private static final String COL_DESC = "description";
    private static final String COL_REPORTED = "reported_at";
    private static final String COL_SCHEDULED = "scheduled_for";
    private static final String COL_COMPLETED = "completed_at";
    private static final String COL_COST = "cost";
    private static final String COL_STATUS = "status";
    private static final String COL_NOTES = "notes";

    public MaintenanceDAOImpl(IConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public List<MaintenanceRecord> findAll() throws SQLException {
        return connectionManager.doTask(conn -> {
            List<MaintenanceRecord> result = new ArrayList<>();
            PreparedStatement stmt = conn.prepareStatement("""
                SELECT m.*, d.name as device_name 
                FROM device_maintenance m
                JOIN devices d ON m.device_id = d.id
            """);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(mapResultSetToRecord(rs));
            }
            return result;
        });
    }

    @Override
    public Optional<MaintenanceRecord> findById(int id) throws SQLException {
        return connectionManager.doTask(conn -> {
            PreparedStatement stmt = conn.prepareStatement("""
                SELECT m.*, d.name as device_name 
                FROM device_maintenance m
                JOIN devices d ON m.device_id = d.id
                WHERE m.id = ?
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
    public List<MaintenanceRecord> findByDeviceId(int deviceId) throws SQLException {
        return connectionManager.doTask(conn -> {
            List<MaintenanceRecord> result = new ArrayList<>();
            PreparedStatement stmt = conn.prepareStatement("""
                SELECT m.*, d.name as device_name 
                FROM device_maintenance m
                JOIN devices d ON m.device_id = d.id
                WHERE m.device_id = ?
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
    public List<MaintenanceRecord> findByStatus(String status) throws SQLException {
        return connectionManager.doTask(conn -> {
            List<MaintenanceRecord> result = new ArrayList<>();
            PreparedStatement stmt = conn.prepareStatement("""
                SELECT m.*, d.name as device_name 
                FROM device_maintenance m
                JOIN devices d ON m.device_id = d.id
                WHERE m.status = ?
            """);
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(mapResultSetToRecord(rs));
            }
            return result;
        });
    }

    @Override
    public void insert(MaintenanceRecord maintenance) throws SQLException {
        connectionManager.doTask(conn -> {
            PreparedStatement stmt = conn.prepareStatement("""
                INSERT INTO device_maintenance 
                (device_id, maintenance_type, description, scheduled_for, cost, status, notes)
                VALUES (?, ?, ?, ?, ?, ?, ?)
            """);
            stmt.setInt(1, maintenance.deviceId());
            stmt.setString(2, maintenance.maintenanceType());
            stmt.setString(3, maintenance.description());
            stmt.setTimestamp(4, maintenance.scheduledFor() != null ? 
                Timestamp.valueOf(maintenance.scheduledFor()) : null);
            stmt.setBigDecimal(5, maintenance.cost());
            stmt.setString(6, maintenance.status());
            stmt.setString(7, maintenance.notes());
            stmt.executeUpdate();
            return null;
        });
    }

    @Override
    public void update(MaintenanceRecord maintenance) throws SQLException {
        connectionManager.doTask(conn -> {
            PreparedStatement stmt = conn.prepareStatement("""
                UPDATE device_maintenance 
                SET maintenance_type = ?, description = ?, scheduled_for = ?, 
                    cost = ?, status = ?, notes = ?
                WHERE id = ?
            """);
            stmt.setString(1, maintenance.maintenanceType());
            stmt.setString(2, maintenance.description());
            stmt.setTimestamp(3, maintenance.scheduledFor() != null ? 
                Timestamp.valueOf(maintenance.scheduledFor()) : null);
            stmt.setBigDecimal(4, maintenance.cost());
            stmt.setString(5, maintenance.status());
            stmt.setString(6, maintenance.notes());
            stmt.setInt(7, maintenance.id());
            stmt.executeUpdate();
            return null;
        });
    }

    @Override
    public void deleteById(int id) throws SQLException {
        connectionManager.doTask(conn -> {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM device_maintenance WHERE id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return null;
        });
    }

    @Override
    public void completeMaintenance(int id, String notes) throws SQLException {
        connectionManager.doTask(conn -> {
            PreparedStatement stmt = conn.prepareStatement("""
                UPDATE device_maintenance 
                SET completed_at = ?, status = 'Completed', notes = ?
                WHERE id = ?
            """);
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(2, notes);
            stmt.setInt(3, id);
            stmt.executeUpdate();
            return null;
        });
    }

    private MaintenanceRecord mapResultSetToRecord(ResultSet rs) throws SQLException {
        return new MaintenanceRecord(
            rs.getInt(COL_ID),
            rs.getInt(COL_DEVICE_ID),
            rs.getString(COL_DEVICE_NAME),
            rs.getString(COL_TYPE),
            rs.getString(COL_DESC),
            rs.getTimestamp(COL_REPORTED).toLocalDateTime(),
            Optional.ofNullable(rs.getTimestamp(COL_SCHEDULED))
                .map(Timestamp::toLocalDateTime)
                .orElse(null),
            Optional.ofNullable(rs.getTimestamp(COL_COMPLETED))
                .map(Timestamp::toLocalDateTime)
                .orElse(null),
            rs.getBigDecimal(COL_COST),
            rs.getString(COL_STATUS),
            rs.getString(COL_NOTES)
        );
    }
} 