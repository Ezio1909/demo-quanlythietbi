package quanlythietbi.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class SettingsPanel extends JPanel {
    private static final Color CARD_BG = new Color(255, 255, 255);
    private static final Color BORDER_COLOR = new Color(200, 200, 200);
    private static final Color BUTTON_BG = new Color(51, 122, 183);
    
    public SettingsPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));
        
        // Add title
        JLabel titleLabel = new JLabel("Settings", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Create settings content
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(240, 240, 240));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Add settings sections
        contentPanel.add(createSection("General Settings", new String[][]{
            {"Language", "English", "🌐"},
            {"Theme", "Light", "🎨"},
            {"Notifications", "Enabled", "🔔"}
        }));
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        contentPanel.add(createSection("Database Settings", new String[][]{
            {"Database Type", "H2", "💾"},
            {"Auto Backup", "Daily", "🔄"},
            {"Backup Location", "/backup", "📁"}
        }));
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        contentPanel.add(createSection("Report Settings", new String[][]{
            {"Default Format", "PDF", "📄"},
            {"Include Logo", "Yes", "🖼️"},
            {"Auto-generate Reports", "Weekly", "⏱️"}
        }));
        
        // Add content to scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new Color(240, 240, 240));
        add(scrollPane, BorderLayout.CENTER);
        
        // Add bottom button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(240, 240, 240));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JButton saveButton = new JButton("Save Changes");
        saveButton.setBackground(BUTTON_BG);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorder(new EmptyBorder(8, 20, 8, 20));
        
        JButton resetButton = new JButton("Reset to Default");
        resetButton.setBackground(CARD_BG);
        resetButton.setFocusPainted(false);
        resetButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        
        buttonPanel.add(resetButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(saveButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createSection(String title, String[][] settings) {
        JPanel section = new JPanel();
        section.setBackground(CARD_BG);
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(title),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        for (String[] setting : settings) {
            JPanel row = new JPanel(new BorderLayout(10, 0));
            row.setBackground(CARD_BG);
            row.setBorder(new EmptyBorder(5, 0, 5, 0));
            
            // Icon and label panel
            JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            labelPanel.setBackground(CARD_BG);
            
            JLabel iconLabel = new JLabel(setting[2]);
            iconLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            labelPanel.add(iconLabel);
            
            JLabel label = new JLabel(setting[0]);
            label.setFont(new Font("Arial", Font.PLAIN, 14));
            labelPanel.add(label);
            
            JComboBox<String> value = new JComboBox<>(new String[]{setting[1]});
            value.setPreferredSize(new Dimension(200, 25));
            
            row.add(labelPanel, BorderLayout.WEST);
            row.add(value, BorderLayout.EAST);
            
            section.add(row);
        }
        
        return section;
    }
} 