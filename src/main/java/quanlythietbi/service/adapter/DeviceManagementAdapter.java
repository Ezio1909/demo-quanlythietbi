package quanlythietbi.service.adapter;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quanlythietbi.entity.DeviceInfoRecord;
import quanlythietbi.service.deviceinfo.DeviceInfoDAO;

public class DeviceManagementAdapter {
    private static final Logger logger = LoggerFactory.getLogger(DeviceManagementAdapter.class);
    private final DeviceInfoDAO deviceDAO;

    public DeviceManagementAdapter(DeviceInfoDAO deviceDAO) {
        this.deviceDAO = deviceDAO;
    }

    public List<DeviceInfoRecord> getAllDevices() {
        try {
            return deviceDAO.findAll();
        } catch (SQLException e) {
            logger.error("Failed to get all devices", e);
            return Collections.emptyList();
        }
    }

    public Optional<DeviceInfoRecord> getDevice(Integer id) {
        try {
            return deviceDAO.findById(id);
        } catch (SQLException e) {
            logger.error("Failed to get device with id: {}", id, e);
            return Optional.empty();
        }
    }

    public void addDevice(DeviceInfoRecord device) {
        try {
            deviceDAO.insert(device);
        } catch (SQLException e) {
            logger.error("Failed to add device: {}", device, e);
        }
    }

    public void updateDevice(DeviceInfoRecord device) {
        try {
            deviceDAO.update(device);
        } catch (SQLException e) {
            logger.error("Failed to update device: {}", device, e);
        }
    }

    public void deleteDevice(Integer id) {
        try {
            deviceDAO.deleteById(id);
        } catch (SQLException e) {
            logger.error("Failed to delete device with id: {}", id, e);
        }
    }
} 