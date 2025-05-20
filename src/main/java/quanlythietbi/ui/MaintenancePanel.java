package quanlythietbi.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import quanlythietbi.entity.MaintenanceRecord;
import quanlythietbi.service.adapter.DeviceManagementAdapter;
import quanlythietbi.service.adapter.MaintenanceManagementAdapter;
import quanlythietbi.ui.components.DateTimePicker;

public class MaintenancePanel extends JPanel implements RefreshablePanel {
    private final MaintenanceManagementAdapter maintenanceAdapter;
    private final DeviceManagementAdapter deviceAdapter;
    private JTable maintenanceTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton, completeButton;
    private JComboBox<String> statusFilter;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private javax.swing.Timer autoRefreshTimer;
    private boolean autoRefreshEnabled = false;

    public MaintenancePanel(MaintenanceManagementAdapter maintenanceAdapter, DeviceManagementAdapter deviceAdapter) {
        this.maintenanceAdapter = maintenanceAdapter;
        this.deviceAdapter = deviceAdapter;
        setLayout(new BorderLayout());
        initializeComponents();
        // Add component listener for tab switch
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                refreshMaintenanceTable();
                setAutoRefreshEnabled(true);
            }
            @Override
            public void componentHidden(java.awt.event.ComponentEvent e) {
                setAutoRefreshEnabled(false);
            }
        });
        // Setup auto-refresh timer
        autoRefreshTimer = new javax.swing.Timer(10_000, e -> refreshMaintenanceTable());
        autoRefreshTimer.setRepeats(true);
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
        DateTimePicker scheduledPicker = new DateTimePicker();

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
        form.add(scheduledPicker, gbc);

        // Create custom dialog
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Add Maintenance Record", true);
        dialog.setLayout(new BorderLayout());
        dialog.add(form, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Add button listeners
        okButton.addActionListener(e -> {
            try {
                // Validate cost
                String costText = costField.getText().trim();
                if (costText.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog,
                        "Please enter a cost value.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                    costField.requestFocus();
                    return;
                }
                BigDecimal cost = new BigDecimal(costText);

                // Validate description
                if (descArea.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog,
                        "Please enter a description.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                    descArea.requestFocus();
                    return;
                }

                // Get scheduled date
                LocalDateTime scheduledDate = scheduledPicker.getValue();
                if (scheduledDate == null) {
                    JOptionPane.showMessageDialog(dialog,
                        "Please select a scheduled date and time.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                    scheduledPicker.requestFocus();
                    return;
                }

                // Parse device ID
                String deviceSelection = (String) deviceCombo.getSelectedItem();
                int deviceId = Integer.parseInt(deviceSelection.split(" - ")[0]);

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
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Invalid cost value. Please enter a valid number.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                costField.requestFocus();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        // Set dialog properties
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.setVisible(true);
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
            DateTimePicker scheduledPicker = new DateTimePicker();
            scheduledPicker.setValue(record.scheduledFor());
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
            form.add(scheduledPicker, gbc);
            gbc.gridy++;
            form.add(statusCombo, gbc);

            // Create custom dialog
            JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Edit Maintenance Record", true);
            dialog.setLayout(new BorderLayout());
            dialog.add(form, BorderLayout.CENTER);

            // Create button panel
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton okButton = new JButton("OK");
            JButton cancelButton = new JButton("Cancel");
            buttonPanel.add(okButton);
            buttonPanel.add(cancelButton);
            dialog.add(buttonPanel, BorderLayout.SOUTH);

            // Add button listeners
            okButton.addActionListener(e -> {
                try {
                    // Validate cost
                    String costText = costField.getText().trim();
                    if (costText.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog,
                            "Please enter a cost value.",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                        costField.requestFocus();
                        return;
                    }
                    BigDecimal cost = new BigDecimal(costText);

                    // Validate description
                    if (descArea.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(dialog,
                            "Please enter a description.",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                        descArea.requestFocus();
                        return;
                    }

                    // Get scheduled date
                    LocalDateTime scheduledDate = scheduledPicker.getValue();
                    if (scheduledDate == null) {
                        JOptionPane.showMessageDialog(dialog,
                            "Please select a scheduled date and time.",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                        scheduledPicker.requestFocus();
                        return;
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
                    dialog.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog,
                        "Invalid cost value. Please enter a valid number.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                    costField.requestFocus();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog,
                        "Error: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            });

            cancelButton.addActionListener(e -> dialog.dispose());

            // Set dialog properties
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setResizable(false);
            dialog.setVisible(true);
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

    @Override
    public void refreshData() {
        refreshMaintenanceTable();
    }

    @Override
    public void setAutoRefreshEnabled(boolean enabled) {
        this.autoRefreshEnabled = enabled;
        if (enabled) {
            if (!autoRefreshTimer.isRunning()) autoRefreshTimer.start();
        } else {
            if (autoRefreshTimer.isRunning()) autoRefreshTimer.stop();
        }
    }
} 