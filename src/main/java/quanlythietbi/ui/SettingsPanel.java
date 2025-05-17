package quanlythietbi.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class SettingsPanel extends JPanel {
    
    public SettingsPanel() {
        setLayout(new BorderLayout());
        
        // Add title
        JLabel titleLabel = new JLabel("Settings", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Create settings content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Add settings sections
        contentPanel.add(createSection("General Settings", new String[][]{
            {"Language", "English"},
            {"Theme", "Light"},
            {"Notifications", "Enabled"}
        }));
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        contentPanel.add(createSection("Database Settings", new String[][]{
            {"Database Type", "H2"},
            {"Auto Backup", "Daily"},
            {"Backup Location", "/backup"}
        }));
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        contentPanel.add(createSection("Report Settings", new String[][]{
            {"Default Format", "PDF"},
            {"Include Logo", "Yes"},
            {"Auto-generate Reports", "Weekly"}
        }));
        
        // Add content to scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
        
        // Add bottom button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton saveButton = new JButton("Save Changes");
        saveButton.setBackground(new Color(51, 122, 183));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        
        JButton resetButton = new JButton("Reset to Default");
        
        buttonPanel.add(resetButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(saveButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createSection(String title, String[][] settings) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(title),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        for (String[] setting : settings) {
            JPanel row = new JPanel(new BorderLayout());
            row.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            
            JLabel label = new JLabel(setting[0]);
            label.setFont(new Font("Arial", Font.PLAIN, 14));
            
            JComboBox<String> value = new JComboBox<>(new String[]{setting[1]});
            value.setPreferredSize(new Dimension(200, 25));
            
            row.add(label, BorderLayout.WEST);
            row.add(value, BorderLayout.EAST);
            
            section.add(row);
        }
        
        return section;
    }
} 