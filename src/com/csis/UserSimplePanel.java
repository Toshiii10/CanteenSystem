package com.csis;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.Vector;

public class UserSimplePanel extends JPanel {
    private final String query;
    private final Object[] queryParams;
    private final JTable table = new JTable();
    private final JLabel lblTitle = new JLabel();
    private final Timer autoRefreshTimer;

    // Constructor Variant A: Standard global view lookup queries
    public UserSimplePanel(String titleText, String sqlQuery) {
        this(titleText, sqlQuery, new Object[0]);
    }

    // Constructor Variant B: Parameterized context queries (e.g., logging in specific user IDs)
    public UserSimplePanel(String titleText, String sqlQuery, Object... params) {
        super(new BorderLayout(15, 15));
        this.query = sqlQuery;
        this.queryParams = params;

        // Clean spacing bounds around the panel sheet canvas
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // MATCH: Soft off-white/gray background panel backdrop color
        setBackground(new Color(242, 242, 242)); 

        // 1. Build a Polished Sub-Header Ribbon Area
        lblTitle.setText(titleText);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 24)); 
        lblTitle.setForeground(new Color(27, 54, 93)); 
        
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setFont(new Font("Tahoma", Font.BOLD, 13)); 
        btnRefresh.setFocusPainted(false);
        btnRefresh.setBackground(Color.WHITE);
        btnRefresh.addActionListener(e -> refreshTableData());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(btnRefresh, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // 2. Configure a Sharp, Polished Grid View Table Layout
        table.setFont(new Font("Tahoma", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.setGridColor(new Color(255, 255, 255)); 
        table.setShowGrid(true);
        
        table.setBackground(Color.WHITE);
        table.setForeground(new Color(33, 37, 41));
        
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(255, 255, 255)); 
        table.getTableHeader().setForeground(new Color(33, 37, 41));    
        table.getTableHeader().setReorderingAllowed(false);
        
        // THE FIX: Switched to a Mouse Double-Click Listener! Much more reliable than Selection Listener.
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Requires a double-click so they don't accidentally open it
                if (e.getClickCount() == 2 && table.getSelectedRow() >= 0) {
                    showFullMessage();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(222, 226, 230), 1));
        scrollPane.getViewport().setBackground(Color.WHITE); 
        add(scrollPane, BorderLayout.CENTER);

        // Execute initial data render pass
        refreshTableData();

        autoRefreshTimer = new Timer(15_000, e -> refreshTableData());
        autoRefreshTimer.start();

        addAncestorListener(new AncestorListener() {
            @Override public void ancestorRemoved(AncestorEvent event) { autoRefreshTimer.stop(); }
            @Override public void ancestorAdded(AncestorEvent event) { }
            @Override public void ancestorMoved(AncestorEvent event) { }
        });
    }

    // Now looks at the Panel Title, not the database columns!
    private void showFullMessage() {
        int row = table.getSelectedRow();
        if (row < 0 || table.getColumnCount() < 2) return;

        try {
            String title = "Details";
            String content = "";
            String panelName = lblTitle.getText(); 

            // Check if it's the Announcements tab
                if (panelName.contains("Announcements")) {
                // THE FIX: Concatenate the two values for that "Welcome! - THIS TITLE" format
                String msgTitle = String.valueOf(table.getValueAt(row, 0)); // The Title
                title = msgTitle; // The new formatted header
                
                // Keep the search logic for the body column
                int targetCol = 2; 
                for (int i = 0; i < table.getColumnCount(); i++) {
                    String colName = table.getColumnName(i).toUpperCase();
                    if (colName.contains("MESSAGE") || colName.contains("CONTENT") || colName.contains("BODY")) {
                        targetCol = i;
                        break;
                    }
                }
                content = String.valueOf(table.getValueAt(row, targetCol));
            }

            // Check if it's the Notifications tab
else if (panelName.contains("Alerts") || panelName.contains("Notifications")) {
                int idCol = -1; // Start at -1 to strictly check if we find it
                int titleCol = -1;
                int bodyCol = 1; // Fallback
                
                // 1. Scan the table headers for the exact columns
                for (int i = 0; i < table.getColumnCount(); i++) {
                    String colName = table.getColumnName(i).toUpperCase();
                    if (colName.equals("ID") || colName.contains("NOTIFICATION ID") || colName.contains("NOTIFICATION_ID")) idCol = i;
                    else if (colName.contains("TITLE")) titleCol = i;
                    else if (colName.contains("BODY") || colName.contains("MESSAGE") || colName.contains("NOTIFICATION")) bodyCol = i;
                }
                
                // 2. Extract the reading text
                content = String.valueOf(table.getValueAt(row, bodyCol)); 
                
                if (titleCol != -1) {
                    title = "Alert - " + table.getValueAt(row, titleCol);
                } else {
                    title = "Alert Notification";
                }

                // 3. MARK AS READ (Only if we successfully found the ID column!)
                if (idCol != -1) {
                    String notifId = String.valueOf(table.getValueAt(row, idCol));
                    try (Connection c = DB.getConnection();
                         PreparedStatement ps = c.prepareStatement("UPDATE notifications SET status = 'READ' WHERE notification_id = ?")) {
                        ps.setString(1, notifId);
                        int updated = ps.executeUpdate(); // Tells us how many rows were actually changed
                        
                        if (updated > 0) {
                            refreshTableData();
                        } else {
                            System.err.println("Error: No database row matches Notification ID: " + notifId);
                        }
                    } catch (SQLException ex) {
                        System.err.println("Failed to mark notification as read: " + ex.getMessage());
                    }
                } else {
                    System.err.println("WARNING: Could not update status. Your table is missing the 'ID' column!");
                }
            }

            JTextArea textArea = new JTextArea(content);
            textArea.setFont(new Font("Tahoma", Font.PLAIN, 14));
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setEditable(false);
            textArea.setBackground(UIManager.getColor("Label.background"));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 200));
            scrollPane.setBorder(BorderFactory.createEmptyBorder());

            SwingUtilities.invokeLater(() -> table.clearSelection());

            JOptionPane.showMessageDialog(this, scrollPane, title, JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            System.err.println("Error showing popup: " + ex.getMessage());
        }
    }

    public void refreshTableData() {
        try (Connection c = DB.getConnection(); 
             PreparedStatement ps = c.prepareStatement(query)) {
            
            for (int i = 0; i < queryParams.length; i++) {
                ps.setObject(i + 1, queryParams[i]);
            }

            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int columnCount = meta.getColumnCount();

                Vector<String> columnNames = new Vector<>();
                for (int col = 1; col <= columnCount; col++) {
                    String name = meta.getColumnLabel(col).replace("_", " ").toUpperCase();
                    columnNames.add(name);
                }

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
                    public boolean isCellEditable(int r, int c) { return false; } 
                });
                
                table.setBackground(Color.WHITE);
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch dashboard view rows: " + e.getMessage());
            UIUtils.error(this, e);
        }
    }
}