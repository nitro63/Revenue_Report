package com.Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class GetValueBooksEntries {

    private StringProperty Quarter, Week, Year, Month, Date, ValueBook, FirstSerial, LastSerial, Quantity, ValAmount, CumuAmount, PurAmount, remarks, ID;

    private String year, month, date, valueBook, firstSerial, lastSerial, quantity, valAmount, cumuAmount, purAmount;

    public GetValueBooksEntries(){   }

    public GetValueBooksEntries( String week, String date, String valueBook, String firstSerial, String lastSerial, String quantity, String valAmount, String cumuAmount, String purAmount, String remarks) {
        this.Week = new SimpleStringProperty(week);
        this.Date = new SimpleStringProperty(date);
        this.ValueBook = new SimpleStringProperty(valueBook);
        this.FirstSerial = new SimpleStringProperty(firstSerial);
        this.LastSerial = new SimpleStringProperty(lastSerial);
        this.Quantity = new SimpleStringProperty(quantity);
        this.ValAmount = new SimpleStringProperty(valAmount);
        this.CumuAmount = new SimpleStringProperty(cumuAmount);
        this.PurAmount = new SimpleStringProperty(purAmount);
        this.remarks = new SimpleStringProperty(remarks);
    }

    public GetValueBooksEntries(String date, String valueBook, String firstSerial, String lastSerial, String quantity, String valAmount, String cumuAmount, String purAmount, String remarks) {
        this.Date = new SimpleStringProperty(date);
        this.ValueBook = new SimpleStringProperty(valueBook);
        this.FirstSerial = new SimpleStringProperty(firstSerial);
        this.LastSerial = new SimpleStringProperty(lastSerial);
        this.Quantity = new SimpleStringProperty(quantity);
        this.ValAmount = new SimpleStringProperty(valAmount);
        this.CumuAmount = new SimpleStringProperty(cumuAmount);
        this.PurAmount = new SimpleStringProperty(purAmount);
        this.remarks = new SimpleStringProperty(remarks);
    }

    public String getQuarter() {
        return Quarter.get();
    }

    public StringProperty quarterProperty() {
        return Quarter;
    }

    public void setQuarter(String quarter) {
        this.Quarter.set(quarter);
    }

    public String getWeek() {
        return Week.get();
    }

    public StringProperty weekProperty() {
        return Week;
    }

    public void setWeek(String week) {
        this.Week.set(week);
    }

    public String getRemarks() {
        return remarks.get();
    }

    public StringProperty remarksProperty() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks.set(remarks);
    }

    public String getYear() {
        year = Year.get();
        return year;
    }

    public StringProperty yearProperty() {
        return Year;
    }

    public void setYear(String year) {
        this.Year.set(year);
    }

    public String getMonth() {
        month = monthProperty().get();
        return month;
    }

    public StringProperty monthProperty() {
        return Month;
    }

    public void setMonth(String month) {
        monthProperty().set(month);
    }

    public String getDate() {
        date = Date.get();
        return date;
    }

    public StringProperty dateProperty() {
        return Date;
    }

    public void setDate(String date) {
        this.Date.set(date);
    }

    public String getValueBook() {
        valueBook = ValueBook.get();
        return valueBook;
    }

    public StringProperty valueBookProperty() {
        return ValueBook;
    }

    public void setValueBook(String valueBook) {
        this.ValueBook.set(valueBook);
    }

    public String getFirstSerial() {
        firstSerial = FirstSerial.get();
        return firstSerial;
    }

    public StringProperty firstSerialProperty() {
        return FirstSerial;
    }

    public void setFirstSerial(String firstSerial) {
        this.FirstSerial.set(firstSerial);
    }

    public String getLastSerial() {
        lastSerial = LastSerial.get();
        return lastSerial;
    }

    public StringProperty lastSerialProperty() {
        return LastSerial;
    }

    public void setLastSerial(String lastSerial) {
        this.LastSerial.set(lastSerial);
    }

    public String getQuantity() {
        quantity = Quantity.get();
        return quantity;
    }

    public StringProperty quantityProperty() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        this.Quantity.set(quantity);
    }

    public String getValAmount() {
        valAmount = ValAmount.get();
        return valAmount;
    }

    public StringProperty valAmountProperty() {
        return ValAmount;
    }

    public void setValAmount(String valAmount) {
        this.ValAmount.set(valAmount);
    }

    public String getCumuAmount() {
        cumuAmount = CumuAmount.get();
        return cumuAmount;
    }

    public StringProperty cumuAmountProperty() {
        return CumuAmount;
    }

    public void setCumuAmount(String cumuAmount) {
        this.CumuAmount.set(cumuAmount);
    }

    public String getPurAmount() {
        purAmount = PurAmount.get();
        return purAmount;
    }

    public StringProperty purAmountProperty() {
        return PurAmount;
    }

    public void setPurAmount(String purAmount) {
        this.PurAmount.set(purAmount);
    }
}
