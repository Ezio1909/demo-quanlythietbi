package quanlythietbi.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import quanlythietbi.entity.MaintenanceRecord;
import quanlythietbi.service.adapter.MaintenanceManagementAdapter;
import quanlythietbi.service.adapter.DeviceManagementAdapter;

public class MaintenancePanel extends JPanel {
    private final MaintenanceManagementAdapter maintenanceAdapter;
    private final DeviceManagementAdapter deviceAdapter;
    private JTable maintenanceTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton, completeButton;
    private JComboBox<String> statusFilter;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public MaintenancePanel(MaintenanceManagementAdapter maintenanceAdapter, DeviceManagementAdapter deviceAdapter) {
        this.maintenanceAdapter = maintenanceAdapter;
        this.deviceAdapter = deviceAdapter;
        setLayout(new BorderLayout());
        initializeComponents();
    }

    private void initializeComponents() {
        // Create toolbar
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        // Status filter
        statusFilter = new JComboBox<>(new String[]{"All", "Pending", "Scheduled", "In Progress", "Completed"});
        statusFilter.addActionListener(e -> refreshMaintenanceTable());
        toolBar.add(new JLabel("Status: "));
        toolBar.add(statusFilter);

        // Buttons
        addButton = new JButton("Add Maintenance");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        completeButton = new JButton("Mark Complete");

        toolBar.add(addButton);
        toolBar.add(editButton);
        toolBar.add(deleteButton);
        toolBar.add(completeButton);

        // Table
        String[] columns = {
            "ID",
            "Device",
            "Type",
            "Description",
            "Reported",
            "Scheduled",
            "Completed",
            "Cost",
            "Status"
        };
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        maintenanceTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(maintenanceTable);

        // Add components
        add(toolBar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Add listeners
        addButton.addActionListener(e -> showAddMaintenanceDialog());
        editButton.addActionListener(e -> showEditMaintenanceDialog());
        deleteButton.addActionListener(e -> deleteSelectedMaintenance());
        completeButton.addActionListener(e -> completeSelectedMaintenance());

        // Initial data load
        refreshMaintenanceTable();
    }

    private void showAddMaintenanceDialog() {
        // Get list of devices for combo box
        var devices = deviceAdapter.getAllDevices();
        if (devices.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No devices available. Please add devices first.",
                "No Devices",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Create form components
        JComboBox<String> deviceCombo = new JComboBox<>(
            devices.stream()
                .map(d -> d.id() + " - " + d.name())
                .toArray(String[]::new)
        );
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{
            "Repair",
            "Inspection",
            "Upgrade",
            "Replacement",
            "Cleaning"
        });
        JTextArea descArea = new JTextArea(3, 30);
        JTextField costField = new JTextField(10);
        JTextField scheduledField = new JTextField(16);
        scheduledField.setToolTipText("Format: YYYY-MM-DD HH:mm");

        // Create form panel
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        form.add(new JLabel("Device:"), gbc);
        gbc.gridy++;
        form.add(new JLabel("Type:"), gbc);
        gbc.gridy++;
        form.add(new JLabel("Description:"), gbc);
        gbc.gridy++;
        form.add(new JLabel("Cost:"), gbc);
        gbc.gridy++;
        form.add(new JLabel("Scheduled For:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        form.add(deviceCombo, gbc);
        gbc.gridy++;
        form.add(typeCombo, gbc);
        gbc.gridy++;
        form.add(new JScrollPane(descArea), gbc);
        gbc.gridy++;
        form.add(costField, gbc);
        gbc.gridy++;
        form.add(scheduledField, gbc);

        int result = JOptionPane.showConfirmDialog(this, form,
            "Add Maintenance Record",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                // Parse device ID
                String deviceSelection = (String) deviceCombo.getSelectedItem();
                int deviceId = Integer.parseInt(deviceSelection.split(" - ")[0]);

                // Parse cost
                BigDecimal cost = new BigDecimal(costField.getText().trim());

                // Parse scheduled date
                LocalDateTime scheduledDate = null;
                if (!scheduledField.getText().trim().isEmpty()) {
                    scheduledDate = LocalDateTime.parse(
                        scheduledField.getText().trim(),
                        DATE_FORMATTER
                    );
                }

                MaintenanceRecord newRecord = new MaintenanceRecord(
                    null, // ID will be assigned by database
                    deviceId,
                    deviceSelection.split(" - ")[1], // Device name
                    (String) typeCombo.getSelectedItem(),
                    descArea.getText().trim(),
                    LocalDateTime.now(),
                    scheduledDate,
                    null, // completed_at
                    cost,
                    "Pending",
                    null // notes
                );

                maintenanceAdapter.addMaintenanceRecord(newRecord);
                refreshMaintenanceTable();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                    "Invalid cost value. Please enter a valid number.",
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

    private void showEditMaintenanceDialog() {
        int selectedRow = maintenanceTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a maintenance record to edit",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer recordId = (Integer) tableModel.getValueAt(selectedRow, 0);
        maintenanceAdapter.getMaintenanceRecord(recordId).ifPresent(record -> {
            // Create form components
            JTextField typeField = new JTextField(record.maintenanceType());
            JTextArea descArea = new JTextArea(record.description(), 3, 30);
            JTextField costField = new JTextField(
                record.cost() != null ? record.cost().toString() : ""
            );
            JTextField scheduledField = new JTextField(
                record.scheduledFor() != null ? 
                record.scheduledFor().format(DATE_FORMATTER) : ""
            );
            JComboBox<String> statusCombo = new JComboBox<>(new String[]{
                "Pending",
                "Scheduled",
                "In Progress",
                "Completed"
            });
            statusCombo.setSelectedItem(record.status());

            // Create form panel
            JPanel form = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.insets = new Insets(5, 5, 5, 5);

            form.add(new JLabel("Type:"), gbc);
            gbc.gridy++;
            form.add(new JLabel("Description:"), gbc);
            gbc.gridy++;
            form.add(new JLabel("Cost:"), gbc);
            gbc.gridy++;
            form.add(new JLabel("Scheduled For:"), gbc);
            gbc.gridy++;
            form.add(new JLabel("Status:"), gbc);

            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            form.add(typeField, gbc);
            gbc.gridy++;
            form.add(new JScrollPane(descArea), gbc);
            gbc.gridy++;
            form.add(costField, gbc);
            gbc.gridy++;
            form.add(scheduledField, gbc);
            gbc.gridy++;
            form.add(statusCombo, gbc);

            int result = JOptionPane.showConfirmDialog(this, form,
                "Edit Maintenance Record",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    // Parse cost
                    BigDecimal cost = new BigDecimal(costField.getText().trim());

                    // Parse scheduled date
                    LocalDateTime scheduledDate = null;
                    if (!scheduledField.getText().trim().isEmpty()) {
                        scheduledDate = LocalDateTime.parse(
                            scheduledField.getText().trim(),
                            DATE_FORMATTER
                        );
                    }

                    MaintenanceRecord updatedRecord = new MaintenanceRecord(
                        record.id(),
                        record.deviceId(),
                        record.deviceName(),
                        typeField.getText().trim(),
                        descArea.getText().trim(),
                        record.reportedAt(),
                        scheduledDate,
                        record.completedAt(),
                        cost,
                        (String) statusCombo.getSelectedItem(),
                        record.notes()
                    );

                    maintenanceAdapter.updateMaintenanceRecord(updatedRecord);
                    refreshMaintenanceTable();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this,
                        "Invalid cost value. Please enter a valid number.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this,
                        "Error: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void deleteSelectedMaintenance() {
        int selectedRow = maintenanceTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a maintenance record to delete",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer recordId = (Integer) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this maintenance record?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            maintenanceAdapter.deleteMaintenanceRecord(recordId);
            refreshMaintenanceTable();
        }
    }

    private void completeSelectedMaintenance() {
        int selectedRow = maintenanceTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a maintenance record to complete",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer recordId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String status = (String) tableModel.getValueAt(selectedRow, 8);
        
        if ("Completed".equals(status)) {
            JOptionPane.showMessageDialog(this,
                "This maintenance record is already completed",
                "Already Completed",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String notes = JOptionPane.showInputDialog(this,
            "Enter completion notes (optional):",
            "Complete Maintenance",
            JOptionPane.PLAIN_MESSAGE);

        if (notes != null) { // null means user cancelled
            maintenanceAdapter.completeMaintenanceRecord(recordId, notes);
            refreshMaintenanceTable();
        }
    }

    private void refreshMaintenanceTable() {
        tableModel.setRowCount(0);
        String selectedStatus = (String) statusFilter.getSelectedItem();
        
        List<MaintenanceRecord> records;
        if ("All".equals(selectedStatus)) {
            records = maintenanceAdapter.getAllMaintenanceRecords();
        } else {
            records = maintenanceAdapter.getMaintenanceRecordsByStatus(selectedStatus);
        }

        for (MaintenanceRecord record : records) {
            Object[] row = {
                record.id(),
                record.deviceName(),
                record.maintenanceType(),
                record.description(),
                record.reportedAt().format(DATE_FORMATTER),
                record.scheduledFor() != null ? 
                    record.scheduledFor().format(DATE_FORMATTER) : "",
                record.completedAt() != null ? 
                    record.completedAt().format(DATE_FORMATTER) : "",
                record.cost(),
                record.status()
            };
            tableModel.addRow(row);
        }
    }
} 