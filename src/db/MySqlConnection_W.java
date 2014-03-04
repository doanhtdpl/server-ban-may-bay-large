/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import db.Utils.DataTable;
import java.sql.SQLException;
import libCore.Config;
import libCore.Util;

/**
 *
 * @author Mrkupi
 */
public class MySqlConnection_W {
    
    public static MySqlConnectionClient _connection;
    
    private MySqlConnection_W() {
        
        String host         =   Config.getParam("sql", "host");
        int port            =   Integer.valueOf(Config.getParam("sql", "port"));
        String userName     =   Config.getParam("sql", "userName");
        String password     =   Config.getParam("sql", "pass");
        String database     =   Config.getParam("sql", "database");
        _connection         =   MySqlConnectionClient.getInstance(host, port, userName, password, database);
    }
    
    private static class MySqlConnection_WHolder {

        private static final MySqlConnection_W INSTANCE = new MySqlConnection_W();
    }
    
    public static MySqlConnection_W getInstance() {
        return MySqlConnection_W.MySqlConnection_WHolder.INSTANCE;
    }
    
    public static void close() {
        _connection.close();
    }

    public static int createTable( String tableName, String[] columns, String[] types ) {
        return _connection.createTable( tableName, columns, types );
    }
    
    public static int dropTable( String tableName ) {
        return _connection.dropTable(tableName);
    }
    
    public static int insertTable( String table, String... params ) {
        String values   =   Util.arrayToString(params);
        return _connection.insertTable( table, values );
    }
    
    public static int insertTable( String table, String[] columns, String[] values ) {
        String strColumns   =   Util.arrayToString( columns );
        String strValues    =   Util.arrayToString( values );
        return _connection.insertTable( table, strColumns, strValues );
    }
    
    /*
     * Return result of update table
     * Return -1 if failed
     */
    public static int updateTable( String table, String[] columns, String[] values, String where ) {
        if( columns.length != values.length )
            return -1;
        
        String strContent =   "";
        for( int i = 0; i < columns.length; ++i ) {
            String columContent;
            if( i == columns.length - 1 ) {
                columContent    =   String.format( "%s=%s", columns[i], values[i] );
            } else {
                columContent    =   String.format( "%s=%s,", columns[i], values[i] );
            }
            strContent          +=  columContent;
        }
        
        return _connection.updateTable(table, strContent, where);
    }
    
    public static int deleteAll( String table ) {
        return _connection.deleteAll(table);
    }
    
    public static int deleteAllWhere( String table, String where ) {
        return _connection.deleteAllWhere(table, where);
    }
    
    public static int addColumn( String table, String columName, String type ) {
        return _connection.addColumn(table, columName, type);
    }
    
    public static int deleteColumn( String table, String columnName ) {
        return _connection.deleteColumn(table, columnName);
    }
    
    public static int changeColumnName( String table, String oldCName, String newCName, String type ) {
        return _connection.changeColumnName(table, oldCName, newCName, type);
    }
    
    // Test mysqlconnection W
    public static void main( String[] args ) {
        
        // Test create table
//        String[] colums =   { "food", "level", "exp" };
//        String[] types  =   { "VARCHAR(15)", "VARCHAR(15)", "VARCHAR(15)" };
//        int result  =   MySqlConnection_W.getInstance().createTable( "Penguins2", colums, types );
//        System.out.println( result );
        
        // Test
//        int result  =   MySqlConnection_W.getInstance().insertTable( "Penguins2", "20", "30", "40");
//        System.out.println( result );
        // Test
//        String[] colums =   { "food", "level", "exp" };
//        String[] types  =   { "30", "30", "30" };
//        int result  =   MySqlConnection_W.getInstance().insertTable( "Penguins2", colums, types );
//        System.out.println( result );
        
//        System.out.println( MySqlConnection_W.getInstance().deleteAllWhere( "Penguins2", "food = 20" ) );
//        System.out.println( MySqlConnection_W.getInstance().deleteAll( "Penguins2" ) );
        
//        String[] colums =   { "food", "level", "exp" };
//        String[] types  =   { "30", "30", "30" };
//        MySqlConnection_W.getInstance().updateTable( "Penguins2", colums, types, "food = 20" );
        
//        System.out.println(MySqlConnection_W.getInstance().addColumn( "Penguins2", "egg", "VARCHAR(30)" ));
        
//        System.out.println(MySqlConnection_W.getInstance().deleteColumn( "Penguins2", "egg" ));
        
        System.out.println(MySqlConnection_W.getInstance().changeColumnName( "Penguins2", "food", "nFood", "VARCHAR(50)"));
    }
}
