package com.fqueue.commons;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLExecutor extends Connector{

    public SQLExecutor() {
    }
    //insert into table
    public void addPreparedStatement(Connection conn, String sqlInsert, String[] args) throws SQLException {
        PreparedStatement psInsert = null;
        try {
            psInsert = conn.prepareStatement(sqlInsert);
            for (int i = 0; i < args.length; i++) {
                int iParam = i + 1;
                psInsert.setString(iParam, args[i]);
            }
            psInsert.executeUpdate();
        }
        catch (SQLException se) {
            throw se;
        }

    }

    //update table
    public void editPreparedStatement(Connection conn, String sqlUpdate, String[] args) throws SQLException {
        try {
            addPreparedStatement(conn, sqlUpdate, args);
        }
        catch (SQLException se) {
            throw se;
        }
    }

    //delete table
    public void deletePreparedStatement(Connection conn, String sqlUpdate, String[] args) throws SQLException {
        try {
            addPreparedStatement(conn, sqlUpdate, args);
        }
        catch (SQLException se) {
            throw se;
        }
    }

    //select from table
    public ResultSet selectPreparedStatement(Connection conn, String sqlSelect, String[] args) throws SQLException {
        PreparedStatement psSelect = null;
        ResultSet rs = null;
        try {
            psSelect = conn.prepareStatement(sqlSelect);
            for (int i = 0; i < args.length; i++) {
                int iParam = i + 1;
                psSelect.setString(iParam, args[i]);
            }
            rs = psSelect.executeQuery();
        }
        catch (SQLException se) {
            throw se;
        }
        return rs;
    }
    
//    public static final List toList(ResultSet rs, List wantedColumnNames) throws SQLException
//    {
//        List rows = new ArrayList();
// 
//        int numWantedColumns = wantedColumnNames.size();
//        while (rs.next())
//        {
//            Map row = new LinkedHashMap();
//             
//            for (int i = 0; i < numWantedColumns; ++i)
//            {
//                String columnName   = (String)wantedColumnNames.get(i);
//                Object value = rs.getString(columnName);
//                row.put(columnName, value);
//            }
// 
//            rows.add(row);
//        }
// 
//        return rows;
//    }
}   
