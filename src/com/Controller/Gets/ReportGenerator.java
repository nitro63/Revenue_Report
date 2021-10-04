/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controller.Gets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.revenue_report.DBConnection;

/**
 *
 * @author NiTrO
 */
public class ReportGenerator {
    private final Connection con;
    private PreparedStatement stmnt;
    
    
    
    public ReportGenerator() throws SQLException, ClassNotFoundException{
        this.con = DBConnection.getConn();
    }
    
    public Float getWeekSum(String Center, String item, String month, String week, String Year) throws SQLException{
        float totalAmount;
       stmnt = con.prepareStatement(" SELECT `revenueAmount`   FROM `daily_entries` WHERE  `revenueItem` = '"+item+"' AND `revenueWeek` = '"+week+"' AND `daily_revCenter` = '"+Center+"' AND `revenueMonth` = '"+month+"' AND `revenueYear` = '"+Year+"'  ");
       ResultSet rs = stmnt.executeQuery();
       ResultSetMetaData meta= rs.getMetaData();
       int row = 0 ;        
       int col = meta.getColumnCount();
       ObservableList<Float> Amount = FXCollections.observableArrayList();//List to Store revenue items which have entries for the specified week
       while(rs.next()){//looping through the retrieved revenueItems result set
           for(int j=1; j<=col; j++){
               if(j == 1){
           String revitem =rs.getObject(j).toString();
           Amount.add(Float.parseFloat(revitem));//adding revenue items to list
           }
           }     
       }
        totalAmount = 0;
        for(int i = 0; i < Amount.size(); i++){
            totalAmount += Amount.get(i);
        }
        return totalAmount;
    }
    
    
    
    
    
}
