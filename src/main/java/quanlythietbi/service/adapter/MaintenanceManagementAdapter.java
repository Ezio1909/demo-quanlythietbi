package quanlythietbi.service.adapter;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quanlythietbi.entity.MaintenanceRecord;
import quanlythietbi.service.maintenance.MaintenanceDAO;

public class MaintenanceManagementAdapter {
    private static final Logger logger = LoggerFactory.getLogger(MaintenanceManagementAdapter.class);
    private final MaintenanceDAO maintenanceDAO;

    public MaintenanceManagementAdapter(MaintenanceDAO maintenanceDAO) {
        this.maintenanceDAO = maintenanceDAO;
    }

    public List<MaintenanceRecord> getAllMaintenanceRecords() {
        try {
            return maintenanceDAO.findAll();
        } catch (SQLException e) {
            logger.error("Failed to get all maintenance records", e);
            return Collections.emptyList();
        }
    }

    public Optional<MaintenanceRecord> getMaintenanceRecord(Integer id) {
        try {
            return maintenanceDAO.findById(id);
        } catch (SQLException e) {
            logger.error("Failed to get maintenance record with id: {}", id, e);
            return Optional.empty();
        }
    }

    public List<MaintenanceRecord> getMaintenanceRecordsByDevice(Integer deviceId) {
        try {
            return maintenanceDAO.findByDeviceId(deviceId);
        } catch (SQLException e) {
            logger.error("Failed to get maintenance records for device id: {}", deviceId, e);
            return Collections.emptyList();
        }
    }

    public List<MaintenanceRecord> getMaintenanceRecordsByStatus(String status) {
        try {
            return maintenanceDAO.findByStatus(status);
        } catch (SQLException e) {
            logger.error("Failed to get maintenance records with status: {}", status, e);
            return Collections.emptyList();
        }
    }

    public void addMaintenanceRecord(MaintenanceRecord maintenance) {
        try {
            maintenanceDAO.insert(maintenance);
        } catch (SQLException e) {
            logger.error("Failed to add maintenance record: {}", maintenance, e);
            throw new RuntimeException("Failed to add maintenance record", e);
        }
    }

    public void updateMaintenanceRecord(MaintenanceRecord maintenance) {
        try {
            maintenanceDAO.update(maintenance);
        } catch (SQLException e) {
            logger.error("Failed to update maintenance record: {}", maintenance, e);
            throw new RuntimeException("Failed to update maintenance record", e);
        }
    }

    public void deleteMaintenanceRecord(Integer id) {
        try {
            maintenanceDAO.deleteById(id);
        } catch (SQLException e) {
            logger.error("Failed to delete maintenance record with id: {}", id, e);
            throw new RuntimeException("Failed to delete maintenance record", e);
        }
    }

    public void completeMaintenanceRecord(Integer id, String notes) {
        try {
            maintenanceDAO.completeMaintenance(id, notes);
        } catch (SQLException e) {
            logger.error("Failed to complete maintenance record with id: {}", id, e);
            throw new RuntimeException("Failed to complete maintenance record", e);
        }
    }
} 