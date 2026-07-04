package com.csis;

import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.util.*;

public class QueryTableModel extends AbstractTableModel {
    private final java.util.List<String> columns = new ArrayList<>();
    private final java.util.List<Object[]> rows = new ArrayList<>();

    public void load(String sql, Object... params) throws SQLException {
        columns.clear();
        rows.clear();
        
        try (Connection c = DB.getConnection(); 
             PreparedStatement p = c.prepareStatement(sql)) {
             
            for (int i = 0; i < params.length; i++) {
                p.setObject(i + 1, params[i]);
            }
            
            try (ResultSet r = p.executeQuery()) {
                ResultSetMetaData meta = r.getMetaData();
                int colCount = meta.getColumnCount();
                
                for (int i = 1; i <= colCount; i++) {
                    columns.add(meta.getColumnLabel(i));
                }
                
                while (r.next()) {
                    Object[] rowData = new Object[colCount];
                    for (int i = 0; i < colCount; i++) {
                        rowData[i] = r.getObject(i + 1);
                    }
                    rows.add(rowData);
                }
            }
        }
        fireTableStructureChanged();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public String getColumnName(int colIndex) {
        String name = columns.get(colIndex);
        if (name == null) return "";
        // convert snake_case to words
        String display = name.replace('_', ' ');
        // split camelCase (e.g., totalAmount -> total Amount)
        display = display.replaceAll("([a-z])([A-Z])", "$1 $2");
        // Capitalize each word, but keep common acronyms like ID uppercase
        StringBuilder sb = new StringBuilder();
        for (String w : display.split(" ")) {
            if (w.isEmpty()) continue;
            String lower = w.toLowerCase();
            if (lower.equals("id")) {
                sb.append("ID");
            } else {
                sb.append(Character.toUpperCase(lower.charAt(0))).append(lower.substring(1));
            }
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    @Override
    public Object getValueAt(int rowIndex, int colIndex) {
        return rows.get(rowIndex)[colIndex];
    }

    public int findColumnByName(String name) {
        for (int i = 0; i < columns.size(); i++) {
            if (columns.get(i).equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }
}