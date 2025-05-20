package quanlythietbi.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class SettingsPanel extends JPanel implements RefreshablePanel {
    private static final Color CARD_BG = new Color(255, 255, 255);
    private static final Color BORDER_COLOR = new Color(200, 200, 200);
    private static final Color BUTTON_BG = new Color(51, 122, 183);
    
    private javax.swing.Timer autoRefreshTimer;
    private boolean autoRefreshEnabled = false;
    private JLabel autoRefreshLabel;
    private static final DateTimeFormatter REFRESH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public SettingsPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));
        
        // Add title
        JLabel titleLabel = new JLabel("Settings", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Create contentPanel with border and padding
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createTitledBorder(""));
        contentPanel.setBackground(Color.WHITE);
        
        // Create settings content
        JPanel settingsContent = new JPanel();
        settingsContent.setBackground(new Color(240, 240, 240));
        settingsContent.setLayout(new BoxLayout(settingsContent, BoxLayout.Y_AXIS));
        settingsContent.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Add settings sections
        settingsContent.add(createSection("General Settings", new String[][]{
            {"Language", "English", "🌐"},
            {"Theme", "Light", "🎨"},
            {"Notifications", "Enabled", "🔔"}
        }));
        
        settingsContent.add(Box.createRigidArea(new Dimension(0, 20)));
        
        settingsContent.add(createSection("Database Settings", new String[][]{
            {"Database Type", "H2", "💾"},
            {"Auto Backup", "Daily", "🔄"},
            {"Backup Location", "/backup", "📁"}
        }));
        
        settingsContent.add(Box.createRigidArea(new Dimension(0, 20)));
        
        settingsContent.add(createSection("Report Settings", new String[][]{
            {"Default Format", "PDF", "📄"},
            {"Include Logo", "Yes", "🖼️"},
            {"Auto-generate Reports", "Weekly", "⏱️"}
        }));
        
        // Add content to scroll pane
        JScrollPane scrollPane = new JScrollPane(settingsContent);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new Color(240, 240, 240));
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);
        
        // Add bottom panel for auto-refresh label and buttons
        autoRefreshLabel = new JLabel();
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
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(autoRefreshLabel, BorderLayout.EAST);
        bottomPanel.add(buttonPanel, BorderLayout.WEST);
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Add component listener for tab switch
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                refreshData();
                setAutoRefreshEnabled(true);
            }
            @Override
            public void componentHidden(java.awt.event.ComponentEvent e) {
                setAutoRefreshEnabled(false);
            }
        });
        // Setup auto-refresh timer (no-op for now)
        autoRefreshTimer = new javax.swing.Timer(10_000, e -> refreshData());
        autoRefreshTimer.setRepeats(true);
    }
    
    @Override
    public void refreshData() {
        updateAutoRefreshLabel();
        // No-op for now, as this panel is mostly static
    }
    
    private void updateAutoRefreshLabel() {
        autoRefreshLabel.setText("Auto-refreshed at " + LocalDateTime.now().format(REFRESH_FORMATTER));
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
    
    private JPanel createSection(String title, String[][] settings) {
        JPanel section = new JPanel();
        section.setBackground(CARD_BG);
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBorder(new EmptyBorder(10, 10, 10, 10));
        
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