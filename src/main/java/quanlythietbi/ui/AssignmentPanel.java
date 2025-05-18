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
import javax.swing.table.DefaultTableModel;

import quanlythietbi.entity.DeviceAssignmentRecord;
import quanlythietbi.entity.DeviceInfoRecord;
import quanlythietbi.entity.EmployeeInfoRecord;
import quanlythietbi.service.adapter.AssignmentManagementAdapter;
import quanlythietbi.service.adapter.DeviceManagementAdapter;

public class AssignmentPanel extends JPanel {
    private final AssignmentManagementAdapter assignmentAdapter;
    private final DeviceManagementAdapter deviceAdapter;
    private JTable assignmentTable;
    private DefaultTableModel tableModel;
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
        JButton assignButton = new JButton("Assign Device");
        JButton returnButton = new JButton("Return Device");
        
        toolBar.add(assignButton);
        toolBar.add(returnButton);

        // Table
        String[] columns = {
            "ID",
            "Device",
            "Employee",
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

        // Initial data load
        refreshAssignmentTable();
    }

    private void showAssignDeviceDialog() {
        // Get available devices (excluding those in maintenance/repair)
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

        // Get list of employees
        List<EmployeeInfoRecord> employees = assignmentAdapter.getAllEmployees();
        if (employees.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No employees found in the system.",
                "No Employees",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Create device combo box
        JComboBox<DeviceInfoRecord> deviceCombo = new JComboBox<>(
            availableDevices.toArray(new DeviceInfoRecord[0])
        );
        deviceCombo.setRenderer((list, value, index, isSelected, cellHasFocus) -> 
            new JLabel(value != null ? value.name() + " (" + value.serialNumber() + ")" : ""));

        // Create employee combo box
        JComboBox<EmployeeInfoRecord> employeeCombo = new JComboBox<>(
            employees.toArray(new EmployeeInfoRecord[0])
        );
        employeeCombo.setRenderer((list, value, index, isSelected, cellHasFocus) -> 
            new JLabel(value != null ? value.name() + " (" + value.department() + ")" : ""));

        // Create form panel
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        form.add(new JLabel("Device:"), gbc);
        gbc.gridy++;
        form.add(new JLabel("Employee:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        form.add(deviceCombo, gbc);
        gbc.gridy++;
        form.add(employeeCombo, gbc);

        int result = JOptionPane.showConfirmDialog(this, form,
            "Assign Device",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            DeviceInfoRecord selectedDevice = (DeviceInfoRecord) deviceCombo.getSelectedItem();
            EmployeeInfoRecord selectedEmployee = (EmployeeInfoRecord) employeeCombo.getSelectedItem();

            if (selectedDevice != null && selectedEmployee != null) {
                assignmentAdapter.assignDevice(selectedDevice.id(), selectedEmployee.id());
                refreshAssignmentTable();
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
        String status = (String) tableModel.getValueAt(selectedRow, 5);

        if ("Returned".equals(status)) {
            JOptionPane.showMessageDialog(this,
                "This device has already been returned",
                "Already Returned",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String notes = JOptionPane.showInputDialog(this,
            "Enter return notes (optional):",
            "Return Device",
            JOptionPane.PLAIN_MESSAGE);

        if (notes != null) { // null means user cancelled
            assignmentAdapter.returnDevice(assignmentId);
            refreshAssignmentTable();
        }
    }

    private void refreshAssignmentTable() {
        tableModel.setRowCount(0);
        List<DeviceAssignmentRecord> assignments = assignmentAdapter.getAllAssignments();
        
        for (DeviceAssignmentRecord assignment : assignments) {
            tableModel.addRow(new Object[]{
                assignment.id(),
                assignment.deviceName(),
                assignment.employeeName(),
                assignment.assignedAt().format(DATE_FORMATTER),
                assignment.returnedAt() != null ? 
                    assignment.returnedAt().format(DATE_FORMATTER) : "",
                assignment.status()
            });
        }
    }
} 