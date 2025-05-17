package quanlythietbi.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import quanlythietbi.service.adapter.AssignmentManagementAdapter;
import quanlythietbi.service.adapter.DeviceManagementAdapter;

public class MainFrame extends JFrame {
    private JPanel contentPanel;
    private JPanel sidebarPanel;
    private CardLayout cardLayout;
    private final DeviceManagementAdapter deviceAdapter;
    private final AssignmentManagementAdapter assignmentAdapter;

    private static final String DASHBOARD = "Dashboard";
    private static final String DEVICES = "Devices";
    private static final String ASSIGNMENTS = "Assignments";
    private static final String MAINTENANCE = "Maintenance";
    private static final String REPORTS = "Reports";
    private static final String SETTINGS = "Settings";

    public MainFrame(DeviceManagementAdapter deviceAdapter, AssignmentManagementAdapter assignmentAdapter) {
        this.deviceAdapter = deviceAdapter;
        this.assignmentAdapter = assignmentAdapter;
        initializeFrame();
        createSidebar();
        createContentPanel();
        addPanels();
    }

    private void initializeFrame() {
        setTitle("Device Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void createSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(51, 51, 51));
        sidebarPanel.setPreferredSize(new Dimension(200, getHeight()));

        addSidebarButton(DASHBOARD);
        addSidebarButton(DEVICES);
        addSidebarButton(ASSIGNMENTS);
        addSidebarButton(MAINTENANCE);
        addSidebarButton(REPORTS);
        addSidebarButton(SETTINGS);

        add(sidebarPanel, BorderLayout.WEST);
    }

    private void addSidebarButton(String name) {
        JButton button = new JButton(name);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(51, 51, 51));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(e -> switchPanel(name));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(75, 75, 75));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(51, 51, 51));
            }
        });
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebarPanel.add(button);
    }

    private void createContentPanel() {
        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void addPanels() {
        // Add all panels
        contentPanel.add(new DashboardPanel(), DASHBOARD);
        contentPanel.add(new DeviceManagementPanel(deviceAdapter), DEVICES);
        contentPanel.add(new AssignmentPanel(assignmentAdapter), ASSIGNMENTS);
        contentPanel.add(new MaintenancePanel(), MAINTENANCE);
        contentPanel.add(new ReportsPanel(), REPORTS);
        contentPanel.add(new SettingsPanel(), SETTINGS);

        // Show dashboard by default
        cardLayout.show(contentPanel, DASHBOARD);
    }

    private void switchPanel(String name) {
        cardLayout.show(contentPanel, name);
    }
} 