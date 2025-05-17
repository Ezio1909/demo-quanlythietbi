package quanlythietbi.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import quanlythietbi.entity.DeviceAssignmentRecord;
import quanlythietbi.service.adapter.AssignmentManagementAdapter;

public class AssignmentPanel extends JPanel {
    private final AssignmentManagementAdapter adapter;
    private JTable assignmentTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> filterCombo;
    private JTextField searchField;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public AssignmentPanel(AssignmentManagementAdapter adapter) {
        this.adapter = adapter;
        setLayout(new BorderLayout());
        
        // Add title
        JLabel titleLabel = new JLabel("Device Assignments", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Create toolbar
        JPanel toolBar = createToolBar();
        add(toolBar, BorderLayout.NORTH);
        
        // Create table
        createAssignmentTable();
        JScrollPane scrollPane = new JScrollPane(assignmentTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Add bottom panel for actions
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Load initial data
        refreshTable();
    }
    
    private JPanel createToolBar() {
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        toolBar.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        
        // Filter combo
        filterCombo = new JComboBox<>(new String[]{"All Assignments", "Active Only", "Returned"});
        filterCombo.addActionListener(e -> refreshTable());
        
        // Search field
        searchField = new JTextField(20);
        searchField.putClientProperty("JTextField.placeholderText", "Search assignments...");
        
        // Buttons
        JButton assignButton = new JButton("Assign Device");
        assignButton.setBackground(new Color(51, 122, 183));
        assignButton.setForeground(Color.WHITE);
        assignButton.setFocusPainted(false);
        assignButton.addActionListener(e -> showAssignDialog());
        
        toolBar.add(new JLabel("Filter:"));
        toolBar.add(filterCombo);
        toolBar.add(Box.createHorizontalStrut(20));
        toolBar.add(new JLabel("Search:"));
        toolBar.add(searchField);
        toolBar.add(Box.createHorizontalStrut(20));
        toolBar.add(assignButton);
        
        return toolBar;
    }
    
    private void createAssignmentTable() {
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
        assignmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        assignmentTable.getTableHeader().setReorderingAllowed(false);
        
        // Set column widths
        assignmentTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        assignmentTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        assignmentTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        assignmentTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        assignmentTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        assignmentTable.getColumnModel().getColumn(5).setPreferredWidth(120);
        assignmentTable.getColumnModel().getColumn(6).setPreferredWidth(80);
    }
    
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton returnButton = new JButton("Return Device");
        returnButton.addActionListener(e -> returnSelectedDevice());
        
        panel.add(returnButton);
        return panel;
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        List<DeviceAssignmentRecord> assignments;
        
        switch (filterCombo.getSelectedIndex()) {
            case 1 -> assignments = adapter.getActiveAssignments();
            default -> assignments = adapter.getAllAssignments();
        }
        
        for (DeviceAssignmentRecord assignment : assignments) {
            Object[] row = {
                assignment.id(),
                assignment.deviceName(),
                assignment.employeeName(),
                assignment.department(),
                assignment.assignedAt().format(DATE_FORMATTER),
                assignment.returnedAt() != null ? assignment.returnedAt().format(DATE_FORMATTER) : "-",
                assignment.status()
            };
            tableModel.addRow(row);
        }
    }
    
    private void showAssignDialog() {
        // In a real application, these would be populated from the database
        String[] employees = {"John Doe", "Jane Smith", "Bob Johnson"};
        String[] devices = {"Laptop Dell XPS", "iPhone 12", "HP Printer"};
        
        JComboBox<String> employeeCombo = new JComboBox<>(employees);
        JComboBox<String> deviceCombo = new JComboBox<>(devices);
        
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("Employee:"));
        panel.add(employeeCombo);
        panel.add(new JLabel("Device:"));
        panel.add(deviceCombo);
        
        int result = JOptionPane.showConfirmDialog(
            this,
            panel,
            "Assign Device",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );
        
        if (result == JOptionPane.OK_OPTION) {
            // In a real application, you would get the actual IDs
            adapter.assignDevice(1, 1);
            refreshTable();
        }
    }
    
    private void returnSelectedDevice() {
        int selectedRow = assignmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Please select an assignment to return",
                "No Selection",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        int assignmentId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String status = (String) tableModel.getValueAt(selectedRow, 6);
        
        if ("Returned".equals(status)) {
            JOptionPane.showMessageDialog(
                this,
                "This device has already been returned",
                "Already Returned",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to mark this device as returned?",
            "Confirm Return",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            adapter.returnDevice(assignmentId);
            refreshTable();
        }
    }
} 