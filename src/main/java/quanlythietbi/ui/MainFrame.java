package quanlythietbi.ui;

import javax.swing.*;
import java.awt.*;

import quanlythietbi.service.adapter.AssignmentManagementAdapter;
import quanlythietbi.service.adapter.DeviceManagementAdapter;
import quanlythietbi.service.adapter.MaintenanceManagementAdapter;
import quanlythietbi.service.adapter.DashboardMetricsAdapter;
import quanlythietbi.service.deviceinfo.DeviceInfoDAO;
import quanlythietbi.service.maintenance.MaintenanceDAO;

public class MainFrame extends JFrame {
    private final DeviceManagementAdapter deviceAdapter;
    private final AssignmentManagementAdapter assignmentAdapter;
    private final MaintenanceManagementAdapter maintenanceAdapter;
    private final DashboardMetricsAdapter dashboardAdapter;
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private DeviceManagementPanel devicePanel;
    private AssignmentPanel assignmentPanel;
    private MaintenancePanel maintenancePanel;
    private DashboardPanel dashboardPanel;
    private ReportsPanel reportsPanel;
    private SettingsPanel settingsPanel;

    public MainFrame(
        DeviceManagementAdapter deviceAdapter, 
        AssignmentManagementAdapter assignmentAdapter,
        MaintenanceManagementAdapter maintenanceAdapter
    ) {
        this.deviceAdapter = deviceAdapter;
        this.assignmentAdapter = assignmentAdapter;
        this.maintenanceAdapter = maintenanceAdapter;
        this.dashboardAdapter = new DashboardMetricsAdapter(
            deviceAdapter.getDeviceDAO(),
            maintenanceAdapter.getMaintenanceDAO()
        );
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
        
        // Create and add content panels
        devicePanel = new DeviceManagementPanel(deviceAdapter);
        assignmentPanel = new AssignmentPanel(assignmentAdapter, deviceAdapter);
        maintenancePanel = new MaintenancePanel(maintenanceAdapter, deviceAdapter);
        dashboardPanel = new DashboardPanel(dashboardAdapter);
        reportsPanel = new ReportsPanel();
        settingsPanel = new SettingsPanel();

        contentPanel.add(devicePanel, "devices");
        contentPanel.add(assignmentPanel, "assignments");
        contentPanel.add(maintenancePanel, "maintenance");
        contentPanel.add(dashboardPanel, "dashboard");
        contentPanel.add(reportsPanel, "reports");
        contentPanel.add(settingsPanel, "settings");

        // Add sidebar with tab switch callback
        SidebarPanel sidebar = new SidebarPanel(contentPanel, cardLayout) {
            @Override
            public void onTabSwitched(String cardName) {
                setPanelAutoRefresh(cardName);
            }
        };

        // Add components to main container
        mainContainer.add(sidebar, BorderLayout.WEST);
        mainContainer.add(contentPanel, BorderLayout.CENTER);

        // Add main container to frame
        add(mainContainer);

        // Set minimum size and pack
        setMinimumSize(new Dimension(1000, 600));
        pack();
        setLocationRelativeTo(null);

        // Enable auto-refresh for the default panel
        setPanelAutoRefresh("devices");
    }

    private void setPanelAutoRefresh(String cardName) {
        devicePanel.setAutoRefreshEnabled("devices".equals(cardName));
        assignmentPanel.setAutoRefreshEnabled("assignments".equals(cardName));
        maintenancePanel.setAutoRefreshEnabled("maintenance".equals(cardName));
        dashboardPanel.setAutoRefreshEnabled("dashboard".equals(cardName));
        reportsPanel.setAutoRefreshEnabled("reports".equals(cardName));
        settingsPanel.setAutoRefreshEnabled("settings".equals(cardName));
    }
} 