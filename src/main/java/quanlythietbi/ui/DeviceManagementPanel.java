package quanlythietbi.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import quanlythietbi.entity.DeviceInfoRecord;
import quanlythietbi.service.adapter.DeviceManagementAdapter;

public class DeviceManagementPanel extends JPanel {
    private final DeviceManagementAdapter adapter;
    private JTable deviceTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton;
    private JTextField searchField;

    public DeviceManagementPanel(DeviceManagementAdapter adapter) {
        this.adapter = adapter;
        setLayout(new BorderLayout());
        initializeComponents();
    }

    private void initializeComponents() {
        // Create toolbar
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        // Search field
        searchField = new JTextField(20);
        searchField.setToolTipText("Search devices...");
        toolBar.add(new JLabel("Search: "));
        toolBar.add(searchField);

        // Buttons
        addButton = new JButton("Add Device");
        editButton = new JButton("Edit Device");
        deleteButton = new JButton("Delete Device");

        toolBar.add(addButton);
        toolBar.add(editButton);
        toolBar.add(deleteButton);

        // Table
        String[] columns = {
            "ID", 
            "Name", 
            "Type", 
            "Serial Number", 
            "Status"
        };
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        deviceTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(deviceTable);

        // Add components
        add(toolBar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Add listeners
        addButton.addActionListener(e -> showAddDeviceDialog());
        editButton.addActionListener(e -> showEditDeviceDialog());
        deleteButton.addActionListener(e -> deleteSelectedDevice());

        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                // TODO: Implement search
            }
        });

        // Initial data load
        refreshDeviceTable();
    }

    private void showAddDeviceDialog() {
        JTextField nameField = new JTextField();
        JTextField typeField = new JTextField();
        JTextField serialField = new JTextField();

        Object[] message = {
            "Device Name:", nameField,
            "Device Type:", typeField,
            "Serial Number:", serialField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add New Device", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String type = typeField.getText().trim();
            String serial = serialField.getText().trim();

            if (!name.isEmpty() && !type.isEmpty() && !serial.isEmpty()) {
                adapter.addDevice(name, type, serial);
                refreshDeviceTable();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "All fields are required", 
                    "Validation Error", 
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
            JTextField nameField = new JTextField(device.name());
            JTextField typeField = new JTextField(device.type());
            JTextField serialField = new JTextField(device.serialNumber());

            Object[] message = {
                "Device Name:", nameField,
                "Device Type:", typeField,
                "Serial Number:", serialField
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Edit Device", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (option == JOptionPane.OK_OPTION) {
                String name = nameField.getText().trim();
                String type = typeField.getText().trim();
                String serial = serialField.getText().trim();

                if (!name.isEmpty() && !type.isEmpty() && !serial.isEmpty()) {
                    DeviceInfoRecord updatedDevice = new DeviceInfoRecord(
                        device.id(),
                        name,
                        type,
                        serial,
                        device.status()
                    );
                    adapter.updateDevice(updatedDevice);
                    refreshDeviceTable();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "All fields are required", 
                        "Validation Error", 
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
                device.status()
            };
            tableModel.addRow(row);
        }
    }
} 