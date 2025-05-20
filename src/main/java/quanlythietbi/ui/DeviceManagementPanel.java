package quanlythietbi.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import quanlythietbi.entity.DeviceInfoRecord;
import quanlythietbi.service.adapter.DeviceManagementAdapter;
import quanlythietbi.ui.components.DeviceFilterPanel;
import quanlythietbi.ui.components.SortableTable;

public class DeviceManagementPanel extends JPanel implements RefreshablePanel {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DeviceManagementAdapter adapter;
    private SortableTable deviceTable;
    private DefaultTableModel tableModel;
    private javax.swing.Timer autoRefreshTimer;
    private boolean autoRefreshEnabled = false;
    private JLabel autoRefreshLabel;

    public DeviceManagementPanel(DeviceManagementAdapter adapter) {
        this.adapter = adapter;
        setLayout(new BorderLayout(10, 10));
        // Create contentPanel with border and padding
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createTitledBorder(""));
        contentPanel.setBackground(Color.WHITE);
        // Initialize and add components to contentPanel
        JPanel centerPanel = initializeComponents();
        contentPanel.add(centerPanel, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);
        // Add auto-refresh label at bottom right
        autoRefreshLabel = new JLabel();
        autoRefreshLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 20));
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(autoRefreshLabel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
        refreshDeviceTable();
        // Add component listener for tab switch
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                refreshDeviceTable();
                setAutoRefreshEnabled(true);
            }
            @Override
            public void componentHidden(java.awt.event.ComponentEvent e) {
                setAutoRefreshEnabled(false);
            }
        });
        // Setup auto-refresh timer
        autoRefreshTimer = new javax.swing.Timer(10_000, e -> refreshDeviceTable());
        autoRefreshTimer.setRepeats(true);
    }

    private JPanel initializeComponents() {
        // Top panel with buttons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        // Buttons
        JButton addButton = new JButton("Add Device");
        JButton editButton = new JButton("Edit Device");
        JButton deleteButton = new JButton("Delete Device");
        
        addButton.addActionListener(e -> showAddDeviceDialog());
        editButton.addActionListener(e -> showEditDeviceDialog());
        deleteButton.addActionListener(e -> deleteSelectedDevice());
        
        topPanel.add(addButton);
        topPanel.add(editButton);
        topPanel.add(deleteButton);

        // Initialize table
        String[] columns = {
            "ID",
            "Name",
            "Type",
            "Serial Number",
            "Status",
            "Model",
            "Manufacturer",
            "Location",
            "Department",
            "Condition",
            "Asset Tag"
        };
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        deviceTable = new SortableTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(deviceTable);
        
        // Create filter panel
        List<DeviceInfoRecord> devices = adapter.getAllDevices();
        Set<String> statuses = new HashSet<>();
        Set<String> conditions = new HashSet<>();
        Set<String> departments = new HashSet<>();
        Set<String> locations = new HashSet<>();
        
        for (DeviceInfoRecord device : devices) {
            if (device.status() != null) statuses.add(device.status());
            if (device.deviceCondition() != null) conditions.add(device.deviceCondition());
            if (device.department() != null) departments.add(device.department());
            if (device.location() != null) locations.add(device.location());
        }

        DeviceFilterPanel filterPanel = new DeviceFilterPanel(
            statuses.toArray(new String[0]),
            conditions.toArray(new String[0]),
            departments.toArray(new String[0]),
            locations.toArray(new String[0]),
            name -> deviceTable.addFilter("Name", name),
            status -> deviceTable.addFilter("Status", status),
            condition -> deviceTable.addFilter("Condition", condition),
            department -> deviceTable.addFilter("Department", department),
            location -> deviceTable.addFilter("Location", location)
        );

        // Add components to panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        centerPanel.add(filterPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(topPanel, BorderLayout.SOUTH);
        return centerPanel;
    }

    private JPanel createBasicInfoPanel(JTextField nameField, JTextField typeField, 
            JTextField serialField, JComboBox<String> statusCombo, 
            JTextField modelField, JTextField manufacturerField) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add fields
        panel.add(new JLabel("Name:*"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Type:*"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Serial Number:*"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Status:*"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Model:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Manufacturer:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(nameField, gbc);
        gbc.gridy++;
        panel.add(typeField, gbc);
        gbc.gridy++;
        panel.add(serialField, gbc);
        gbc.gridy++;
        panel.add(statusCombo, gbc);
        gbc.gridy++;
        panel.add(modelField, gbc);
        gbc.gridy++;
        panel.add(manufacturerField, gbc);

        return panel;
    }

    private JPanel createPurchaseInfoPanel(JTextField purchaseDateField, 
            JTextField purchasePriceField, JTextField supplierField, 
            JTextField warrantyExpiryField) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        panel.add(new JLabel("Purchase Date:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Purchase Price:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Supplier:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Warranty Expiry:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(purchaseDateField, gbc);
        gbc.gridy++;
        panel.add(purchasePriceField, gbc);
        gbc.gridy++;
        panel.add(supplierField, gbc);
        gbc.gridy++;
        panel.add(warrantyExpiryField, gbc);

        return panel;
    }

    private JPanel createAssetInfoPanel(JTextField assetTagField, JTextField locationField,
            JTextField departmentField, JTextField categoryField) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        panel.add(new JLabel("Asset Tag:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Location:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Department:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Category:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(assetTagField, gbc);
        gbc.gridy++;
        panel.add(locationField, gbc);
        gbc.gridy++;
        panel.add(departmentField, gbc);
        gbc.gridy++;
        panel.add(categoryField, gbc);

        return panel;
    }

    private JPanel createLifecyclePanel(JTextField lastInspectionField, 
            JTextField nextInspectionField, JTextField endOfLifeField,
            JComboBox<String> conditionCombo, JTextArea notesArea) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        panel.add(new JLabel("Last Inspection:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Next Inspection:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("End of Life:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Condition:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Notes:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(lastInspectionField, gbc);
        gbc.gridy++;
        panel.add(nextInspectionField, gbc);
        gbc.gridy++;
        panel.add(endOfLifeField, gbc);
        gbc.gridy++;
        panel.add(conditionCombo, gbc);
        gbc.gridy++;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        panel.add(new JScrollPane(notesArea), gbc);

        return panel;
    }

    private void showAddDeviceDialog() {
        // Create all form fields
        JTextField nameField = new JTextField(20);
        JTextField typeField = new JTextField(20);
        JTextField serialField = new JTextField(20);
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Available", "In Use", "Maintenance", "Retired"});
        JTextField modelField = new JTextField(20);
        JTextField manufacturerField = new JTextField(20);
        
        JTextField purchaseDateField = new JTextField(10);
        JTextField purchasePriceField = new JTextField(10);
        JTextField supplierField = new JTextField(20);
        JTextField warrantyExpiryField = new JTextField(10);
        
        JTextField assetTagField = new JTextField(20);
        JTextField locationField = new JTextField(20);
        JTextField departmentField = new JTextField(20);
        JTextField categoryField = new JTextField(20);
        
        JTextField lastInspectionField = new JTextField(10);
        JTextField nextInspectionField = new JTextField(10);
        JTextField endOfLifeField = new JTextField(10);
        JComboBox<String> conditionCombo = new JComboBox<>(new String[]{"New", "Good", "Fair", "Poor"});
        JTextArea notesArea = new JTextArea(4, 30);

        // Add date format tooltips
        purchaseDateField.setToolTipText("Format: YYYY-MM-DD");
        warrantyExpiryField.setToolTipText("Format: YYYY-MM-DD");
        lastInspectionField.setToolTipText("Format: YYYY-MM-DD");
        nextInspectionField.setToolTipText("Format: YYYY-MM-DD");
        endOfLifeField.setToolTipText("Format: YYYY-MM-DD");

        // Create tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Basic Info", createBasicInfoPanel(nameField, typeField, serialField, statusCombo, modelField, manufacturerField));
        tabbedPane.addTab("Purchase", createPurchaseInfoPanel(purchaseDateField, purchasePriceField, supplierField, warrantyExpiryField));
        tabbedPane.addTab("Asset", createAssetInfoPanel(assetTagField, locationField, departmentField, categoryField));
        tabbedPane.addTab("Lifecycle", createLifecyclePanel(lastInspectionField, nextInspectionField, endOfLifeField, conditionCombo, notesArea));

        int option = JOptionPane.showConfirmDialog(this, tabbedPane, "Add New Device", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            try {
                // Validate required fields
                String name = nameField.getText().trim();
                String type = typeField.getText().trim();
                String serial = serialField.getText().trim();
                
                if (name.isEmpty() || type.isEmpty() || serial.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Name, Type, and Serial Number are required", 
                        "Validation Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Parse dates
                LocalDate purchaseDate = purchaseDateField.getText().trim().isEmpty() ? null :
                    LocalDate.parse(purchaseDateField.getText().trim(), DATE_FORMATTER);
                LocalDate warrantyExpiry = warrantyExpiryField.getText().trim().isEmpty() ? null :
                    LocalDate.parse(warrantyExpiryField.getText().trim(), DATE_FORMATTER);
                LocalDate lastInspection = lastInspectionField.getText().trim().isEmpty() ? null :
                    LocalDate.parse(lastInspectionField.getText().trim(), DATE_FORMATTER);
                LocalDate nextInspection = nextInspectionField.getText().trim().isEmpty() ? null :
                    LocalDate.parse(nextInspectionField.getText().trim(), DATE_FORMATTER);
                LocalDate endOfLife = endOfLifeField.getText().trim().isEmpty() ? null :
                    LocalDate.parse(endOfLifeField.getText().trim(), DATE_FORMATTER);

                // Parse purchase price
                BigDecimal purchasePrice = purchasePriceField.getText().trim().isEmpty() ? null :
                    new BigDecimal(purchasePriceField.getText().trim());

                DeviceInfoRecord newDevice = new DeviceInfoRecord(
                    null, // ID will be assigned by database
                    name,
                    type,
                    serial,
                    statusCombo.getSelectedItem().toString(),
                    purchaseDate,
                    purchasePrice,
                    supplierField.getText().trim(),
                    warrantyExpiry,
                    modelField.getText().trim(),
                    manufacturerField.getText().trim(),
                    null, // specifications
                    null, // firmware version
                    assetTagField.getText().trim(),
                    locationField.getText().trim(),
                    departmentField.getText().trim(),
                    categoryField.getText().trim(),
                    lastInspection,
                    nextInspection,
                    endOfLife,
                    conditionCombo.getSelectedItem().toString(),
                    notesArea.getText().trim(),
                    null, // created_at (set by database)
                    null, // updated_at (set by database)
                    "system", // created_by
                    "system"  // last_modified_by
                );

                adapter.addDevice(newDevice);
                refreshDeviceTable();
            } catch (Exception e) {
                String msg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
                if (msg.contains("duplicate") && msg.contains("serial_number")) {
                    JOptionPane.showMessageDialog(this, 
                        "Cannot add due to duplicate serial number", 
                        "Add Not Allowed", 
                        JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "An unexpected error occurred. Please try again.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void showEditDeviceDialog() {
        int selectedRow = deviceTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a device to edit", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer deviceId = (Integer) tableModel.getValueAt(selectedRow, 0);
        adapter.getDevice(deviceId).ifPresent(device -> {
            // Create and populate form fields
            JTextField nameField = new JTextField(device.name(), 20);
            JTextField typeField = new JTextField(device.type(), 20);
            JTextField serialField = new JTextField(device.serialNumber(), 20);
            JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Available", "In Use", "Maintenance", "Retired"});
            statusCombo.setSelectedItem(device.status());
            JTextField modelField = new JTextField(device.model(), 20);
            JTextField manufacturerField = new JTextField(device.manufacturer(), 20);
            
            JTextField purchaseDateField = new JTextField(device.purchaseDate() != null ? 
                device.purchaseDate().format(DATE_FORMATTER) : "", 10);
            JTextField purchasePriceField = new JTextField(device.purchasePrice() != null ? 
                device.purchasePrice().toString() : "", 10);
            JTextField supplierField = new JTextField(device.supplier(), 20);
            JTextField warrantyExpiryField = new JTextField(device.warrantyExpiry() != null ? 
                device.warrantyExpiry().format(DATE_FORMATTER) : "", 10);
            
            JTextField assetTagField = new JTextField(device.assetTag(), 20);
            JTextField locationField = new JTextField(device.location(), 20);
            JTextField departmentField = new JTextField(device.department(), 20);
            JTextField categoryField = new JTextField(device.category(), 20);
            
            JTextField lastInspectionField = new JTextField(device.lastInspectionDate() != null ? 
                device.lastInspectionDate().format(DATE_FORMATTER) : "", 10);
            JTextField nextInspectionField = new JTextField(device.nextInspectionDate() != null ? 
                device.nextInspectionDate().format(DATE_FORMATTER) : "", 10);
            JTextField endOfLifeField = new JTextField(device.endOfLifeDate() != null ? 
                device.endOfLifeDate().format(DATE_FORMATTER) : "", 10);
            JComboBox<String> conditionCombo = new JComboBox<>(new String[]{"New", "Good", "Fair", "Poor"});
            conditionCombo.setSelectedItem(device.deviceCondition());
            JTextArea notesArea = new JTextArea(device.notes(), 4, 30);

            // Add date format tooltips
            purchaseDateField.setToolTipText("Format: YYYY-MM-DD");
            warrantyExpiryField.setToolTipText("Format: YYYY-MM-DD");
            lastInspectionField.setToolTipText("Format: YYYY-MM-DD");
            nextInspectionField.setToolTipText("Format: YYYY-MM-DD");
            endOfLifeField.setToolTipText("Format: YYYY-MM-DD");

            // Create tabbed pane
            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.addTab("Basic Info", createBasicInfoPanel(nameField, typeField, serialField, statusCombo, modelField, manufacturerField));
            tabbedPane.addTab("Purchase", createPurchaseInfoPanel(purchaseDateField, purchasePriceField, supplierField, warrantyExpiryField));
            tabbedPane.addTab("Asset", createAssetInfoPanel(assetTagField, locationField, departmentField, categoryField));
            tabbedPane.addTab("Lifecycle", createLifecyclePanel(lastInspectionField, nextInspectionField, endOfLifeField, conditionCombo, notesArea));

            int option = JOptionPane.showConfirmDialog(this, tabbedPane, "Edit Device", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (option == JOptionPane.OK_OPTION) {
                try {
                    // Validate required fields
                    String name = nameField.getText().trim();
                    String type = typeField.getText().trim();
                    String serial = serialField.getText().trim();
                    
                    if (name.isEmpty() || type.isEmpty() || serial.isEmpty()) {
                        JOptionPane.showMessageDialog(this, 
                            "Name, Type, and Serial Number are required", 
                            "Validation Error", 
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Parse dates
                    LocalDate purchaseDate = purchaseDateField.getText().trim().isEmpty() ? null :
                        LocalDate.parse(purchaseDateField.getText().trim(), DATE_FORMATTER);
                    LocalDate warrantyExpiry = warrantyExpiryField.getText().trim().isEmpty() ? null :
                        LocalDate.parse(warrantyExpiryField.getText().trim(), DATE_FORMATTER);
                    LocalDate lastInspection = lastInspectionField.getText().trim().isEmpty() ? null :
                        LocalDate.parse(lastInspectionField.getText().trim(), DATE_FORMATTER);
                    LocalDate nextInspection = nextInspectionField.getText().trim().isEmpty() ? null :
                        LocalDate.parse(nextInspectionField.getText().trim(), DATE_FORMATTER);
                    LocalDate endOfLife = endOfLifeField.getText().trim().isEmpty() ? null :
                        LocalDate.parse(endOfLifeField.getText().trim(), DATE_FORMATTER);

                    // Parse purchase price
                    BigDecimal purchasePrice = purchasePriceField.getText().trim().isEmpty() ? null :
                        new BigDecimal(purchasePriceField.getText().trim());

                    DeviceInfoRecord updatedDevice = new DeviceInfoRecord(
                        device.id(),
                        name,
                        type,
                        serial,
                        statusCombo.getSelectedItem().toString(),
                        purchaseDate,
                        purchasePrice,
                        supplierField.getText().trim(),
                        warrantyExpiry,
                        modelField.getText().trim(),
                        manufacturerField.getText().trim(),
                        device.specifications(), // keep existing specs
                        device.firmwareVersion(), // keep existing firmware
                        assetTagField.getText().trim(),
                        locationField.getText().trim(),
                        departmentField.getText().trim(),
                        categoryField.getText().trim(),
                        lastInspection,
                        nextInspection,
                        endOfLife,
                        conditionCombo.getSelectedItem().toString(),
                        notesArea.getText().trim(),
                        device.createdAt(), // keep original creation time
                        null, // updated_at will be set by database
                        device.createdBy(), // keep original creator
                        "system" // last_modified_by
                    );

                    adapter.updateDevice(updatedDevice);
                    refreshDeviceTable();
                } catch (Exception e) {
                    String msg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
                    if (msg.contains("duplicate") && msg.contains("serial_number")) {
                        JOptionPane.showMessageDialog(this, 
                            "Cannot update due to duplicate serial number", 
                            "Update Not Allowed", 
                            JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, 
                            "An unexpected error occurred. Please try again.", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    private void deleteSelectedDevice() {
        int selectedRow = deviceTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a device to delete", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer deviceId = (Integer) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this device?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                adapter.deleteDevice(deviceId);
                refreshDeviceTable();
            } catch (RuntimeException ex) {
                // Show user-friendly error if it's a constraint violation
                JOptionPane.showMessageDialog(this,
                    "Cannot delete due to device being used",
                    "Delete Not Allowed",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshDeviceTable() {
        tableModel.setRowCount(0);
        for (DeviceInfoRecord device : adapter.getAllDevices()) {
            Object[] row = {
                device.id(),
                device.name(),
                device.type(),
                device.serialNumber(),
                device.status(),
                device.model(),
                device.manufacturer(),
                device.location(),
                device.department(),
                device.deviceCondition(),
                device.assetTag()
            };
            tableModel.addRow(row);
        }
        autoRefreshLabel.setText("Auto-refreshed at " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    @Override
    public void refreshData() {
        refreshDeviceTable();
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