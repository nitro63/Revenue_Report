/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.Gets;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author HP
 */
public class GetTargAnalReport {
    private StringProperty month, totalRevenue, remarks, achvAmt, achvPercent, outAmt, outPercent;
    
    public GetTargAnalReport(){
        
    }
    
    public GetTargAnalReport(String month, String totalRevenue, String achvAmt, String achvPercent, String outAmt, String outPercent){
        this.month = new SimpleStringProperty(month);
        this.totalRevenue = new SimpleStringProperty(totalRevenue);
        this.achvAmt = new SimpleStringProperty(achvAmt);
        this.achvPercent = new SimpleStringProperty(achvPercent);
        this.outAmt = new SimpleStringProperty(outAmt);
        this.outPercent = new SimpleStringProperty(outPercent);
//        this.remarks = new SimpleStringProperty(remarks);
        
    }
    
      
    public StringProperty monthProperty(){
        return month;
    }
    
    public final String getmonth(){
        return monthProperty().get();
    }
    
    public final void setmonth(String month){
        monthProperty().set(month);
    }
      
    public StringProperty totalRevenueProperty(){
        return totalRevenue;
    }
    
    public final String gettotalRevenue(){
        return totalRevenueProperty().get();
    }
    
    public final void settotalRevenue(String totalRevenue){
        totalRevenueProperty().set(totalRevenue);
    }
      
    public StringProperty achvAmtProperty(){
        return achvAmt;
    }
    
    public final String getachvAmt(){
        return achvAmtProperty().get();
    }
    
    public final void setCenter(String achvAmt){
        achvAmtProperty().set(achvAmt);
    }
      
    public StringProperty achvPercentProperty(){
        return achvPercent;
    }
    
    public final String getachvPercent(){
        return achvPercentProperty().get();
    }
    
    public final void setachvPercent(String achvPercent){
        achvPercentProperty().set(achvPercent);
    }
      
    public StringProperty outAmtProperty(){
        return outAmt;
    }
    
    public final String getoutAmt(){
        return outAmtProperty().get();
    }
    
    public final void setoutAmt(String outAmt){
        outAmtProperty().set(outAmt);
    }
      
    public StringProperty outPercentProperty(){
        return outPercent;
    }
    
    public final String getoutPercent(){
        return outPercentProperty().get();
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
