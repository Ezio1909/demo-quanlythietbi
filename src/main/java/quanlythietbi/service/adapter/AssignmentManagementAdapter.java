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
        } catch (SQLException e) {
            logger.error("Failed to assign device {} to employee {}", deviceId, employeeId, e);
            throw new IllegalArgumentException("Failed to assign device: " + e.getMessage());
        }
    }

    public void returnDevice(Integer assignmentId) {
        try {
            assignmentDAO.returnDevice(assignmentId);
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