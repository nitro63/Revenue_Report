/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package revenue_report;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author HP
 */
public class GetEntries {
   private  StringProperty Code, Item, Date, Month, Amount, Week, Year, Quarter;
//   private float Amount;
//    int Week;
   
   public GetEntries(){
       
   }

    public GetEntries(String Code, String Item, String Date, String Month, String Amount, String Week, String Year, String Quarter) {
        this.Code = new SimpleStringProperty(Code);
        this.Item = new  SimpleStringProperty(Item);
        this.Date = new SimpleStringProperty(Date);
        this.Month =new SimpleStringProperty( Month);
        this.Amount = new SimpleStringProperty(Amount) ;
        this.Week = new SimpleStringProperty(Week);
        this.Year = new SimpleStringProperty(Year);
        this.Quarter = new SimpleStringProperty(Quarter);
    }

    public StringProperty CodeProperty() {
        return Code;
    }
    
    public final String getCode() {
        return CodeProperty().get();
    }

    public final void setCode(String Code) {
        CodeProperty().set(Code);
    }
    public StringProperty QuarterProperty() {
        return Quarter;
    }
    
    public final String getQuarter() {
        return QuarterProperty().get();
    }

    public final void setQuarter(String Quarter) {
        QuarterProperty().set(Quarter);
    }
    
     public StringProperty YearProperty() {
        return Year;
    }
    
    public final String getYear() {
        return YearProperty().get();
    }

    public final void setYear(String Year) {
        YearProperty().set(Year);
    }

    public StringProperty ItemProperty() {
        return Item;
    }
    
    public final String getItem(){
        return ItemProperty().get();
    }

    public final void setItem(String Item) {
        ItemProperty().set(Item);
    }

    public StringProperty DateProperty() {
        return Date;
    }

    public final String getDate(){
        return DateProperty().get();
    }
    public final void setDate(String Date) {
        DateProperty().set(Date);
    }

    public StringProperty MonthProperty() {
        return Month;
    }
    
    public final String getMonth(){
        return MonthProperty().get();
    }

    public final void setMonth(String Month) {
        MonthProperty().set(Month);
    }

    public StringProperty AmountProperty() {
        return Amount;
    }
    
    public final String getAmount(){
        return AmountProperty().get();
    }

    public final void setAmount(String Amount) {
        AmountProperty().set(Amount);
    }

    public StringProperty WeekProperty() {
        return Week;
    }
    
    public final String getWeek(){
        return WeekProperty().get();
    }

    public final void setWeek(String Week) {
        WeekProperty().set(Week);
    }
    
}


