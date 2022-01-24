/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revenue_report;

import com.Controller.PromptDialogController;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author HP
 */
public class DBConnection {
    private static final String Username = "root";
    private static final String Password = "";
    static File jarPath=new File(DBConnection.class.getProtectionDomain().getCodeSource().getLocation().getPath()), directory = new File("./");
    static String propertiesPath= directory.getAbsolutePath();


    private static Properties propt() throws IOException {
        Properties pop = new Properties();
        InputStream fx;
        if (!getLocalProperties()){
        fx = DBConnection.class.getResourceAsStream("/com/revenue_report/connection.properties");
        pop.load(fx);
        pop.store(new FileOutputStream(propertiesPath+"/connection.properties"),null);
        fx = new FileInputStream(propertiesPath+ "/connection.properties");
        pop.load(fx);
        }else{
            fx = new FileInputStream(propertiesPath+"/connection.properties");
            pop.load(fx);
        }
        return pop;
    }
    private static boolean getLocalProperties(){
        try {
            FileReader reader = new FileReader(propertiesPath+"/connection.properties");
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }

    public static Connection getConn() {
        try {
            propt();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Connection con ;
        try {
            String port = propt().getProperty("Port"), User = propt().getProperty("User"), iP = propt().getProperty("URL"),
                    userT = propt().getProperty("useTimeZone"), servTZ = propt().getProperty("serverTimeZone"), autoRect = propt().getProperty("autoReconnect"),
                    vSC = propt().getProperty("VerifyServerCertificate"), uSSl= propt().getProperty("UseSSL"), rSSL = propt().getProperty("RequireSSL"),
                    pass = propt().getProperty("Password"), sslMode = propt().getProperty("sslMode");
            final String URL = "jdbc:mysql://"+iP+":"+port+"/revenue_monitoring?useTimezone="+userT+"&serverTimezone="+servTZ+"&autoReconnect="+autoRect+"&sslmode= "+sslMode+"&useSSL="+uSSl+"&verifyServerCertificate="+vSC+"&requireSSL="+rSSL;
            con = DriverManager.getConnection(URL, User, pass);
        }catch (Exception e) {
            e.printStackTrace();
            assert e instanceof SQLException;
            if (((SQLException) e).getErrorCode() == 0) { //Error Code 0: database server offline
                new PromptDialogController("Error!", "Database server is offline!", true);
            }
            return null;
        }/*
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
        }*/
        return con;
    }
}
