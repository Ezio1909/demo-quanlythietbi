package quanlythietbi.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import quanlythietbi.entity.DeviceAssignmentRecord;
import quanlythietbi.entity.EmployeeInfoRecord;
import quanlythietbi.service.adapter.AssignmentManagementAdapter;
import quanlythietbi.service.adapter.DeviceManagementAdapter;
import quanlythietbi.ui.components.SortableTable;
import quanlythietbi.ui.dialogs.AssignDeviceDialog;
import quanlythietbi.ui.dialogs.EmployeeDevicesDialog;

public class AssignmentPanel extends JPanel implements RefreshablePanel {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter REFRESH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final AssignmentManagementAdapter assignmentAdapter;
    private final DeviceManagementAdapter deviceAdapter;
    private SortableTable assignmentTable;
    private DefaultTableModel tableModel;
    private javax.swing.Timer autoRefreshTimer;
    private boolean autoRefreshEnabled = false;
    private JLabel autoRefreshLabel;

    public AssignmentPanel(AssignmentManagementAdapter assignmentAdapter, DeviceManagementAdapter deviceAdapter) {
        this.assignmentAdapter = assignmentAdapter;
        this.deviceAdapter = deviceAdapter;
        setLayout(new BorderLayout(10, 10));
        // Top panel with buttons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        JButton assignButton = new JButton("Assign Device");
        JButton returnButton = new JButton("Return Device");
        assignButton.addActionListener(e -> showAssignDeviceDialog());
        returnButton.addActionListener(e -> returnSelectedDevice());
        topPanel.add(assignButton);
        topPanel.add(returnButton);
        // Table
        String[] columns = {
            "ID", "Device", "Employee", "Department", "Assigned Date", "Expiration Date", "Returned Date", "Status"
        };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        assignmentTable = new SortableTable(tableModel);
        TableColumn employeeColumn = assignmentTable.getColumnModel().getColumn(2);
        employeeColumn.setCellRenderer(new DefaultTableCellRenderer() {{ setToolTipText("Click to view all devices assigned to this employee"); }});
        assignmentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = assignmentTable.rowAtPoint(e.getPoint());
                int col = assignmentTable.columnAtPoint(e.getPoint());
                if (row >= 0 && col == 2) {
                    String employeeName = (String) tableModel.getValueAt(row, col);
                    String department = (String) tableModel.getValueAt(row, 3);
                    List<EmployeeInfoRecord> employees = assignmentAdapter.getAllEmployees();
                    employees.stream().filter(emp -> emp.name().equals(employeeName) && emp.department().equals(department)).findFirst().ifPresent(employee -> showEmployeeDevicesDialog(employee));
                }
            }
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                int col = assignmentTable.columnAtPoint(e.getPoint());
                if (col == 2) assignmentTable.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) { assignmentTable.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR)); }
        });
        JScrollPane scrollPane = new JScrollPane(assignmentTable);
        // Center panel with padding (like Device tab)
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
        // Add auto-refresh label at bottom right
        autoRefreshLabel = new JLabel();
        autoRefreshLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 20));
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(autoRefreshLabel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
        refreshAssignmentTable();
        // Add component listener for tab switch
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                refreshAssignmentTable();
                setAutoRefreshEnabled(true);
            }
            @Override
            public void componentHidden(java.awt.event.ComponentEvent e) {
                setAutoRefreshEnabled(false);
            }
        });
        // Setup auto-refresh timer
        autoRefreshTimer = new javax.swing.Timer(1_000, e -> refreshAssignmentTable());
        autoRefreshTimer.setRepeats(true);
    }

    private void showAssignDeviceDialog() {
        AssignDeviceDialog dialog = new AssignDeviceDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            assignmentAdapter,
            deviceAdapter
        );
        dialog.setVisible(true);
        refreshAssignmentTable();
    }

    private void showEmployeeDevicesDialog(EmployeeInfoRecord employee) {
        EmployeeDevicesDialog dialog = new EmployeeDevicesDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            assignmentAdapter,
            employee
        );
        dialog.setVisible(true);
    }

    private void returnSelectedDevice() {
        int selectedRow = assignmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select an assignment to return",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = assignmentTable.convertRowIndexToModel(selectedRow);
        Integer assignmentId = (Integer) tableModel.getValueAt(modelRow, 0);
        String status = (String) tableModel.getValueAt(modelRow, 7);

        if ("Returned".equals(status)) {
            JOptionPane.showMessageDialog(this,
                "This device has already been returned",
                "Already Returned",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to return this device?",
            "Confirm Return",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                assignmentAdapter.returnDevice(assignmentId);
                refreshAssignmentTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "An unexpected error occurred. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshAssignmentTable() {
        tableModel.setRowCount(0);
        List<DeviceAssignmentRecord> assignments = assignmentAdapter.getAllAssignments();
        
        for (DeviceAssignmentRecord assignment : assignments) {
            tableModel.addRow(new Object[]{
                assignment.id(),
                assignment.deviceName(),
                assignment.employeeName(),
                assignment.department(),
                assignment.assignedAt().format(DATE_FORMATTER),
                assignment.expirationDate().format(DATE_FORMATTER),
                assignment.returnedAt() != null ? 
                    assignment.returnedAt().format(DATE_FORMATTER) : "",
                assignment.status()
            });
        }
        updateAutoRefreshLabel();
    }

    private void updateAutoRefreshLabel() {
        autoRefreshLabel.setText("Auto-refreshed at " + LocalDateTime.now().format(REFRESH_FORMATTER));
    }

    @Override
    public void refreshData() {
        refreshAssignmentTable();
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
} 