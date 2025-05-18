package quanlythietbi.ui;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import quanlythietbi.entity.DeviceInfoRecord;
import quanlythietbi.service.adapter.DeviceManagementAdapter;
import quanlythietbi.ui.components.DeviceFilterPanel;
import quanlythietbi.ui.components.SimpleDocumentListener;
import quanlythietbi.ui.components.SortableTable;

public class DeviceManagementPanel extends JPanel {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DeviceManagementAdapter adapter;
    private SortableTable deviceTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    public DeviceManagementPanel(DeviceManagementAdapter adapter) {
        this.adapter = adapter;
        setLayout(new BorderLayout(10, 10));
        initializeComponents();
        refreshDeviceTable();
    }

    private void initializeComponents() {
        // Top panel with search and buttons
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search by Device Name:"));
        searchField = new JTextField(20);
        searchField.getDocument().addDocumentListener(new SimpleDocumentListener(
            () -> deviceTable.addFilter("Name", searchField.getText())
        ));
        searchPanel.add(searchField);
        topPanel.add(searchPanel, BorderLayout.WEST);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Add Device");
        JButton editButton = new JButton("Edit Device");
        JButton deleteButton = new JButton("Delete Device");
        
        addButton.addActionListener(e -> showAddDeviceDialog());
        editButton.addActionListener(e -> showEditDeviceDialog());
        deleteButton.addActionListener(e -> deleteSelectedDevice());
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

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
            if (device.condition() != null) conditions.add(device.condition());
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
        add(centerPanel, BorderLayout.CENTER);
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
                JOptionPane.showMessageDialog(this, 
                    "Error: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
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
            conditionCombo.setSelectedItem(device.condition());
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
                    JOptionPane.showMessageDialog(this, 
                        "Error: " + e.getMessage(), 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
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
            adapter.deleteDevice(deviceId);
            refreshDeviceTable();
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
                device.condition(),
                device.assetTag()
            };
            tableModel.addRow(row);
        }
    }
}