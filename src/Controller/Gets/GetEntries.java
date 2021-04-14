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
public class GetEntries {
    private String code, center, item, date, month, amount, week, year, quarter;
   private  StringProperty Code, Center, Item, Date, Month, Amount, Week, Year, Quarter;
//   private float Amount;
//    int Week;
   
   public GetEntries(){
       
   }

    public GetEntries(String Code, String Center, String Item, String Date, String Month, String Amount, String Week, String Year, String Quarter) {
        this.Code = new SimpleStringProperty(Code);
        this.Item = new  SimpleStringProperty(Item);
        this.Center = new  SimpleStringProperty(Center);
        this.Date = new SimpleStringProperty(Date);
        this.Month =new SimpleStringProperty( Month);
        this.Amount = new SimpleStringProperty(Amount) ;
        this.Week = new SimpleStringProperty(Week);
        this.Year = new SimpleStringProperty(Year);
        this.Quarter = new SimpleStringProperty(Quarter);
    }

    public GetEntries(String Date, String Month, String Week, String Year, String Quarter, String Amount) {
        this.Date = new SimpleStringProperty(Date);
        this.Month =new SimpleStringProperty( Month);
        this.Amount = new SimpleStringProperty(Amount) ;
        this.Week = new SimpleStringProperty(Week);
        this.Year = new SimpleStringProperty(Year);
        this.Quarter = new SimpleStringProperty(Quarter);
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
       code = CodeProperty().get();
        return code;
    }

    public final void setCode(String Code) {
        CodeProperty().set(Code);
    }
    
    public StringProperty CenterProperty() {
        return Center;
    }
    
    public final String getCenter() {
       center = CenterProperty().get();
        return center;
    }

    public final void setCenter(String Center) {
        CenterProperty().set(Center);
    }
    public StringProperty QuarterProperty() {
        return Quarter;
    }
    
    public final String getQuarter() {
       quarter = QuarterProperty().get();
        return quarter;
    }

    public final void setQuarter(String Quarter) {
        QuarterProperty().set(Quarter);
    }
    
     public StringProperty YearProperty() {
        return Year;
    }
    
    public final String getYear() {
       year = YearProperty().get();
        return year;
    }

    public final void setYear(String Year) {
        YearProperty().set(Year);
    }

    public StringProperty ItemProperty() {
        return Item;
    }
    
    public final String getItem(){
       item = ItemProperty().get();
        return item;
    }

    public final void setItem(String Item) {
        ItemProperty().set(Item);
    }

    public StringProperty DateProperty() {
        return Date;
    }

    public final String getDate(){
       date = DateProperty().get();
        return date;
    }
    public final void setDate(String Date) {
        DateProperty().set(Date);
    }

    public StringProperty MonthProperty() {
        return Month;
    }
    
    public final String getMonth(){
       month = MonthProperty().get();
        return month;
    }

    public final void setMonth(String Month) {
        MonthProperty().set(Month);
    }

    public StringProperty AmountProperty() {
        return Amount;
    }
    
    public final String getAmount(){
       amount = AmountProperty().get();
        return amount;
    }

    public final void setAmount(String Amount) {
        AmountProperty().set(Amount);
    }

    public StringProperty WeekProperty() {
        return Week;
    }
    
    public final String getWeek(){
       week = WeekProperty().get();
        return week;
    }

    public final void setWeek(String Week) {
        WeekProperty().set(Week);
    }
    
}


