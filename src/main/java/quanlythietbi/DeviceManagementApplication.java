package quanlythietbi;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quanlythietbi.connector.IConnectionManager;
import quanlythietbi.connector.PooledConnectionManagerImpl;
import quanlythietbi.enums.DBType;
import quanlythietbi.service.adapter.AssignmentManagementAdapter;
import quanlythietbi.service.adapter.DeviceManagementAdapter;
import quanlythietbi.service.adapter.MaintenanceManagementAdapter;
import quanlythietbi.service.assignment.DeviceAssignmentDAO;
import quanlythietbi.service.assignment.DeviceAssignmentDAOImpl;
import quanlythietbi.service.deviceinfo.DeviceInfoDAO;
import quanlythietbi.service.deviceinfo.DeviceInfoDAOImpl;
import quanlythietbi.service.employeeinfo.EmployeeInfoDAO;
import quanlythietbi.service.employeeinfo.EmployeeInfoDAOImpl;
import quanlythietbi.service.maintenance.MaintenanceDAO;
import quanlythietbi.service.maintenance.MaintenanceDAOImpl;
import quanlythietbi.ui.MainFrame;

public class DeviceManagementApplication {
    private static final Logger logger = LoggerFactory.getLogger(DeviceManagementApplication.class);

    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            logger.error("Failed to set system look and feel", e);
        }

        SwingUtilities.invokeLater(() -> {
            try {
                // Set up database connection
                IConnectionManager connectionManager = new PooledConnectionManagerImpl(DBType.MYSQL, 16);
                
                // Set up DAOs
                DeviceInfoDAO deviceDAO = new DeviceInfoDAOImpl(connectionManager);
                DeviceAssignmentDAO assignmentDAO = new DeviceAssignmentDAOImpl(connectionManager);
                MaintenanceDAO maintenanceDAO = new MaintenanceDAOImpl(connectionManager);
                EmployeeInfoDAO employeeDAO = new EmployeeInfoDAOImpl(connectionManager);
                
                // Set up adapters
                DeviceManagementAdapter deviceAdapter = new DeviceManagementAdapter(deviceDAO);
                AssignmentManagementAdapter assignmentAdapter = new AssignmentManagementAdapter(
                    assignmentDAO,
                    employeeDAO,
                    deviceDAO
                );
                MaintenanceManagementAdapter maintenanceAdapter = new MaintenanceManagementAdapter(maintenanceDAO);
                
                // Create and show main frame
                MainFrame frame = new MainFrame(deviceAdapter, assignmentAdapter, maintenanceAdapter);
                frame.setVisible(true);
            } catch (Exception e) {
                logger.error("Failed to start application", e);
                JOptionPane.showMessageDialog(null,
                    "Error starting application: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
} 