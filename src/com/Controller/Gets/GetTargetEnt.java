/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controller.Gets;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author HP
 */
public class GetTargetEnt {
    private StringProperty Center, Year, Amount, ID;
    
    public GetTargetEnt(){
        
    }

    public GetTargetEnt(  String Amount, String Year){
        this.Amount = new SimpleStringProperty(Amount);
        this.Year = new SimpleStringProperty(Year);
    }

    public GetTargetEnt( String ID, String Amount, String Year){
        this.ID = new SimpleStringProperty(ID);
        this.Amount = new SimpleStringProperty(Amount);
        this.Year = new SimpleStringProperty(Year);
    }

    public GetTargetEnt( String ID, String Center, String Amount, String Year){
        this.ID = new SimpleStringProperty(ID);
        this.Amount = new SimpleStringProperty(Amount);
        this.Center = new SimpleStringProperty(Center);
        this.Year = new SimpleStringProperty(Year);
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

    public String getID() {
        return ID.get();
    }

    public StringProperty IDProperty() {
        return ID;
    }

    public void setID(String ID) {
        this.ID.set(ID);
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
    
    public StringProperty AmountProperty(){
        return Amount;
    }
    
    public final String getAmount(){
        return AmountProperty().get();
    }
    
    public final void setAmount(String Amount){
        AmountProperty().set(Amount);
    }
}
