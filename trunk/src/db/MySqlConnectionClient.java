/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.io.File;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

import java.sql.*;
import java.math.*;
import java.util.logging.Level;
import libCore.Config;

/**
 *
 * @author Mrkupi
 */
public class MySqlConnectionClient {

    private static Map<String, MySqlConnectionClient> _instances = new NonBlockingHashMap();
    private static final Lock createLock_ = new ReentrantLock();
    private Connection _connection;
    private String _host = "";
    private int _port = 0;
    private String _userName = "";
    private String _password = "";
    private String _database = "";
    private Boolean _auto_Reconnect = false;
    private int _max_reconnect = 0;
    private int _timeout = 0; //500 ms
    private int MAX_ACTIVE = 0;
    private int MAX_IDLE = 0;
    private int MAX_WAIT = 0;

    public static MySqlConnectionClient getInstance(String host, int port) {
        return getInstance(host, port, "", "", "");
    }

    public static MySqlConnectionClient getInstance(String host, int port, String database) {
        return getInstance(host, port, "", "", database);
    }

    public static MySqlConnectionClient getInstance(String host, int port, String userName, String password, String database) {
        String key = host + port + password + database;
        if (!_instances.containsKey(key)) {
            _instances.put(key, new MySqlConnectionClient(host, port, userName, password, database));
        }
        return _instances.get(key);
    }

    public MySqlConnectionClient(String host, int port, String userName, String password, String database) {

        this._host = host;
        this._port = port;
        this._userName = userName;
        this._password = password;
        this._database = database;

        this._auto_Reconnect    =   Boolean.valueOf(Config.getParam("sql", "autoReconnect" ));
        this._max_reconnect     =   Integer.valueOf(Config.getParam("sql", "maxReconnects" ));
        
        getConnection();
    }

    public MySqlConnectionClient(String host, int port, int timeout, String userName, String password, String database) {

        this._host = host;
        this._port = port;
        this._userName = userName;
        this._timeout = timeout;
        this._password = password;
        this._database = database;

        this._auto_Reconnect = Boolean.valueOf(Config.getParam("sql", share.ShareMacros.MYSQL_AUTO_RECONNECT));
        this._max_reconnect = Integer.valueOf(Config.getParam("sql", share.ShareMacros.MYSQL_MAX_RECONNECTS));
        
        getConnection();
    }

    /*
     * return JDBC connection
     */
    public Connection getConnection() {

        if (_connection == null) {
            try {
                String driver = "org.gjt.mm.mysql.Driver";
                // load the driver
                Class.forName(driver);
                String dbURL = "jdbc:mysql://" + _host + ":" + _port + "/" + _database;

                java.util.Properties connProperties = new java.util.Properties();
                connProperties.put( share.ShareMacros.DATABASE_USER, _userName );
                connProperties.put( share.ShareMacros.DATABASE_PASSWORD, _password );

                connProperties.put( share.ShareMacros.MYSQL_AUTO_RECONNECT, true );
                connProperties.put( share.ShareMacros.MYSQL_MAX_RECONNECTS, String.valueOf(_max_reconnect) );

                // Create jdbc connect
                this._connection = DriverManager.getConnection( dbURL, _userName, _password );
            } catch (ClassNotFoundException ex) {
                java.util.logging.Logger.getLogger(MySqlConnectionClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(MySqlConnectionClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return _connection;
    }

    /*
     * Close current JDBC connection in safe
     */
    public void close() {
        try {
            this._connection.close();
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(MySqlConnectionClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    

    /*
     * Excute a any command
     */
    public Boolean excute( String cmd ) {
        Boolean result  =   false;
        try {
            
            Statement sttm      =   _connection.createStatement();
            result      =   sttm.execute( cmd );
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(MySqlConnectionClient.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println( "Happen exception: " + ex );
        } finally {
            return result;
        }
    }
    
    /*
     * Excute a query string get data
     */
    public ResultSet excuteQuery( String cmd ) {
        ResultSet rs    =   null;
        try {
            
            Statement sttm  =   _connection.createStatement();
            rs              =   sttm.executeQuery( cmd );
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(MySqlConnectionClient.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println( "Happen exception: " + ex );
        } finally {
            return rs;
        }
    }
    
    /*
     * Excute a update query
     */
    public int excuteUpdate( String cmd ) {
        int result          =   -1;
        try {
            Statement sttm  =   _connection.createStatement();
            result      =   sttm.executeUpdate( cmd );
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(MySqlConnectionClient.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println( "Happen exception: " + ex );
        } finally {
            return result;
        }
    }
    
    
    /*
     * create new table in db
     * @tableName : Table Name
     * @colums : List of column in table
     * @types : List type correctly with column
     */
    public int createTable( String tableName, String[] colums, String[] types ) {
        if( colums.length != types.length )
            return -1;
        
        String strQuery =   String.format( "CREATE TABLE IF NOT EXIST %s (", tableName );
        for( int i = 0; i < colums.length; ++i ) {
            String columContent;
            if( i == colums.length - 1 ) {
                columContent    =   String.format( "%s %s)", colums[i], types[i] );
            } else {
                columContent    =   String.format( "%s %s,", colums[i], types[i] );
            }
            strQuery            =   strQuery + columContent;
        }
        
        return excuteUpdate(strQuery );
    }
    
    public int dropTable( String tableName ) {
        String strQuery =   String.format( "DROP TABLE \'s\'", tableName );
        
        return excuteUpdate(strQuery );
    }
    
    
    public ResultSet selectAll( String fromTable ) {
        
        String querryStr=   String.format( "SELECT * FROM %s", fromTable );
        
        return excuteQuery( querryStr );
    }
    
    public ResultSet selectCmd( String params, String fromTable ) {
        
        String querryStr=   String.format( "SELECT %s FROM %s", params, fromTable );
        
        return excuteQuery( querryStr );
    }
    
    public ResultSet selectCmdWhere( String params, String fromTable, String where ) {
        
        String querryStr=   String.format( "SELECT %s FROM %s WHERE %s", params, fromTable, where );
        
        return excuteQuery( querryStr );
    }
    
    public ResultSet selectCmdWhereEx( String params, String fromTable, String where, String ex ) {
        
        String querryStr=   String.format( "SELECT %s FROM %s WHERE %s %s", params, fromTable, where, ex );
        
        return excuteQuery( querryStr );
    }
    
    public int insertTable( String toTable, String values ) {
        
        String strInsert    =   String.format( "INSERT INTO %s VALUES (%s)", toTable, values );
        
        return excuteUpdate( strInsert );
    }
    
    public int insertTable( String toTable, String params, String values ) {
        
        String strInsert    =   String.format( "INSERT INTO %s (%s) VALUES (%s)", toTable, params, values );
        
        return excuteUpdate( strInsert );
    }
    
    public int updateTable( String table, String content, String where ) {
        
        String strQuery =   String.format( "UPDATE %s SET %s WHERE %s", table, content, where );
        return excuteUpdate( strQuery );
    }
    
    public int deleteAll( String table ) {
        
        String strQuery =   String.format( "DELETE FROM %s", table );
        return excuteUpdate( strQuery );
    }
    
    public int deleteAllWhere( String table, String where ) {
        
        String strQuery =   String.format( "DELETE FROM %s WHERE %s", table, where );
        return excuteUpdate( strQuery );
    }
    
    public int addColumn( String table, String columName, String type ) {
        
        String strQuery =   String.format( "ALTER TABLE %s ADD COLUMN %s %s", table, columName, type );
        return excuteUpdate( strQuery );
    }
    
    public int deleteColumn( String table, String columName ) {
        
        String strQuery =   String.format( "ALTER TABLE %s DROP COLUMN %s", table, columName );
        return excuteUpdate( strQuery );
    }
    
    public int changeColumnName( String table, String oldName, String newName, String type ) {
        
        String strQuery =   String.format( "ALTER TABLE %s CHANGE %s %s %s", table, oldName, newName, type );
        return excuteUpdate( strQuery );
    }
    
    
    
    public static void main(String[] args) {
        Connection conn = MySqlConnectionClient.getInstance("localhost", 8888, "root", "cgk", "cgk_dbptk").getConnection();
        System.out.println("Result: " + conn);
        
//        Connection conn = null;
//
//        String url = "jdbc:mysql://localhost:8888/";
//
//        String dbName = "cgk_dbptk";
//
//        String driver = "com.mysql.jdbc.Driver";
//
//        String userName = "root";
//
//        String password = "cgk";
//        java.util.Properties connProperties = new java.util.Properties();
//        connProperties.put( share.ShareMacros.DATABASE_USER, "root" );
//        connProperties.put( share.ShareMacros.DATABASE_PASSWORD, "cgk" );
//
//        connProperties.put( share.ShareMacros.MYSQL_AUTO_RECONNECT, true );
//        connProperties.put( share.ShareMacros.MYSQL_MAX_RECONNECTS, 4 );
//        try {
//
//        Class.forName(driver).newInstance();
//
//        conn = DriverManager.getConnection( url+dbName, connProperties );
//
//        System.out.println("Connected to the database");
//
//        conn.close();
//
//        System.out.println("Disconnected from database");
//
//        } catch (Exception e) {
//
//        System.out.println("NO CONNECTION =(");
//
//        }
        
    }
}
