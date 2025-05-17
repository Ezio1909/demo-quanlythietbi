package quanlythietbi.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ReportsPanel extends JPanel {
    private static final Color CARD_BG = new Color(255, 255, 255);
    private static final Color BORDER_COLOR = new Color(200, 200, 200);
    private static final Color BUTTON_BG = new Color(51, 122, 183);
    
    public ReportsPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));
        
        // Add title
        JLabel titleLabel = new JLabel("Reports", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Create main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(240, 240, 240));
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Add report options
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(createReportCard(
            "Device Inventory Report",
            "Generate a complete list of all devices with their current status",
            "📊"
        ), gbc);
        
        gbc.gridx = 1;
        contentPanel.add(createReportCard(
            "Maintenance History",
            "View maintenance history for all devices",
            "🔧"
        ), gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(createReportCard(
            "Assignment Report",
            "Track device assignments to employees",
            "👥"
        ), gbc);
        
        gbc.gridx = 1;
        contentPanel.add(createReportCard(
            "Usage Statistics",
            "Analyze device usage patterns and trends",
            "📈"
        ), gbc);
        
        // Add content panel to center with scroll
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new Color(240, 240, 240));
        add(scrollPane, BorderLayout.CENTER);
        
        // Add export options panel
        JPanel exportPanel = createExportPanel();
        add(exportPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createReportCard(String title, String description, String icon) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(300, 200));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(CARD_BG);
        
        // Icon and title panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(CARD_BG);
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        headerPanel.add(iconLabel);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(titleLabel);
        
        // Description
        JLabel descLabel = new JLabel("<html><body style='width: 250px'>" + description + "</html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Generate button
        JButton generateButton = new JButton("Generate Report");
        generateButton.setBackground(BUTTON_BG);
        generateButton.setForeground(Color.WHITE);
        generateButton.setFocusPainted(false);
        generateButton.setBorder(new EmptyBorder(8, 15, 8, 15));
        
        card.add(headerPanel, BorderLayout.NORTH);
        card.add(descLabel, BorderLayout.CENTER);
        card.add(generateButton, BorderLayout.SOUTH);
        
        return card;
    }
    
    private JPanel createExportPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JComboBox<String> formatCombo = new JComboBox<>(new String[]{"PDF", "Excel", "CSV"});
        JButton exportButton = new JButton("Export");
        exportButton.setBackground(BUTTON_BG);
        exportButton.setForeground(Color.WHITE);
        exportButton.setFocusPainted(false);
        exportButton.setBorder(new EmptyBorder(5, 15, 5, 15));
        
        panel.add(new JLabel("Export Format:"));
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(formatCombo);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(exportButton);
        
        return panel;
    }
} 