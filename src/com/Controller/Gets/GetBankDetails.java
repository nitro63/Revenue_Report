package com.Controller.Gets;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class GetBankDetails {
    StringProperty gcr, Date,Year, ChequeDate, ChequeNumber, Bank, Amount, Month, ID;
    String GCR, date, chequeDate, chequeNumber, bank, amount;
    public GetBankDetails(){
    }
    public GetBankDetails(String GCR, String Year, String Month, String Date, String chequeDate, String chequeNumber, String Bank, String Amount){
        this.gcr = new SimpleStringProperty(GCR);
        this.Year = new SimpleStringProperty(Year);
        this.Month = new SimpleStringProperty(Month);
        this.Date = new SimpleStringProperty(Date);
        this.ChequeDate = new SimpleStringProperty(chequeDate);
        this.ChequeNumber = new SimpleStringProperty(chequeNumber);
        this.Bank = new SimpleStringProperty(Bank);
        this.Amount = new SimpleStringProperty(Amount);
    }
    public GetBankDetails(String ID, String GCR,  String Date, String chequeDate, String chequeNumber, String Bank, String Amount){
        this.gcr = new SimpleStringProperty(GCR);
        this.Date = new SimpleStringProperty(Date);
        this.ChequeDate = new SimpleStringProperty(chequeDate);
        this.ChequeNumber = new SimpleStringProperty(chequeNumber);
        this.ID = new SimpleStringProperty(ID);
        this.Bank = new SimpleStringProperty(Bank);
        this.Amount = new SimpleStringProperty(Amount);
    }
    public GetBankDetails(String GCR,  String Date, String chequeDate, String chequeNumber, String Bank, String Amount){
        this.gcr = new SimpleStringProperty(GCR);
        this.Date = new SimpleStringProperty(Date);
        this.ChequeDate = new SimpleStringProperty(chequeDate);
        this.ChequeNumber = new SimpleStringProperty(chequeNumber);
        this.Bank = new SimpleStringProperty(Bank);
        this.Amount = new SimpleStringProperty(Amount);
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

    public String getYear() {
        return Year.get();
    }

    public StringProperty yearProperty() {
        return Year;
    }

    public void setYear(String year) {
        this.Year.set(year);
    }

    public String getGCR() {
        return gcr.get();
    }

    public String getMonth() {
        return Month.get();
    }

    public StringProperty monthProperty() {
        return Month;
    }

    public void setMonth(String month) {
        this.Month.set(month);
    }

    public StringProperty GCRProperty() {
        return gcr;
    }

    public void setGCR(String GCR) {
        this.gcr.set(GCR);
    }

    public String getDate() {
        return Date.get();
    }

    public StringProperty dateProperty() {
        return Date;
    }

    public void setDate(String date) {
        this.Date.set(date);
    }

    public String getChequeDate() {
        return ChequeDate.get();
    }

    public StringProperty chequeDateProperty() {
        return ChequeDate;
    }

    public void setChequeDate(String chequeDate) {
        this.ChequeDate.set(chequeDate);
    }

    public String getChequeNumber() {
        return ChequeNumber.get();
    }

    public StringProperty chequeNumberProperty() {
        return ChequeNumber;
    }

    public void setChequeNumber(String chequeNumber) {
        this.ChequeNumber.set(chequeNumber);
    }

    public String getBank() {
        return Bank.get();
    }

    public StringProperty bankProperty() {
        return Bank;
    }

    public void setBank(String bank) {
        this.Bank.set(bank);
    }

    public String getAmount() {
        return Amount.get();
    }

    public StringProperty amountProperty() {
        return Amount;
    }

    public void setAmount(String amount) {
        this.Amount.set(amount);
    }
}
