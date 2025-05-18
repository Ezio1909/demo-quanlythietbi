package quanlythietbi.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import quanlythietbi.entity.DeviceAssignmentRecord;
import quanlythietbi.entity.EmployeeInfoRecord;
import quanlythietbi.service.adapter.AssignmentManagementAdapter;
import quanlythietbi.ui.components.SortableTable;

public class EmployeeDevicesDialog extends JDialog {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final AssignmentManagementAdapter assignmentAdapter;
    private final EmployeeInfoRecord employee;
    private SortableTable deviceTable;
    private DefaultTableModel tableModel;

    public EmployeeDevicesDialog(Frame parent, AssignmentManagementAdapter assignmentAdapter, EmployeeInfoRecord employee) {
        super(parent, "Devices Assigned to " + employee.name(), true);
        this.assignmentAdapter = assignmentAdapter;
        this.employee = employee;
        initializeComponents();
        loadEmployeeDevices();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Employee info panel
        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        infoPanel.add(new JLabel("Name:"));
        infoPanel.add(new JLabel(employee.name()));
        infoPanel.add(new JLabel("Department:"));
        infoPanel.add(new JLabel(employee.department()));
        infoPanel.add(new JLabel("Employee ID:"));
        infoPanel.add(new JLabel(String.valueOf(employee.id())));
        add(infoPanel, BorderLayout.NORTH);

        // Devices table
        String[] columns = {
            "Device Name",
            "Type",
            "Asset Tag",
            "Assigned Date",
            "Expiration Date",
            "Status"
        };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        deviceTable = new SortableTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(deviceTable);
        add(scrollPane, BorderLayout.CENTER);

        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(800, 400));
    }

    private void loadEmployeeDevices() {
        tableModel.setRowCount(0);
        List<DeviceAssignmentRecord> assignments = assignmentAdapter.getAssignmentsByEmployee(employee.id());
        
        for (DeviceAssignmentRecord assignment : assignments) {
            if ("Active".equals(assignment.status())) {
                tableModel.addRow(new Object[]{
                    assignment.deviceName(),
                    "Device Type", // You might want to fetch this from device info
                    "Asset Tag",   // You might want to fetch this from device info
                    assignment.assignedAt().format(DATE_FORMATTER),
                    assignment.expirationDate().format(DATE_FORMATTER),
                    assignment.status()
                });
            }
        }
    }
} 