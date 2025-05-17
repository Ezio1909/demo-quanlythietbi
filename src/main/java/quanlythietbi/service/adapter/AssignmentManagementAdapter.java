package quanlythietbi.service.adapter;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quanlythietbi.entity.DeviceAssignmentRecord;
import quanlythietbi.service.assignment.DeviceAssignmentDAO;

public class AssignmentManagementAdapter {
    private static final Logger logger = LoggerFactory.getLogger(AssignmentManagementAdapter.class);
    private final DeviceAssignmentDAO assignmentDAO;

    public AssignmentManagementAdapter(DeviceAssignmentDAO assignmentDAO) {
        this.assignmentDAO = assignmentDAO;
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

    public void assignDevice(Integer employeeId, Integer deviceId) {
        try {
            DeviceAssignmentRecord assignment = new DeviceAssignmentRecord(
                null,
                employeeId,
                deviceId,
                null,
                null,
                null,
                null,
                null,
                "Active"
            );
            assignmentDAO.insert(assignment);
        } catch (SQLException e) {
            logger.error("Failed to assign device {} to employee {}", deviceId, employeeId, e);
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