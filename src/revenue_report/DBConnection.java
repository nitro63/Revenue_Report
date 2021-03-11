/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package revenue_report;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author HP
 */
public class DBConnection {
    private static final String Username = "root";
    private static final String Password = "";


    public static Connection getConn() throws SQLException, ClassNotFoundException{        
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/"
                + "revenue_monitoring" + "?useTimezone=true&serverTimezone=UTC&autoReconnect=true", Username, Password);
    }
}
