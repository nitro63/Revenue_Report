/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package revenue_report;

import Controller.PromptDialogController;
import org.apache.log4j.Logger;

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


    public static Connection getConn() {
        Connection con ;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            Logger.getLogger(DBConnection.class.getName()).error(e);
            e.printStackTrace();
        }
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/"
                    + "revenue_monitoring" + "?useTimezone=true&serverTimezone=UTC&autoReconnect=true", Username, Password);
        }catch (SQLException e) {
            Logger log = Logger.getLogger(DBConnection.class.getName());
            log.error(e.getMessage(), e);
            log.info(e);
            e.printStackTrace();
            if (e.getErrorCode() == 0) { //Error Code 0: database server offline
                new PromptDialogController("Error!", "Database server is offline!");
            }
            return null;
        }
        return con;
    }
}
