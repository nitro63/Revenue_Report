/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author HP
 */
public class GetTargAnalReport {
    private String month, totalRevenue, achvAmt, achvPercent, outAmt, outPercent;
    private StringProperty Month, TotalRevenue, AchvAmt, AchvPercent, OutAmt, OutPercent;
    
    public GetTargAnalReport(){
        
    }
    
    public GetTargAnalReport(String month, String totalRevenue, String achvAmt, String achvPercent, String outAmt, String outPercent){
        this.Month = new SimpleStringProperty(month);
        this.TotalRevenue = new SimpleStringProperty(totalRevenue);
        this.AchvAmt = new SimpleStringProperty(achvAmt);
        this.AchvPercent = new SimpleStringProperty(achvPercent);
        this.OutAmt = new SimpleStringProperty(outAmt);
        this.OutPercent = new SimpleStringProperty(outPercent);
//        this.remarks = new SimpleStringProperty(remarks);
        
    }
    
      
    public StringProperty monthProperty(){
        return Month;
    }
    
    public final String getmonth(){
        month = Month.get();
        return monthProperty().get();
    }
    
    public final void setmonth(String month){
        monthProperty().set(month);
    }
      
    public StringProperty totalRevenueProperty(){
        return TotalRevenue;
    }
    
    public final String gettotalRevenue(){
        totalRevenue = TotalRevenue.get();
        return totalRevenue;
    }
    
    public final void settotalRevenue(String totalRevenue){
        totalRevenueProperty().set(totalRevenue);
    }
      
    public StringProperty achvAmtProperty(){
        return AchvAmt;
    }
    
    public final String getachvAmt(){
        achvAmt = AchvAmt.get();
        return achvAmt;
    }
    
    public final void setCenter(String achvAmt){
        achvAmtProperty().set(achvAmt);
    }
      
    public StringProperty achvPercentProperty(){
        return AchvPercent;
    }
    
    public final String getachvPercent(){
        achvPercent = AchvPercent.get();
        return achvPercent;
    }
    
    public final void setachvPercent(String achvPercent){
        achvPercentProperty().set(achvPercent);
    }
      
    public StringProperty outAmtProperty(){
        return OutAmt;
    }
    
    public final String getoutAmt(){
        outAmt = OutAmt.get();
        return outAmt;
    }
    
    public final void setoutAmt(String outAmt){
        outAmtProperty().set(outAmt);
    }
      
    public StringProperty outPercentProperty(){
        return OutPercent;
    }
    
    public final String getoutPercent(){
        outPercent = OutPercent.get();
        return outPercent;
    }
    
    public final void setoutPercent(String outPercent){
        outPercentProperty().set(outPercent);
    }
      
//    public StringProperty remarksProperty(){
//        return remarks;
//    }
//    
//    public final String getremarks(){
//        return remarksProperty().get();
//    }
//    
//    public final void setremarks(String remarks){
//        remarksProperty().set(remarks);
//    }
}
