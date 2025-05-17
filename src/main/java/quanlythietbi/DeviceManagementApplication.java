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
import quanlythietbi.service.assignment.DeviceAssignmentDAO;
import quanlythietbi.service.assignment.DeviceAssignmentDAOImpl;
import quanlythietbi.service.deviceinfo.DeviceInfoDAO;
import quanlythietbi.service.deviceinfo.DeviceInfoDAOImpl;
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
                IConnectionManager connectionManager = new PooledConnectionManagerImpl(DBType.H2, 4);
                
                // Set up DAOs and adapters
                DeviceInfoDAO deviceDAO = new DeviceInfoDAOImpl(connectionManager);
                DeviceAssignmentDAO assignmentDAO = new DeviceAssignmentDAOImpl(connectionManager);
                
                DeviceManagementAdapter deviceAdapter = new DeviceManagementAdapter(deviceDAO);
                AssignmentManagementAdapter assignmentAdapter = new AssignmentManagementAdapter(assignmentDAO);
                
                // Create and show main frame
                MainFrame frame = new MainFrame(deviceAdapter, assignmentAdapter);
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