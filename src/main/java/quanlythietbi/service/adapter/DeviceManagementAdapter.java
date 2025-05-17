package quanlythietbi.service.adapter;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import quanlythietbi.entity.DeviceInfoRecord;
import quanlythietbi.service.deviceinfo.DeviceInfoDAO;

public class DeviceManagementAdapter {
    private final DeviceInfoDAO deviceDAO;

    public DeviceManagementAdapter(DeviceInfoDAO deviceDAO) {
        this.deviceDAO = deviceDAO;
    }

    public List<DeviceInfoRecord> getAllDevices() {
        try {
            return deviceDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public Optional<DeviceInfoRecord> getDevice(Integer id) {
        try {
            return deviceDAO.findById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void addDevice(String name, String type, String serialNumber) {
        try {
            DeviceInfoRecord newDevice = new DeviceInfoRecord(
                null, // ID will be assigned by database
                name,
                type,
                serialNumber,
                "Available" // Default status for new devices
            );
            deviceDAO.insert(newDevice);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDevice(DeviceInfoRecord device) {
        try {
            deviceDAO.update(device);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDevice(Integer id) {
        try {
            deviceDAO.deleteById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} 