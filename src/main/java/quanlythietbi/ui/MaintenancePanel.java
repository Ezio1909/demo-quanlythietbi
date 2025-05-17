package quanlythietbi.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class MaintenancePanel extends JPanel {
    private JTable maintenanceTable;
    private DefaultTableModel tableModel;
    
    public MaintenancePanel() {
        setLayout(new BorderLayout());
        
        // Add title
        JLabel titleLabel = new JLabel("Maintenance Schedule", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Create toolbar
        JPanel toolBar = createToolBar();
        add(toolBar, BorderLayout.NORTH);
        
        // Create table
        createMaintenanceTable();
        JScrollPane scrollPane = new JScrollPane(maintenanceTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Add bottom panel for buttons
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Add some sample data
        addSampleData();
    }
    
    private JPanel createToolBar() {
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton addButton = new JButton("Schedule Maintenance");
        JButton editButton = new JButton("Edit Schedule");
        JButton deleteButton = new JButton("Cancel Maintenance");
        
        toolBar.add(addButton);
        toolBar.add(editButton);
        toolBar.add(deleteButton);
        
        return toolBar;
    }
    
    private void createMaintenanceTable() {
        String[] columns = {
            "ID",
            "Device",
            "Type",
            "Scheduled Date",
            "Status",
            "Technician"
        };
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        maintenanceTable = new JTable(tableModel);
        maintenanceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        maintenanceTable.getTableHeader().setReorderingAllowed(false);
    }
    
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton completeButton = new JButton("Mark as Complete");
        panel.add(completeButton);
        return panel;
    }
    
    private void addSampleData() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();
        
        Object[][] sampleData = {
            {1, "Printer HP-1102", "Regular Maintenance", today.format(formatter), "Scheduled", "John Smith"},
            {2, "Scanner Epson V39", "Repair", today.plusDays(1).format(formatter), "Pending", "Mike Johnson"},
            {3, "Laptop Dell XPS", "Software Update", today.plusDays(2).format(formatter), "Scheduled", "Sarah Wilson"}
        };
        
        for (Object[] row : sampleData) {
            tableModel.addRow(row);
        }
    }
} 