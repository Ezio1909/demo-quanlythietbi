package quanlythietbi.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class DatabaseConfigDialog extends JDialog {
    private final JTextField hostField;
    private final JTextField portField;
    private final JTextField dbNameField;
    private final JTextField userField;
    private final JPasswordField passwordField;
    private boolean confirmed = false;

    public DatabaseConfigDialog(Frame parent) {
        super(parent, "Database Configuration", true);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JLabel instruction = new JLabel("<html><b>If you are running MySQL using Docker Compose, you can keep the default values below.</b></html>");
        instruction.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        add(instruction, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Labels
        form.add(new JLabel("Host:"), gbc);
        gbc.gridy++;
        form.add(new JLabel("Port:"), gbc);
        gbc.gridy++;
        form.add(new JLabel("Database Name:"), gbc);
        gbc.gridy++;
        form.add(new JLabel("Username:"), gbc);
        gbc.gridy++;
        form.add(new JLabel("Password:"), gbc);

        // Fields
        gbc.gridx = 1;
        gbc.gridy = 0;
        hostField = new JTextField("localhost", 16);
        hostField.setForeground(Color.GRAY);
        form.add(hostField, gbc);
        gbc.gridy++;
        portField = new JTextField("3306", 16);
        portField.setForeground(Color.GRAY);
        form.add(portField, gbc);
        gbc.gridy++;
        dbNameField = new JTextField("devicedb", 16);
        dbNameField.setForeground(Color.GRAY);
        form.add(dbNameField, gbc);
        gbc.gridy++;
        userField = new JTextField("devuser", 16);
        userField.setForeground(Color.GRAY);
        form.add(userField, gbc);
        gbc.gridy++;
        passwordField = new JPasswordField("devpass", 16);
        passwordField.setForeground(Color.GRAY);
        form.add(passwordField, gbc);

        add(form, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmed = true;
                dispose();
            }
        });
        cancelButton.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        pack();
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Map<String, String> getConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("host", hostField.getText().trim());
        config.put("port", portField.getText().trim());
        config.put("database", dbNameField.getText().trim());
        config.put("username", userField.getText().trim());
        config.put("password", new String(passwordField.getPassword()));
        return config;
    }
} 