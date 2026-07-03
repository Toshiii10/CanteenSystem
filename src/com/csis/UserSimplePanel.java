package com.csis;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class UserSimplePanel extends JPanel {
    private final String query;
    private final Object[] queryParams;
    private final JTable table = new JTable();
    private final JLabel lblTitle = new JLabel();

    // Constructor Variant A: Standard global view lookup queries
    public UserSimplePanel(String titleText, String sqlQuery) {
        this(titleText, sqlQuery, new Object[0]);
    }

    // Constructor Variant B: Parameterized context queries (e.g., logging in specific user IDs)
    public UserSimplePanel(String titleText, String sqlQuery, Object... params) {
        super(new BorderLayout(15, 15));
        this.query = sqlQuery;
        this.queryParams = params;

        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        // 1. Build a Polished Sub-Header Ribbon Area
        lblTitle.setText(titleText);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTitle.setForeground(new Color(33, 37, 41));
        
        JButton btnRefresh = new JButton("Refresh Data");
        btnRefresh.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.setBackground(new Color(240, 242, 245));
        btnRefresh.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
            BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        btnRefresh.addActionListener(e -> refreshTableData());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(btnRefresh, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // 2. Configure a Sharp, Polished Grid View Table Layout
        table.setFont(new Font("Tahoma", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(233, 236, 239));
        table.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(222, 226, 230), 1));
        add(scrollPane, BorderLayout.CENTER);

        // Execute initial data render pass
        refreshTableData();
    }

    public void refreshTableData() {
        try (Connection c = DB.getConnection(); 
             PreparedStatement ps = c.prepareStatement(query)) {
            
            // Inject dynamic user tracking criteria targets if present
            for (int i = 0; i < queryParams.length; i++) {
                ps.setObject(i + 1, queryParams[i]);
            }

            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int columnCount = meta.getColumnCount();

                // Build modern table header layouts
                Vector<String> columnNames = new Vector<>();
                for (int col = 1; col <= columnCount; col++) {
                    // Convert raw snake_case database column strings to neat Title Words
                    String name = meta.getColumnLabel(col).replace("_", " ").toUpperCase();
                    columnNames.add(name);
                }

                // Parse and map active rows out cleanly
                Vector<Vector<Object>> dataRows = new Vector<>();
                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    for (int col = 1; col <= columnCount; col++) {
                        row.add(rs.getObject(col));
                    }
                    dataRows.add(row);
                }

                table.setModel(new DefaultTableModel(dataRows, columnNames) {
                    @Override
                    public boolean isCellEditable(int r, int c) { return false; } // Read-only dashboard safety rules
                });
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch dashboard view rows: " + e.getMessage());
        }
    }
}