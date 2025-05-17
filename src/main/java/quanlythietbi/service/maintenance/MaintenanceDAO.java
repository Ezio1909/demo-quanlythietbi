package quanlythietbi.service.maintenance;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import quanlythietbi.entity.MaintenanceRecord;

public interface MaintenanceDAO {
    List<MaintenanceRecord> findAll() throws SQLException;
    
    Optional<MaintenanceRecord> findById(int id) throws SQLException;
    
    List<MaintenanceRecord> findByDeviceId(int deviceId) throws SQLException;
    
    List<MaintenanceRecord> findByStatus(String status) throws SQLException;
    
    void insert(MaintenanceRecord maintenance) throws SQLException;
    
    void update(MaintenanceRecord maintenance) throws SQLException;
    
    void deleteById(int id) throws SQLException;
    
    void completeMaintenance(int id, String notes) throws SQLException;
} 