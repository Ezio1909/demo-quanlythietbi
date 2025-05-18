package quanlythietbi.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.toedter.calendar.JDateChooser;

import quanlythietbi.entity.DeviceInfoRecord;
import quanlythietbi.entity.EmployeeInfoRecord;
import quanlythietbi.service.adapter.AssignmentManagementAdapter;
import quanlythietbi.service.adapter.DeviceManagementAdapter;

public class AssignDeviceDialog extends JDialog {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final AssignmentManagementAdapter assignmentAdapter;
    private final DeviceManagementAdapter deviceAdapter;
    private JComboBox<DeviceInfoRecord> deviceCombo;
    private JComboBox<EmployeeInfoRecord> employeeCombo;
    private JDateChooser assignedDateChooser;
    private JDateChooser expirationDateChooser;

    public AssignDeviceDialog(Frame parent, AssignmentManagementAdapter assignmentAdapter, DeviceManagementAdapter deviceAdapter) {
        super(parent, "Assign Device", true);
        this.assignmentAdapter = assignmentAdapter;
        this.deviceAdapter = deviceAdapter;
        initializeComponents();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Form panel
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Get available devices
        List<DeviceInfoRecord> availableDevices = deviceAdapter.getAllDevices().stream()
            .filter(d -> "Available".equals(d.status()))
            .toList();

        // Get list of employees
        List<EmployeeInfoRecord> employees = assignmentAdapter.getAllEmployees();

        // Device selection
        form.add(new JLabel("Device:"), gbc);
        deviceCombo = new JComboBox<>(availableDevices.toArray(new DeviceInfoRecord[0]));
        deviceCombo.setRenderer((list, value, index, isSelected, cellHasFocus) -> 
            new JLabel(value != null ? value.name() + " (" + value.serialNumber() + ")" : ""));
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        form.add(deviceCombo, gbc);

        // Employee selection
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        form.add(new JLabel("Employee:"), gbc);
        employeeCombo = new JComboBox<>(employees.toArray(new EmployeeInfoRecord[0]));
        employeeCombo.setRenderer((list, value, index, isSelected, cellHasFocus) -> 
            new JLabel(value != null ? value.name() + " (" + value.department() + ")" : ""));
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        form.add(employeeCombo, gbc);

        // Assignment date
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        form.add(new JLabel("Assignment Date:"), gbc);
        assignedDateChooser = new JDateChooser();
        assignedDateChooser.setDate(new java.util.Date());
        assignedDateChooser.setDateFormatString("yyyy-MM-dd HH:mm");
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        form.add(assignedDateChooser, gbc);

        // Expiration date (default to 1 year from now)
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        form.add(new JLabel("Expiration Date:"), gbc);
        expirationDateChooser = new JDateChooser();
        expirationDateChooser.setDate(java.sql.Timestamp.valueOf(LocalDateTime.now().plusYears(1)));
        expirationDateChooser.setDateFormatString("yyyy-MM-dd HH:mm");
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        form.add(expirationDateChooser, gbc);

        add(form, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton assignButton = new JButton("Assign");
        JButton cancelButton = new JButton("Cancel");

        assignButton.addActionListener(e -> {
            if (validateAndAssign()) {
                dispose();
            }
        });
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(assignButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private boolean validateAndAssign() {
        DeviceInfoRecord selectedDevice = (DeviceInfoRecord) deviceCombo.getSelectedItem();
        EmployeeInfoRecord selectedEmployee = (EmployeeInfoRecord) employeeCombo.getSelectedItem();

        if (selectedDevice == null || selectedEmployee == null) {
            JOptionPane.showMessageDialog(this,
                "Please select both a device and an employee",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (assignedDateChooser.getDate() == null || expirationDateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this,
                "Please select both assignment and expiration dates",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        LocalDateTime assignedDate = assignedDateChooser.getDate().toInstant()
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalDateTime();
        LocalDateTime expirationDate = expirationDateChooser.getDate().toInstant()
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalDateTime();

        if (expirationDate.isBefore(assignedDate)) {
            JOptionPane.showMessageDialog(this,
                "Expiration date cannot be before assignment date",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            assignmentAdapter.assignDevice(selectedEmployee.id(), selectedDevice.id());
            return true;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Assignment Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
} 