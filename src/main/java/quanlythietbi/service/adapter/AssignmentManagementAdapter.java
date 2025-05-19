package quanlythietbi.service.adapter;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quanlythietbi.entity.DeviceAssignmentRecord;
import quanlythietbi.entity.EmployeeInfoRecord;
import quanlythietbi.service.assignment.DeviceAssignmentDAO;
import quanlythietbi.service.deviceinfo.DeviceInfoDAO;
import quanlythietbi.service.employeeinfo.EmployeeInfoDAO;

public class AssignmentManagementAdapter {
    private static final Logger logger = LoggerFactory.getLogger(AssignmentManagementAdapter.class);
    private final DeviceAssignmentDAO assignmentDAO;
    private final EmployeeInfoDAO employeeDAO;
    private final DeviceInfoDAO deviceDAO;

    public AssignmentManagementAdapter(
        DeviceAssignmentDAO assignmentDAO,
        EmployeeInfoDAO employeeDAO,
        DeviceInfoDAO deviceDAO
    ) {
        this.assignmentDAO = assignmentDAO;
        this.employeeDAO = employeeDAO;
        this.deviceDAO = deviceDAO;
    }

    public List<EmployeeInfoRecord> getAllEmployees() {
        try {
            return employeeDAO.findAll();
        } catch (SQLException e) {
            logger.error("Failed to get all employees", e);
            return Collections.emptyList();
        }
    }

    public List<DeviceAssignmentRecord> getAllAssignments() {
        try {
            return assignmentDAO.findAll();
        } catch (SQLException e) {
            logger.error("Failed to get all assignments", e);
            return Collections.emptyList();
        }
    }

    public List<DeviceAssignmentRecord> getActiveAssignments() {
        try {
            return assignmentDAO.findActiveAssignments();
        } catch (SQLException e) {
            logger.error("Failed to get active assignments", e);
            return Collections.emptyList();
        }
    }

    public Optional<DeviceAssignmentRecord> getAssignment(Integer id) {
        try {
            return assignmentDAO.findById(id);
        } catch (SQLException e) {
            logger.error("Failed to get assignment with id: {}", id, e);
            return Optional.empty();
        }
    }

    public List<DeviceAssignmentRecord> getAssignmentsByEmployee(Integer employeeId) {
        try {
            return assignmentDAO.findByEmployeeId(employeeId);
        } catch (SQLException e) {
            logger.error("Failed to get assignments for employee id: {}", employeeId, e);
            return Collections.emptyList();
        }
    }

    public List<DeviceAssignmentRecord> getAssignmentsByDevice(Integer deviceId) {
        try {
            return assignmentDAO.findByDeviceId(deviceId);
        } catch (SQLException e) {
            logger.error("Failed to get assignments for device id: {}", deviceId, e);
            return Collections.emptyList();
        }
    }

    public void assignDevice(Integer employeeId, Integer deviceId) throws IllegalArgumentException {
        try {
            // Validate employee exists
            var employee = employeeDAO.findById(employeeId);
            if (employee == null) {
                throw new IllegalArgumentException("Employee not found with ID: " + employeeId);
            }

            // Validate device exists and is available
            var device = deviceDAO.findById(deviceId);
            if (device.isEmpty()) {
                throw new IllegalArgumentException("Device not found with ID: " + deviceId);
            }
            if (!"Available".equals(device.get().status())) {
                throw new IllegalArgumentException(String.format(
                    "Device '%s' (ID: %d) cannot be assigned because it is currently %s",
                    device.get().name(),
                    deviceId,
                    device.get().status().toLowerCase()
                ));
            }

            // Set assignment dates
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expirationDate = now.plusYears(1);

            // Create the assignment
            DeviceAssignmentRecord assignment = new DeviceAssignmentRecord(
                null,
                employeeId,
                deviceId,
                device.get().name(),
                employee.name(),
                employee.department(),
                now,
                null,
                expirationDate,
                "Active"
            );
            assignmentDAO.insert(assignment);

            // Update device status to 'In Use'
            var updatedDevice = new quanlythietbi.entity.DeviceInfoRecord(
                device.get().id(),
                device.get().name(),
                device.get().type(),
                device.get().serialNumber(),
                "In Use",
                device.get().purchaseDate(),
                device.get().purchasePrice(),
                device.get().supplier(),
                device.get().warrantyExpiry(),
                device.get().model(),
                device.get().manufacturer(),
                device.get().specifications(),
                device.get().firmwareVersion(),
                device.get().assetTag(),
                device.get().location(),
                device.get().department(),
                device.get().category(),
                device.get().lastInspectionDate(),
                device.get().nextInspectionDate(),
                device.get().endOfLifeDate(),
                device.get().deviceCondition(),
                device.get().notes(),
                device.get().createdAt(),
                LocalDateTime.now(),
                device.get().createdBy(),
                "system"
            );
            deviceDAO.update(updatedDevice);
        } catch (SQLException e) {
            logger.error("Failed to assign device {} to employee {}", deviceId, employeeId, e);
            throw new IllegalArgumentException("Failed to assign device: " + e.getMessage());
        }
    }

    public void returnDevice(Integer assignmentId) {
        try {
            // Get assignment and device
            var assignmentOpt = assignmentDAO.findById(assignmentId);
            if (assignmentOpt.isEmpty()) {
                throw new IllegalArgumentException("Assignment not found with ID: " + assignmentId);
            }
            var assignment = assignmentOpt.get();
            assignmentDAO.returnDevice(assignmentId);

            // Update device status to 'Available'
            var deviceOpt = deviceDAO.findById(assignment.deviceId());
            if (deviceOpt.isPresent()) {
                var device = deviceOpt.get();
                var updatedDevice = new quanlythietbi.entity.DeviceInfoRecord(
                    device.id(),
                    device.name(),
                    device.type(),
                    device.serialNumber(),
                    "Available",
                    device.purchaseDate(),
                    device.purchasePrice(),
                    device.supplier(),
                    device.warrantyExpiry(),
                    device.model(),
                    device.manufacturer(),
                    device.specifications(),
                    device.firmwareVersion(),
                    device.assetTag(),
                    device.location(),
                    device.department(),
                    device.category(),
                    device.lastInspectionDate(),
                    device.nextInspectionDate(),
                    device.endOfLifeDate(),
                    device.deviceCondition(),
                    device.notes(),
                    device.createdAt(),
                    LocalDateTime.now(),
                    device.createdBy(),
                    "system"
                );
                deviceDAO.update(updatedDevice);
            }
        } catch (SQLException e) {
            logger.error("Failed to return device for assignment id: {}", assignmentId, e);
        }
    }

    public void deleteAssignment(Integer id) {
        try {
            assignmentDAO.deleteById(id);
        } catch (SQLException e) {
            logger.error("Failed to delete assignment with id: {}", id, e);
        }
    }
} 