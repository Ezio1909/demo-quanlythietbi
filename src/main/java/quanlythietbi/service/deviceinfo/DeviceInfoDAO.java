package quanlythietbi.service.deviceinfo;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import quanlythietbi.entity.DeviceInfoRecord;

public interface DeviceInfoDAO {
    List<DeviceInfoRecord> findAll() throws SQLException;
    
    Optional<DeviceInfoRecord> findById(int id) throws SQLException;
    
    void insert(DeviceInfoRecord device) throws SQLException;
    
    void deleteById(int id) throws SQLException;
    
    void update(DeviceInfoRecord device) throws SQLException;
} 