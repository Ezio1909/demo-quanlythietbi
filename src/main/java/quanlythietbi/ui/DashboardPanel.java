package quanlythietbi.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class DashboardPanel extends JPanel {
    private static final Color CARD_BG = new Color(255, 255, 255);
    private static final Color BORDER_COLOR = new Color(200, 200, 200);
    
    public DashboardPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));
        
        // Add title
        JLabel titleLabel = new JLabel("Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Create stats panel
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setBackground(new Color(240, 240, 240));
        statsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Add stat cards
        statsPanel.add(createStatCard("Total Devices", "120", "📱"));
        statsPanel.add(createStatCard("Available Devices", "85", "✅"));
        statsPanel.add(createStatCard("In Maintenance", "15", "🔧"));
        statsPanel.add(createStatCard("Assigned Devices", "20", "👥"));
        
        // Add stats panel to center
        add(statsPanel, BorderLayout.CENTER);
        
        // Add quick actions panel
        JPanel actionsPanel = new JPanel();
        actionsPanel.setBackground(new Color(240, 240, 240));
        actionsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        actionsPanel.add(createActionButton("Add Device", "➕"));
        actionsPanel.add(createActionButton("Generate Report", "📊"));
        actionsPanel.add(createActionButton("Schedule Maintenance", "🔧"));
        
        add(actionsPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createStatCard(String title, String value, String icon) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(CARD_BG);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        card.add(iconLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(valueLabel);
        
        return card;
    }
    
    private JButton createActionButton(String text, String icon) {
        JButton button = new JButton(icon + " " + text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setPreferredSize(new Dimension(200, 40));
        button.setFocusPainted(false);
        button.setBackground(CARD_BG);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        return button;
    }
} 