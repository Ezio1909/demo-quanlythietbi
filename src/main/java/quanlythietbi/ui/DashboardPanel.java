package quanlythietbi.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import quanlythietbi.service.adapter.DashboardMetricsAdapter;

public class DashboardPanel extends JPanel {
    private static final Color CARD_BG = new Color(255, 255, 255);
    private static final Color BORDER_COLOR = new Color(200, 200, 200);
    private static final Color CHART_BG = new Color(240, 240, 240);
    
    private final DashboardMetricsAdapter metricsAdapter;
    private JPanel statsPanel;
    private JPanel chartsPanel;
    
    public DashboardPanel(DashboardMetricsAdapter metricsAdapter) {
        this.metricsAdapter = metricsAdapter;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));
        
        // Add title
        JLabel titleLabel = new JLabel("Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Create main content panel with scroll
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(240, 240, 240));
        
        // Create stats panel
        statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setBackground(new Color(240, 240, 240));
        statsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Create charts panel
        chartsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        chartsPanel.setBackground(new Color(240, 240, 240));
        chartsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        contentPanel.add(statsPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(chartsPanel);
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new Color(240, 240, 240));
        add(scrollPane, BorderLayout.CENTER);
        
        // Add refresh button
        JButton refreshButton = new JButton("Refresh Dashboard");
        refreshButton.addActionListener(e -> refreshDashboard());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(240, 240, 240));
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Initial load
        refreshDashboard();
        
        // Add component listener to refresh when panel becomes visible
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                refreshDashboard();
            }
        });
    }
    
    private void refreshDashboard() {
        // Clear existing panels
        statsPanel.removeAll();
        chartsPanel.removeAll();
        
        // Add stat cards with fresh data
        statsPanel.add(createStatCard("Total Devices", 
            String.valueOf(metricsAdapter.getTotalDevices()), "📱"));
        statsPanel.add(createStatCard("Available Devices", 
            String.valueOf(metricsAdapter.getAvailableDevices()), "✅"));
        statsPanel.add(createStatCard("In Maintenance", 
            String.valueOf(metricsAdapter.getDevicesInMaintenance()), "🔧"));
        statsPanel.add(createStatCard("Assigned Devices", 
            String.valueOf(metricsAdapter.getAssignedDevices()), "👥"));
        
        // Add chart cards with fresh data
        chartsPanel.add(createChartCard("Device Status Distribution", 
            metricsAdapter.getDeviceStatusCounts(), "📊"));
        chartsPanel.add(createChartCard("Devices by Department", 
            metricsAdapter.getDevicesByDepartment(), "🏢"));
        chartsPanel.add(createChartCard("Maintenance by Type", 
            metricsAdapter.getMaintenanceByType(), "🔧"));
        chartsPanel.add(createChartCard("Maintenance Status", 
            metricsAdapter.getMaintenanceByStatus(), "📈"));
        
        // Add maintenance cost card with fresh data
        JPanel costPanel = new JPanel(new BorderLayout());
        costPanel.setBackground(CARD_BG);
        costPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel costTitle = new JLabel("Total Maintenance Cost");
        costTitle.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel costValue = new JLabel("$" + metricsAdapter.getTotalMaintenanceCost());
        costValue.setFont(new Font("Arial", Font.BOLD, 24));
        
        costPanel.add(costTitle, BorderLayout.NORTH);
        costPanel.add(costValue, BorderLayout.CENTER);
        
        chartsPanel.add(costPanel);
        
        // Force immediate repaint
        statsPanel.revalidate();
        statsPanel.repaint();
        chartsPanel.revalidate();
        chartsPanel.repaint();
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
    
    private JPanel createChartCard(String title, Map<String, Long> data, String icon) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(CARD_BG);
        
        // Header with icon and title
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(CARD_BG);
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        header.add(iconLabel);
        header.add(titleLabel);
        
        // Data panel
        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
        dataPanel.setBackground(CARD_BG);
        
        data.forEach((key, value) -> {
            JPanel row = new JPanel(new BorderLayout());
            row.setBackground(CARD_BG);
            JLabel keyLabel = new JLabel(key);
            JLabel valueLabel = new JLabel(String.valueOf(value));
            row.add(keyLabel, BorderLayout.WEST);
            row.add(valueLabel, BorderLayout.EAST);
            dataPanel.add(row);
            dataPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        });
        
        card.add(header, BorderLayout.NORTH);
        card.add(dataPanel, BorderLayout.CENTER);
        
        return card;
    }
} 