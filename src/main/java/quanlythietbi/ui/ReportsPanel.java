package quanlythietbi.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class ReportsPanel extends JPanel {
    
    public ReportsPanel() {
        setLayout(new BorderLayout());
        
        // Add title
        JLabel titleLabel = new JLabel("Reports", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Create main content panel
        JPanel contentPanel = new JPanel();
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
        
        // Add content panel to center
        add(new JScrollPane(contentPanel), BorderLayout.CENTER);
        
        // Add export options panel
        JPanel exportPanel = createExportPanel();
        add(exportPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createReportCard(String title, String description, String icon) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(300, 200));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(Color.WHITE);
        
        // Icon and title panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
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
        generateButton.setBackground(new Color(51, 122, 183));
        generateButton.setForeground(Color.WHITE);
        generateButton.setFocusPainted(false);
        
        card.add(headerPanel, BorderLayout.NORTH);
        card.add(descLabel, BorderLayout.CENTER);
        card.add(generateButton, BorderLayout.SOUTH);
        
        return card;
    }
    
    private JPanel createExportPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JComboBox<String> formatCombo = new JComboBox<>(new String[]{"PDF", "Excel", "CSV"});
        JButton exportButton = new JButton("Export");
        
        panel.add(new JLabel("Export Format:"));
        panel.add(formatCombo);
        panel.add(exportButton);
        
        return panel;
    }
} 