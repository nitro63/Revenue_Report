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
public class GetCollectEnt {
    private StringProperty Center, GCR, Year, Amount, Month, Date, Type;
    
    public GetCollectEnt(){
        
    }
    
    public GetCollectEnt( String Center, String Amount, String GCR, String Month, String Date, String Year, String Type){
        this.Amount = new SimpleStringProperty(Amount);
        this.GCR = new SimpleStringProperty(GCR);
        this.Center = new SimpleStringProperty(Center);
        this.Month = new SimpleStringProperty(Month);
        this.Date = new SimpleStringProperty(Date);
        this.Year = new SimpleStringProperty(Year);
        this.Type = new SimpleStringProperty(Type);
    }

    public GetCollectEnt(String Amount, String GCR, String Month, String Date, String Year, String Type){
        this.Amount = new SimpleStringProperty(Amount);
        this.GCR = new SimpleStringProperty(GCR);
        this.Month = new SimpleStringProperty(Month);
        this.Date = new SimpleStringProperty(Date);
        this.Year = new SimpleStringProperty(Year);
        this.Type = new SimpleStringProperty(Type);
    }
    
     
    public StringProperty CenterProperty(){
        return Center;
    }
    
    public final String getCenter(){
        return CenterProperty().get();
    }
    
    public final void setCenter(String Center){
        CenterProperty().set(Center);
    }
     
    public StringProperty TypeProperty(){
        return Type;
    }
    
    public final String getType(){
        return TypeProperty().get();
    }
    
    public final void setType(String Type){
        TypeProperty().set(Type);
    }
         
    public StringProperty GCRProperty(){
        return GCR;
    }
    
    public final String getGCR(){
        return GCRProperty().get();
    }
    
    public final void setGCR(String GCR){
        GCRProperty().set(GCR);
    }
     
    public StringProperty AmountProperty(){
        return Amount;
    }
    
    public final String getAmount(){
        return AmountProperty().get();
    }
    
    public final void setAmount(String Amount){
        AmountProperty().set(Amount);
    }
     
    public StringProperty YearProperty(){
        return Year;
    }
    
    public final String getYear(){
        return YearProperty().get();
    }
    
    public final void setYear(String Year){
        YearProperty().set(Year);
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
    
     
    public StringProperty DateProperty(){
        return Date;
    }
    
    public final String getDate(){
        return DateProperty().get();
    }
    
    public final void setDate(String Date){
        DateProperty().set(Date);
    }
    
}
