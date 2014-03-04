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
public class MySqlConnection_Rd {
    
    public static MySqlConnectionClient _connection;
    
    private MySqlConnection_Rd() {
        
        String host         =   Config.getParam("sql", "host");
        int port            =   Integer.valueOf(Config.getParam("sql", "port"));
        String userName     =   Config.getParam("sql", "userName");
        String password     =   Config.getParam("sql", "pass");
        String database     =   Config.getParam("sql", "database");
        _connection         =   MySqlConnectionClient.getInstance(host, port, userName, password, database);
    }
    
    private static class MySqlConnection_RdHolder {

        private static final MySqlConnection_Rd INSTANCE = new MySqlConnection_Rd();
    }
    
    public static MySqlConnection_Rd getInstance() {
        return MySqlConnection_Rd.MySqlConnection_RdHolder.INSTANCE;
    }
    
    public static void close() throws SQLException {
        _connection.close();
    }
    
    public static DataTable excuteQuery( String strQuery, String... colums ) {
        return DataTable.createDataTable( _connection.excuteQuery(strQuery), colums );
    }
    
    public static DataTable selectAll( String table, String... colums ) {
        return DataTable.createDataTable( _connection.selectAll(table), colums );
    }
    
    public static DataTable selectCmd( String table, String... colums ) {
        String params   =   Util.arrayToString(colums);
        return DataTable.createDataTable( _connection.selectCmd( params, table), colums );
    }
    
    public static DataTable selectCmdWhere( String table, String where, String... colums ) {
        String params   =   Util.arrayToString(colums);
        return DataTable.createDataTable( _connection.selectCmdWhere( params, table, where ), colums );
    }
    
    public static DataTable selectCmdWhereEx( String table, String where, String ex, String... colums ) {
        String params   =   Util.arrayToString(colums);
        return DataTable.createDataTable( _connection.selectCmdWhereEx( params, table, where, ex ), colums );
    }
    
    // Test mysqlconnection Rd
    public static void main( String[] args ) {
//        DataTable dt    =   MySqlConnection_Rd.getInstance().selectAll( "Penguins2", "nFood", "level", "exp" );
//        dt    =   MySqlConnection_Rd.getInstance().selectCmd( "Penguins2", "nFood", "level" );
//        dt    =   MySqlConnection_Rd.getInstance().selectCmdWhere("Penguins2", "nFood = 30", "nFood", "level" );
//        dt    =   MySqlConnection_Rd.getInstance().selectCmdWhereEx("Penguins2", "nFood >= 0", "ORDER BY nFood ASC", "nFood", "level" );
    }
}
