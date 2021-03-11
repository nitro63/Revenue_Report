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
        return AmtPayedProperty().get();
    }
    
    public final void setAmtPayed(String AmtPayed){
        AmtPayedProperty().set(AmtPayed);
    }
     
    public StringProperty AmtReprtdProperty(){
        return AmtReprtd;
    }
    
    public final String getAmtReprtd(){
        return AmtReprtdProperty().get();
    }
    
    public final void setAmtReprtd(String AmtReprtd){
        AmtReprtdProperty().set(AmtReprtd);
    }
     
    public StringProperty DiffProperty(){
        return Diff;
    }
    
    public final String getDiff(){
        return DiffProperty().get();
    }
    
    public final void setDiff(String Diff){
        DiffProperty().set(Diff);
    }
     
    public StringProperty MonthProperty(){
        return Month;
    }
    
    public final String getMonth(){
        return MonthProperty().get();
    }
    
    public final void setMonth(String Month){
        MonthProperty().set(Month);
    }
     
    public StringProperty RmksProperty(){
        return Rmks;
    }
    
    public final String getRmks(){
        return RmksProperty().get();
    }
    
    public final void setRmks(String Rmks){
        RmksProperty().set(Rmks);
    }
    
}
