package com.csis;

public class CrudConfig { 
    public final String title;
    public final String table;
    public final String idColumn;
    public final String selectSql;
    public final String searchColumn;
    public final String[] fields; 

    public CrudConfig(String title, String table, String id, String[] fields, String select, String search) {
        this.title = title;
        this.table = table;
        this.idColumn = id;
        this.fields = fields.clone(); // Defensive copy prevents external mutation vulnerabilities
        this.selectSql = select;
        this.searchColumn = search;
    } 
}