package com.csis;

import javax.swing.*;
import java.awt.*;

public class ReportPanel extends JPanel {
    private final QueryTableModel model = new QueryTableModel();
    private final JTable table = new JTable(model);
    private final JComboBox<String> reportsDropdown = new JComboBox<>(new String[]{
        "Current Inventory", 
        "Low Stock", 
        "Expiring Batches", 
        "Order Summary", 
        "Daily Sales", 
        "Popular Products"
    });

    // Centralized query mapping to prevent index out-of-bounds exceptions
    private final String[] queries = {
        "SELECT * FROM v_current_inventory",
        "SELECT * FROM v_low_stock",
        "SELECT * FROM v_expiring_batches",
        "SELECT * FROM v_order_summary ORDER BY ordered_at DESC",
        "SELECT * FROM v_daily_sales ORDER BY sale_date DESC",
        "SELECT * FROM v_popular_products ORDER BY quantity_sold DESC"
    };

    public ReportPanel() {
        super(new BorderLayout(8, 8));
        
        JPanel headerPanel = new JPanel();
        headerPanel.add(UIUtils.title("Reports Engine"));
        headerPanel.add(reportsDropdown);
        
        JButton btnRun = new JButton("Generate");
        JButton btnExport = new JButton("Export CSV");
        headerPanel.add(btnRun);
        headerPanel.add(btnExport);
        
        add(headerPanel, BorderLayout.NORTH);
        
        table.setAutoCreateRowSorter(true);
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        btnRun.addActionListener(e -> loadReport());
        btnExport.addActionListener(e -> CsvExporter.exportTable(this, table, "canteen_report.csv"));
        
        loadReport();
    }

    private void loadReport() {
        int index = reportsDropdown.getSelectedIndex();
        if (index < 0 || index >= queries.length) return; // Defensive guard

        try {
            model.load(queries[index]);
        } catch (Exception e) {
            UIUtils.error(this, e);
        }
    }
}