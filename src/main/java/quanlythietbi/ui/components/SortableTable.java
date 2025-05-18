package quanlythietbi.ui.components;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class SortableTable extends JTable {
    private final Map<String, RowFilter<TableModel, Integer>> columnFilters;
    private final TableRowSorter<TableModel> sorter;

    public SortableTable(DefaultTableModel model) {
        super(model);
        this.columnFilters = new HashMap<>();
        this.sorter = new TableRowSorter<>(model);
        setRowSorter(sorter);
        setupHeaderClickHandling();
    }

    private void setupHeaderClickHandling() {
        JTableHeader header = getTableHeader();
        header.setReorderingAllowed(false);
        header.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = columnAtPoint(e.getPoint());
                if (column >= 0) {
                    sort(column, e.isShiftDown());
                }
            }
        });
    }

    public void sort(int column, boolean isDescending) {
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        int direction = isDescending ? SortOrder.DESCENDING.ordinal() : SortOrder.ASCENDING.ordinal();
        sortKeys.add(new RowSorter.SortKey(column, SortOrder.values()[direction]));
        sorter.setSortKeys(sortKeys);
    }

    public void addFilter(String columnName, String value) {
        if (value == null || value.trim().isEmpty()) {
            columnFilters.remove(columnName);
        } else {
            columnFilters.put(columnName, RowFilter.regexFilter("(?i)" + value, getColumnByName(columnName)));
        }
        applyFilters();
    }

    private int getColumnByName(String columnName) {
        for (int i = 0; i < getColumnCount(); i++) {
            if (getColumnName(i).equals(columnName)) {
                return i;
            }
        }
        return -1;
    }

    private void applyFilters() {
        if (columnFilters.isEmpty()) {
            sorter.setRowFilter(null);
            return;
        }

        RowFilter<TableModel, Integer> combinedFilter = RowFilter.andFilter(new ArrayList<>(columnFilters.values()));
        sorter.setRowFilter(combinedFilter);
    }

    public void clearFilters() {
        columnFilters.clear();
        sorter.setRowFilter(null);
    }
} 