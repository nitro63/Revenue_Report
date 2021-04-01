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
 * @author NiTrO
 */
public class GetColPay {
    private StringProperty Month, AmtReprtd, AmtPayed, Diff, Rmks;
    private String month, amtReprtd, amtPayed, diff, rmks;
    
    public GetColPay(){
        
    }
    
    public GetColPay(String Month, String AmtReprtd, String AmtPayed, String Diff, String Rmks){
        this.AmtPayed = new SimpleStringProperty(AmtPayed);
        this.AmtReprtd = new SimpleStringProperty(AmtReprtd);
        this.Diff = new SimpleStringProperty(Diff);
        this.Month = new SimpleStringProperty(Month);
        this.Rmks = new SimpleStringProperty(Rmks);
    }
    
     
    public StringProperty AmtPayedProperty(){
        return AmtPayed;
    }
    
    public final String getAmtPayed(){
        amtPayed = AmtPayedProperty().get();
        return amtPayed;
    }
    
    public final void setAmtPayed(String AmtPayed){
        AmtPayedProperty().set(AmtPayed);
    }
     
    public StringProperty AmtReprtdProperty(){
        return AmtReprtd;
    }
    
    public final String getAmtReprtd(){
        amtReprtd = AmtReprtdProperty().get();
        return amtReprtd;
    }
    
    public final void setAmtReprtd(String AmtReprtd){
        AmtReprtdProperty().set(AmtReprtd);
    }
     
    public StringProperty DiffProperty(){
        return Diff;
    }
    
    public final String getDiff(){
        diff = DiffProperty().get();
        return diff;
    }
    
    public final void setDiff(String Diff){
        DiffProperty().set(Diff);
    }
     
    public StringProperty MonthProperty(){
        return Month;
    }
    
    public final String getMonth(){
        month = MonthProperty().get();
        return month;
    }
    
    public final void setMonth(String Month){
        MonthProperty().set(Month);
    }
     
    public StringProperty RmksProperty(){
        return Rmks;
    }
    
    public final String getRmks(){
        rmks = RmksProperty().get();
        return rmks;
    }
    
    public final void setRmks(String Rmks){
        RmksProperty().set(Rmks);
    }
    
}
