package quanlythietbi.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import quanlythietbi.entity.DeviceAssignmentRecord;
import quanlythietbi.entity.DeviceInfoRecord;
import quanlythietbi.service.adapter.AssignmentManagementAdapter;
import quanlythietbi.service.adapter.DeviceManagementAdapter;

public class AssignmentPanel extends JPanel {
    private final AssignmentManagementAdapter assignmentAdapter;
    private final DeviceManagementAdapter deviceAdapter;
    private JTable assignmentTable;
    private DefaultTableModel tableModel;
    private JButton assignButton, returnButton, deleteButton;
    private JComboBox<String> statusFilter;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public AssignmentPanel(AssignmentManagementAdapter assignmentAdapter, DeviceManagementAdapter deviceAdapter) {
        this.assignmentAdapter = assignmentAdapter;
        this.deviceAdapter = deviceAdapter;
        setLayout(new BorderLayout());
        initializeComponents();
    }

    private void initializeComponents() {
        // Create toolbar
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        // Status filter
        statusFilter = new JComboBox<>(new String[]{"All", "Active", "Returned"});
        statusFilter.addActionListener(e -> refreshAssignmentTable());
        toolBar.add(new JLabel("Status: "));
        toolBar.add(statusFilter);

        // Buttons
        assignButton = new JButton("Assign Device");
        returnButton = new JButton("Return Device");
        deleteButton = new JButton("Delete Assignment");

        toolBar.add(assignButton);
        toolBar.add(returnButton);
        toolBar.add(deleteButton);

        // Table
        String[] columns = {
            "ID",
            "Device",
            "Employee",
            "Department",
            "Assigned Date",
            "Return Date",
            "Status"
        };
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        assignmentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(assignmentTable);

        // Add components
        add(toolBar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Add listeners
        assignButton.addActionListener(e -> showAssignDeviceDialog());
        returnButton.addActionListener(e -> returnSelectedDevice());
        deleteButton.addActionListener(e -> deleteSelectedAssignment());

        // Initial data load
        refreshAssignmentTable();
    }

    private void showAssignDeviceDialog() {
        // Get list of available devices
        List<DeviceInfoRecord> availableDevices = deviceAdapter.getAllDevices().stream()
            .filter(d -> "Available".equals(d.status()))
            .toList();

        if (availableDevices.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No available devices to assign.",
                "No Devices",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Create form components
        JComboBox<String> deviceCombo = new JComboBox<>(
            availableDevices.stream()
                .map(d -> d.id() + " - " + d.name())
                .toArray(String[]::new)
        );
        JTextField employeeIdField = new JTextField(10);
        JTextField employeeNameField = new JTextField(20);
        JTextField departmentField = new JTextField(20);

        // Create form panel
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        form.add(new JLabel("Device:"), gbc);
        gbc.gridy++;
        form.add(new JLabel("Employee ID:"), gbc);
        gbc.gridy++;
        form.add(new JLabel("Employee Name:"), gbc);
        gbc.gridy++;
        form.add(new JLabel("Department:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        form.add(deviceCombo, gbc);
        gbc.gridy++;
        form.add(employeeIdField, gbc);
        gbc.gridy++;
        form.add(employeeNameField, gbc);
        gbc.gridy++;
        form.add(departmentField, gbc);

        int result = JOptionPane.showConfirmDialog(this, form,
            "Assign Device",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                // Parse device ID
                String deviceSelection = (String) deviceCombo.getSelectedItem();
                int deviceId = Integer.parseInt(deviceSelection.split(" - ")[0]);
                int employeeId = Integer.parseInt(employeeIdField.getText().trim());

                assignmentAdapter.assignDevice(employeeId, deviceId);
                refreshAssignmentTable();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                    "Invalid ID format. Please enter valid numbers.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void returnSelectedDevice() {
        int selectedRow = assignmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select an assignment to return",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer assignmentId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String status = (String) tableModel.getValueAt(selectedRow, 6);
        
        if ("Returned".equals(status)) {
            JOptionPane.showMessageDialog(this,
                "This device has already been returned",
                "Already Returned",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to mark this device as returned?",
            "Confirm Return",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            assignmentAdapter.returnDevice(assignmentId);
            refreshAssignmentTable();
        }
    }

    private void deleteSelectedAssignment() {
        int selectedRow = assignmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select an assignment to delete",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer assignmentId = (Integer) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this assignment record?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            assignmentAdapter.deleteAssignment(assignmentId);
            refreshAssignmentTable();
        }
    }

    private void refreshAssignmentTable() {
        tableModel.setRowCount(0);
        String selectedStatus = (String) statusFilter.getSelectedItem();
        
        List<DeviceAssignmentRecord> assignments;
        if ("All".equals(selectedStatus)) {
            assignments = assignmentAdapter.getAllAssignments();
        } else if ("Active".equals(selectedStatus)) {
            assignments = assignmentAdapter.getActiveAssignments();
        } else {
            assignments = assignmentAdapter.getAllAssignments().stream()
                .filter(a -> "Returned".equals(a.status()))
                .toList();
        }

        for (DeviceAssignmentRecord assignment : assignments) {
            Object[] row = {
                assignment.id(),
                assignment.deviceName(),
                assignment.employeeName(),
                assignment.department(),
                assignment.assignedAt().format(DATE_FORMATTER),
                assignment.returnedAt() != null ? 
                    assignment.returnedAt().format(DATE_FORMATTER) : "",
                assignment.status()
            };
            tableModel.addRow(row);
        }
    }
} 