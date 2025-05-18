package quanlythietbi.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import quanlythietbi.entity.DeviceAssignmentRecord;
import quanlythietbi.entity.EmployeeInfoRecord;
import quanlythietbi.service.adapter.AssignmentManagementAdapter;
import quanlythietbi.service.adapter.DeviceManagementAdapter;
import quanlythietbi.ui.components.SortableTable;
import quanlythietbi.ui.dialogs.AssignDeviceDialog;
import quanlythietbi.ui.dialogs.EmployeeDevicesDialog;

public class AssignmentPanel extends JPanel {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final AssignmentManagementAdapter assignmentAdapter;
    private final DeviceManagementAdapter deviceAdapter;
    private SortableTable assignmentTable;
    private DefaultTableModel tableModel;

    public AssignmentPanel(AssignmentManagementAdapter assignmentAdapter, DeviceManagementAdapter deviceAdapter) {
        this.assignmentAdapter = assignmentAdapter;
        this.deviceAdapter = deviceAdapter;
        setLayout(new BorderLayout(10, 10));
        initializeComponents();
        refreshAssignmentTable();
    }

    private void initializeComponents() {
        // Top panel with buttons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        
        JButton assignButton = new JButton("Assign Device");
        JButton returnButton = new JButton("Return Device");
        
        assignButton.addActionListener(e -> showAssignDeviceDialog());
        returnButton.addActionListener(e -> returnSelectedDevice());
        
        topPanel.add(assignButton);
        topPanel.add(returnButton);
        add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {
            "ID",
            "Device",
            "Employee",
            "Department",
            "Assigned Date",
            "Expiration Date",
            "Returned Date",
            "Status"
        };
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        assignmentTable = new SortableTable(tableModel);
        
        // Add mouse listener for employee name clicks
        assignmentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = assignmentTable.rowAtPoint(e.getPoint());
                int col = assignmentTable.columnAtPoint(e.getPoint());
                
                if (row >= 0 && col == 2) { // Employee column
                    String employeeName = (String) tableModel.getValueAt(row, col);
                    String department = (String) tableModel.getValueAt(row, 3);
                    
                    // Find employee by name and department
                    List<EmployeeInfoRecord> employees = assignmentAdapter.getAllEmployees();
                    employees.stream()
                        .filter(emp -> emp.name().equals(employeeName) && emp.department().equals(department))
                        .findFirst()
                        .ifPresent(employee -> showEmployeeDevicesDialog(employee));
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(assignmentTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void showAssignDeviceDialog() {
        AssignDeviceDialog dialog = new AssignDeviceDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            assignmentAdapter,
            deviceAdapter
        );
        dialog.setVisible(true);
        refreshAssignmentTable();
    }

    private void showEmployeeDevicesDialog(EmployeeInfoRecord employee) {
        EmployeeDevicesDialog dialog = new EmployeeDevicesDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            assignmentAdapter,
            employee
        );
        dialog.setVisible(true);
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

        int modelRow = assignmentTable.convertRowIndexToModel(selectedRow);
        Integer assignmentId = (Integer) tableModel.getValueAt(modelRow, 0);
        String status = (String) tableModel.getValueAt(modelRow, 7);

        if ("Returned".equals(status)) {
            JOptionPane.showMessageDialog(this,
                "This device has already been returned",
                "Already Returned",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to return this device?",
            "Confirm Return",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
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
                assignment.department(),
                assignment.assignedAt().format(DATE_FORMATTER),
                assignment.expirationDate().format(DATE_FORMATTER),
                assignment.returnedAt() != null ? 
                    assignment.returnedAt().format(DATE_FORMATTER) : "",
                assignment.status()
            });
        }
    }
} 