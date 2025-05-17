package quanlythietbi.ui;

import javax.swing.*;
import java.awt.*;

import quanlythietbi.service.adapter.AssignmentManagementAdapter;
import quanlythietbi.service.adapter.DeviceManagementAdapter;
import quanlythietbi.service.adapter.MaintenanceManagementAdapter;

public class MainFrame extends JFrame {
    private final DeviceManagementAdapter deviceAdapter;
    private final AssignmentManagementAdapter assignmentAdapter;
    private final MaintenanceManagementAdapter maintenanceAdapter;
    private CardLayout cardLayout;
    private JPanel contentPanel;

    public MainFrame(
        DeviceManagementAdapter deviceAdapter, 
        AssignmentManagementAdapter assignmentAdapter,
        MaintenanceManagementAdapter maintenanceAdapter
    ) {
        this.deviceAdapter = deviceAdapter;
        this.assignmentAdapter = assignmentAdapter;
        this.maintenanceAdapter = maintenanceAdapter;
        initializeFrame();
    }

    private void initializeFrame() {
        setTitle("Device Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1200, 800));

        // Create main container with BorderLayout
        JPanel mainContainer = new JPanel(new BorderLayout());
        
        // Create card layout and content panel
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        
        // Create sidebar
        SidebarPanel sidebar = new SidebarPanel(contentPanel, cardLayout);
        
        // Create and add content panels
        DeviceManagementPanel devicePanel = new DeviceManagementPanel(deviceAdapter);
        AssignmentPanel assignmentPanel = new AssignmentPanel(assignmentAdapter, deviceAdapter);
        MaintenancePanel maintenancePanel = new MaintenancePanel(maintenanceAdapter, deviceAdapter);
        DashboardPanel dashboardPanel = new DashboardPanel();
        ReportsPanel reportsPanel = new ReportsPanel();
        SettingsPanel settingsPanel = new SettingsPanel();

        contentPanel.add(devicePanel, "devices");
        contentPanel.add(assignmentPanel, "assignments");
        contentPanel.add(maintenancePanel, "maintenance");
        contentPanel.add(dashboardPanel, "dashboard");
        contentPanel.add(reportsPanel, "reports");
        contentPanel.add(settingsPanel, "settings");

        // Add components to main container
        mainContainer.add(sidebar, BorderLayout.WEST);
        mainContainer.add(contentPanel, BorderLayout.CENTER);

        // Add main container to frame
        add(mainContainer);

        // Set minimum size and pack
        setMinimumSize(new Dimension(1000, 600));
        pack();
        setLocationRelativeTo(null);
    }
} 