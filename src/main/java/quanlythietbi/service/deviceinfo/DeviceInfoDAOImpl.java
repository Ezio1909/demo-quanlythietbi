package quanlythietbi.service.deviceinfo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import quanlythietbi.connector.IConnectionManager;
import quanlythietbi.entity.DeviceInfoRecord;

public class DeviceInfoDAOImpl implements DeviceInfoDAO {
    private final IConnectionManager connectionManager;
    
    private static final String COL_DEVICE_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_TYPE = "type";
    private static final String COL_SERIAL = "serial_number";
    private static final String COL_STATUS = "status";

    public DeviceInfoDAOImpl(IConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public List<DeviceInfoRecord> findAll() throws SQLException {
        return connectionManager.doTask(conn -> {
            List<DeviceInfoRecord> result = new ArrayList<>();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM devices");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(new DeviceInfoRecord(
                    rs.getInt(COL_DEVICE_ID),
                    rs.getString(COL_NAME),
                    rs.getString(COL_TYPE),
                    rs.getString(COL_SERIAL),
                    rs.getString(COL_STATUS)
                ));
            }
            return result;
        });
    }

    @Override
    public Optional<DeviceInfoRecord> findById(int id) throws SQLException {
        return connectionManager.doTask(conn -> {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM devices WHERE id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new DeviceInfoRecord(
                    rs.getInt(COL_DEVICE_ID),
                    rs.getString(COL_NAME),
                    rs.getString(COL_TYPE),
                    rs.getString(COL_SERIAL),
                    rs.getString(COL_STATUS)
                ));
            }
            return Optional.empty();
        });
    }

    @Override
    public void insert(DeviceInfoRecord device) throws SQLException {
        connectionManager.doTask(conn -> {
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO devices (name, type, serial_number, status) VALUES (?, ?, ?, ?)"
            );
            stmt.setString(1, device.name());
            stmt.setString(2, device.type());
            stmt.setString(3, device.serialNumber());
            stmt.setString(4, device.status());
            stmt.executeUpdate();
            return null;
        });
    }

    @Override
    public void deleteById(int id) throws SQLException {
        connectionManager.doTask(conn -> {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM devices WHERE id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return null;
        });
    }

    @Override
    public void update(DeviceInfoRecord device) throws SQLException {
        connectionManager.doTask(conn -> {
            PreparedStatement stmt = conn.prepareStatement(
                "UPDATE devices SET name = ?, type = ?, serial_number = ?, status = ? WHERE id = ?"
            );
            stmt.setString(1, device.name());
            stmt.setString(2, device.type());
            stmt.setString(3, device.serialNumber());
            stmt.setString(4, device.status());
            stmt.setInt(5, device.id());
            stmt.executeUpdate();
            return null;
        });
    }
} 