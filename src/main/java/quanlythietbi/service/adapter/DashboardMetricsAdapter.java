package quanlythietbi.service.adapter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import quanlythietbi.entity.DeviceInfoRecord;
import quanlythietbi.entity.MaintenanceRecord;
import quanlythietbi.service.deviceinfo.DeviceInfoDAO;
import quanlythietbi.service.maintenance.MaintenanceDAO;

public class DashboardMetricsAdapter {
    private final DeviceInfoDAO deviceDAO;
    private final MaintenanceDAO maintenanceDAO;

    public DashboardMetricsAdapter(DeviceInfoDAO deviceDAO, MaintenanceDAO maintenanceDAO) {
        this.deviceDAO = deviceDAO;
        this.maintenanceDAO = maintenanceDAO;
    }

    public Map<String, Long> getDeviceStatusCounts() {
        try {
            List<DeviceInfoRecord> devices = deviceDAO.findAll();
            return devices.stream()
                .collect(Collectors.groupingBy(
                    DeviceInfoRecord::status,
                    Collectors.counting()
                ));
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    public Map<String, Long> getDevicesByDepartment() {
        try {
            List<DeviceInfoRecord> devices = deviceDAO.findAll();
            return devices.stream()
                .filter(d -> d.department() != null && !d.department().isEmpty())
                .collect(Collectors.groupingBy(
                    DeviceInfoRecord::department,
                    Collectors.counting()
                ));
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    public BigDecimal getTotalMaintenanceCost() {
        try {
            List<MaintenanceRecord> records = maintenanceDAO.findAll();
            return records.stream()
                .filter(r -> r.cost() != null)
                .map(MaintenanceRecord::cost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    public Map<String, Long> getMaintenanceByType() {
        try {
            List<MaintenanceRecord> records = maintenanceDAO.findAll();
            return records.stream()
                .collect(Collectors.groupingBy(
                    MaintenanceRecord::maintenanceType,
                    Collectors.counting()
                ));
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    public Map<String, Long> getMaintenanceByStatus() {
        try {
            List<MaintenanceRecord> records = maintenanceDAO.findAll();
            return records.stream()
                .collect(Collectors.groupingBy(
                    MaintenanceRecord::status,
                    Collectors.counting()
                ));
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    public long getTotalDevices() {
        try {
            return deviceDAO.findAll().size();
        } catch (Exception e) {
            return 0;
        }
    }

    public long getAvailableDevices() {
        try {
            return deviceDAO.findAll().stream()
                .filter(d -> "Available".equals(d.status()))
                .count();
        } catch (Exception e) {
            return 0;
        }
    }

    public long getDevicesInMaintenance() {
        try {
            return deviceDAO.findAll().stream()
                .filter(d -> "Maintenance".equals(d.status()))
                .count();
        } catch (Exception e) {
            return 0;
        }
    }

    public long getAssignedDevices() {
        try {
            return deviceDAO.findAll().stream()
                .filter(d -> "In Use".equals(d.status()))
                .count();
        } catch (Exception e) {
            return 0;
        }
    }
} 