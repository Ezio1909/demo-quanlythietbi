package quanlythietbi.service.adapter;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import quanlythietbi.entity.DeviceAssignmentRecord;
import quanlythietbi.service.assignment.DeviceAssignmentDAO;

public class AssignmentManagementAdapter {
    private final DeviceAssignmentDAO assignmentDAO;

    public AssignmentManagementAdapter(DeviceAssignmentDAO assignmentDAO) {
        this.assignmentDAO = assignmentDAO;
    }

    public List<DeviceAssignmentRecord> getAllAssignments() {
        try {
            return assignmentDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<DeviceAssignmentRecord> getActiveAssignments() {
        try {
            return assignmentDAO.findActiveAssignments();
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public Optional<DeviceAssignmentRecord> getAssignment(Integer id) {
        try {
            return assignmentDAO.findById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public List<DeviceAssignmentRecord> getAssignmentsByEmployee(Integer employeeId) {
        try {
            return assignmentDAO.findByEmployeeId(employeeId);
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<DeviceAssignmentRecord> getAssignmentsByDevice(Integer deviceId) {
        try {
            return assignmentDAO.findByDeviceId(deviceId);
        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    public void returnDevice(Integer assignmentId) {
        try {
            assignmentDAO.returnDevice(assignmentId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAssignment(Integer id) {
        try {
            assignmentDAO.deleteById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} 