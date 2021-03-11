package Controller.Gets;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class GetBankDetails {
    StringProperty GCR, Date, chequeDate, chequeNumber, Bank, Amount;
    public GetBankDetails(){
    }
    public GetBankDetails(String GCR, String Date, String chequeDate, String chequeNumber, String Bank, String Amount){
        this.GCR = new SimpleStringProperty(GCR);
        this.Date = new SimpleStringProperty(Date);
        this.chequeDate = new SimpleStringProperty(chequeDate);
        this.chequeNumber = new SimpleStringProperty(chequeNumber);
        this.Bank = new SimpleStringProperty(Bank);
        this.Amount = new SimpleStringProperty(Amount);
    }

    public String getGCR() {
        return GCR.get();
    }

    public StringProperty GCRProperty() {
        return GCR;
    }

    public void setGCR(String GCR) {
        this.GCR.set(GCR);
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
        return chequeDate.get();
    }

    public StringProperty chequeDateProperty() {
        return chequeDate;
    }

    public void setChequeDate(String chequeDate) {
        this.chequeDate.set(chequeDate);
    }

    public String getChequeNumber() {
        return chequeNumber.get();
    }

    public StringProperty chequeNumberProperty() {
        return chequeNumber;
    }

    public void setChequeNumber(String chequeNumber) {
        this.chequeNumber.set(chequeNumber);
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
