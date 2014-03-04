/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package db.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mrkupi
 */
public class DataTable extends ArrayList<DataRow> {
    public static DataTable createDataTable(ResultSet rs, String ... columns)
    {
        DataTable dt = new DataTable();
        try {
            
            while ( rs.next() )
            {
                DataRow row = new DataRow();
                
                for ( String col : columns )
                {
                    row.put( col, rs.getString( col ) );
                }
                
                dt.add( row );
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataTable.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println( "Exception happen when create table: " + ex );
        } finally {
            return dt;
        }
    }
}
