package quanlythietbi.ui.components;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class DeviceFilterPanel extends JPanel {
    private final JTextField nameField;
    private final JComboBox<String> statusCombo;
    private final JComboBox<String> conditionCombo;
    private final JComboBox<String> departmentCombo;
    private final JComboBox<String> locationCombo;
    private final JButton clearButton;

    public DeviceFilterPanel(
            String[] statuses,
            String[] deviceConditions,
            String[] departments,
            String[] locations,
            Consumer<String> onNameFilter,
            Consumer<String> onStatusFilter,
            Consumer<String> onDeviceConditionFilter,
            Consumer<String> onDepartmentFilter,
            Consumer<String> onLocationFilter) {
        
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Filters"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name filter
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Name:"), gbc);
        
        nameField = new JTextField(15);
        nameField.getDocument().addDocumentListener(new SimpleDocumentListener(() -> 
            onNameFilter.accept(nameField.getText())
        ));
        gbc.gridx = 1;
        add(nameField, gbc);

        // Status filter
        gbc.gridx = 2;
        add(new JLabel("Status:"), gbc);
        
        statusCombo = new JComboBox<>(addEmptyOption(statuses));
        statusCombo.addActionListener(e -> {
            String selected = (String) statusCombo.getSelectedItem();
            onStatusFilter.accept("".equals(selected) ? null : selected);
        });
        gbc.gridx = 3;
        add(statusCombo, gbc);

        // Condition filter
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Condition:"), gbc);
        
        conditionCombo = new JComboBox<>(addEmptyOption(deviceConditions));
        conditionCombo.addActionListener(e -> {
            String selected = (String) conditionCombo.getSelectedItem();
            onDeviceConditionFilter.accept("".equals(selected) ? null : selected);
        });
        gbc.gridx = 1;
        add(conditionCombo, gbc);

        // Department filter
        gbc.gridx = 2;
        add(new JLabel("Department:"), gbc);
        
        departmentCombo = new JComboBox<>(addEmptyOption(departments));
        departmentCombo.addActionListener(e -> {
            String selected = (String) departmentCombo.getSelectedItem();
            onDepartmentFilter.accept("".equals(selected) ? null : selected);
        });
        gbc.gridx = 3;
        add(departmentCombo, gbc);

        // Location filter
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Location:"), gbc);
        
        locationCombo = new JComboBox<>(addEmptyOption(locations));
        locationCombo.addActionListener(e -> {
            String selected = (String) locationCombo.getSelectedItem();
            onLocationFilter.accept("".equals(selected) ? null : selected);
        });
        gbc.gridx = 1;
        add(locationCombo, gbc);

        // Clear button
        clearButton = new JButton("Clear Filters");
        clearButton.addActionListener(e -> clearFilters());
        gbc.gridx = 3;
        gbc.anchor = GridBagConstraints.EAST;
        add(clearButton, gbc);
    }

    private String[] addEmptyOption(String[] options) {
        String[] withEmpty = new String[options.length + 1];
        withEmpty[0] = "";
        System.arraycopy(options, 0, withEmpty, 1, options.length);
        return withEmpty;
    }

    public void clearFilters() {
        nameField.setText("");
        statusCombo.setSelectedIndex(0);
        conditionCombo.setSelectedIndex(0);
        departmentCombo.setSelectedIndex(0);
        locationCombo.setSelectedIndex(0);
    }
} 