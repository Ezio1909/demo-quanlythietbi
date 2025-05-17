package quanlythietbi.service.deviceinfo;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

import quanlythietbi.connector.IConnectionManager;
import quanlythietbi.entity.DeviceInfoRecord;

public class DeviceInfoDAOImpl implements DeviceInfoDAO {
    private final IConnectionManager connectionManager;
    
    // Column constants
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_TYPE = "type";
    private static final String COL_SERIAL = "serial_number";
    private static final String COL_STATUS = "status";
    private static final String COL_PURCHASE_DATE = "purchase_date";
    private static final String COL_PURCHASE_PRICE = "purchase_price";
    private static final String COL_SUPPLIER = "supplier";
    private static final String COL_WARRANTY_EXPIRY = "warranty_expiry";
    private static final String COL_MODEL = "model";
    private static final String COL_MANUFACTURER = "manufacturer";
    private static final String COL_SPECS = "specifications";
    private static final String COL_FIRMWARE = "firmware_version";
    private static final String COL_ASSET_TAG = "asset_tag";
    private static final String COL_LOCATION = "location";
    private static final String COL_DEPARTMENT = "department";
    private static final String COL_CATEGORY = "category";
    private static final String COL_LAST_INSPECTION = "last_inspection_date";
    private static final String COL_NEXT_INSPECTION = "next_inspection_date";
    private static final String COL_END_OF_LIFE = "end_of_life_date";
    private static final String COL_CONDITION = "condition";
    private static final String COL_NOTES = "notes";
    private static final String COL_CREATED_AT = "created_at";
    private static final String COL_UPDATED_AT = "updated_at";
    private static final String COL_CREATED_BY = "created_by";
    private static final String COL_MODIFIED_BY = "last_modified_by";

    public DeviceInfoDAOImpl(IConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    private DeviceInfoRecord mapResultSetToRecord(ResultSet rs) throws SQLException {
        return new DeviceInfoRecord(
            rs.getInt(COL_ID),
            rs.getString(COL_NAME),
            rs.getString(COL_TYPE),
            rs.getString(COL_SERIAL),
            rs.getString(COL_STATUS),
            Optional.ofNullable(rs.getDate(COL_PURCHASE_DATE))
                .map(Date::toLocalDate)
                .orElse(null),
            rs.getBigDecimal(COL_PURCHASE_PRICE),
            rs.getString(COL_SUPPLIER),
            Optional.ofNullable(rs.getDate(COL_WARRANTY_EXPIRY))
                .map(Date::toLocalDate)
                .orElse(null),
            rs.getString(COL_MODEL),
            rs.getString(COL_MANUFACTURER),
            rs.getString(COL_SPECS),
            rs.getString(COL_FIRMWARE),
            rs.getString(COL_ASSET_TAG),
            rs.getString(COL_LOCATION),
            rs.getString(COL_DEPARTMENT),
            rs.getString(COL_CATEGORY),
            Optional.ofNullable(rs.getDate(COL_LAST_INSPECTION))
                .map(Date::toLocalDate)
                .orElse(null),
            Optional.ofNullable(rs.getDate(COL_NEXT_INSPECTION))
                .map(Date::toLocalDate)
                .orElse(null),
            Optional.ofNullable(rs.getDate(COL_END_OF_LIFE))
                .map(Date::toLocalDate)
                .orElse(null),
            rs.getString(COL_CONDITION),
            rs.getString(COL_NOTES),
            rs.getTimestamp(COL_CREATED_AT).toLocalDateTime(),
            rs.getTimestamp(COL_UPDATED_AT).toLocalDateTime(),
            rs.getString(COL_CREATED_BY),
            rs.getString(COL_MODIFIED_BY)
        );
    }

    @Override
    public List<DeviceInfoRecord> findAll() throws SQLException {
        return connectionManager.doTask(conn -> {
            List<DeviceInfoRecord> result = new ArrayList<>();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM devices");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(mapResultSetToRecord(rs));
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
                return Optional.of(mapResultSetToRecord(rs));
            }
            return Optional.empty();
        });
    }

    @Override
    public void insert(DeviceInfoRecord device) throws SQLException {
        connectionManager.doTask(conn -> {
            PreparedStatement stmt = conn.prepareStatement("""
                INSERT INTO devices (
                    name, type, serial_number, status,
                    purchase_date, purchase_price, supplier, warranty_expiry,
                    model, manufacturer, specifications, firmware_version,
                    asset_tag, location, department, category,
                    last_inspection_date, next_inspection_date, end_of_life_date,
                    condition, notes, created_by, last_modified_by
                ) VALUES (
                    ?, ?, ?, ?,
                    ?, ?, ?, ?,
                    ?, ?, ?, ?,
                    ?, ?, ?, ?,
                    ?, ?, ?,
                    ?, ?, ?, ?
                )
            """);
            
            int paramIndex = 1;
            stmt.setString(paramIndex++, device.name());
            stmt.setString(paramIndex++, device.type());
            stmt.setString(paramIndex++, device.serialNumber());
            stmt.setString(paramIndex++, device.status());
            
            setDateOrNull(stmt, paramIndex++, device.purchaseDate());
            setBigDecimalOrNull(stmt, paramIndex++, device.purchasePrice());
            stmt.setString(paramIndex++, device.supplier());
            setDateOrNull(stmt, paramIndex++, device.warrantyExpiry());
            
            stmt.setString(paramIndex++, device.model());
            stmt.setString(paramIndex++, device.manufacturer());
            stmt.setString(paramIndex++, device.specifications());
            stmt.setString(paramIndex++, device.firmwareVersion());
            
            stmt.setString(paramIndex++, device.assetTag());
            stmt.setString(paramIndex++, device.location());
            stmt.setString(paramIndex++, device.department());
            stmt.setString(paramIndex++, device.category());
            
            setDateOrNull(stmt, paramIndex++, device.lastInspectionDate());
            setDateOrNull(stmt, paramIndex++, device.nextInspectionDate());
            setDateOrNull(stmt, paramIndex++, device.endOfLifeDate());
            
            stmt.setString(paramIndex++, device.condition());
            stmt.setString(paramIndex++, device.notes());
            stmt.setString(paramIndex++, device.createdBy());
            stmt.setString(paramIndex++, device.lastModifiedBy());
            
            stmt.executeUpdate();
            return null;
        });
    }

    @Override
    public void update(DeviceInfoRecord device) throws SQLException {
        connectionManager.doTask(conn -> {
            PreparedStatement stmt = conn.prepareStatement("""
                UPDATE devices SET
                    name = ?, type = ?, serial_number = ?, status = ?,
                    purchase_date = ?, purchase_price = ?, supplier = ?, warranty_expiry = ?,
                    model = ?, manufacturer = ?, specifications = ?, firmware_version = ?,
                    asset_tag = ?, location = ?, department = ?, category = ?,
                    last_inspection_date = ?, next_inspection_date = ?, end_of_life_date = ?,
                    condition = ?, notes = ?, last_modified_by = ?, updated_at = CURRENT_TIMESTAMP
                WHERE id = ?
            """);
            
            int paramIndex = 1;
            stmt.setString(paramIndex++, device.name());
            stmt.setString(paramIndex++, device.type());
            stmt.setString(paramIndex++, device.serialNumber());
            stmt.setString(paramIndex++, device.status());
            
            setDateOrNull(stmt, paramIndex++, device.purchaseDate());
            setBigDecimalOrNull(stmt, paramIndex++, device.purchasePrice());
            stmt.setString(paramIndex++, device.supplier());
            setDateOrNull(stmt, paramIndex++, device.warrantyExpiry());
            
            stmt.setString(paramIndex++, device.model());
            stmt.setString(paramIndex++, device.manufacturer());
            stmt.setString(paramIndex++, device.specifications());
            stmt.setString(paramIndex++, device.firmwareVersion());
            
            stmt.setString(paramIndex++, device.assetTag());
            stmt.setString(paramIndex++, device.location());
            stmt.setString(paramIndex++, device.department());
            stmt.setString(paramIndex++, device.category());
            
            setDateOrNull(stmt, paramIndex++, device.lastInspectionDate());
            setDateOrNull(stmt, paramIndex++, device.nextInspectionDate());
            setDateOrNull(stmt, paramIndex++, device.endOfLifeDate());
            
            stmt.setString(paramIndex++, device.condition());
            stmt.setString(paramIndex++, device.notes());
            stmt.setString(paramIndex++, device.lastModifiedBy());
            stmt.setInt(paramIndex++, device.id());
            
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

    private void setDateOrNull(PreparedStatement stmt, int paramIndex, LocalDate date) throws SQLException {
        if (date != null) {
            stmt.setDate(paramIndex, Date.valueOf(date));
        } else {
            stmt.setNull(paramIndex, Types.DATE);
        }
    }

    private void setBigDecimalOrNull(PreparedStatement stmt, int paramIndex, BigDecimal value) throws SQLException {
        if (value != null) {
            stmt.setBigDecimal(paramIndex, value);
        } else {
            stmt.setNull(paramIndex, Types.DECIMAL);
        }
    }
} 