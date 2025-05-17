package quanlythietbi.service.assignment;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import quanlythietbi.entity.DeviceAssignmentRecord;

public interface DeviceAssignmentDAO {
    List<DeviceAssignmentRecord> findAll() throws SQLException;
    
    Optional<DeviceAssignmentRecord> findById(int id) throws SQLException;
    
    List<DeviceAssignmentRecord> findByEmployeeId(int employeeId) throws SQLException;
    
    List<DeviceAssignmentRecord> findByDeviceId(int deviceId) throws SQLException;
    
    List<DeviceAssignmentRecord> findActiveAssignments() throws SQLException;
    
    void insert(DeviceAssignmentRecord assignment) throws SQLException;
    
    void update(DeviceAssignmentRecord assignment) throws SQLException;
    
    void deleteById(int id) throws SQLException;
    
    void returnDevice(int assignmentId) throws SQLException;
} 