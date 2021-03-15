package Controller.Gets;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class GetValueBooksEntries {
    private StringProperty Year, Month, Date, valueBook, firstSerial, lastSerial, Quantity, valAmount, cumuAmount, purAmount, remarks;

    public GetValueBooksEntries(){   }

    public GetValueBooksEntries(String Year, String month, String date, String valueBook, String firstSerial, String lastSerial, String quantity, String valAmount, String cumuAmount, String purAmount, String remarks) {
        this.Year = new SimpleStringProperty(Year);
        this.Month = new SimpleStringProperty(month);
        this.Date = new SimpleStringProperty(date);
        this.valueBook = new SimpleStringProperty(valueBook);
        this.firstSerial = new SimpleStringProperty(firstSerial);
        this.lastSerial = new SimpleStringProperty(lastSerial);
        this.Quantity = new SimpleStringProperty(quantity);
        this.valAmount = new SimpleStringProperty(valAmount);
        this.cumuAmount = new SimpleStringProperty(cumuAmount);
        this.purAmount = new SimpleStringProperty(purAmount);
        this.remarks = new SimpleStringProperty(remarks);
    }

    public GetValueBooksEntries(String month, String date, String valueBook, String firstSerial, String lastSerial, String quantity, String valAmount, String cumuAmount, String purAmount, String remarks) {
        this.Month = new SimpleStringProperty(month);
        this.Date = new SimpleStringProperty(date);
        this.valueBook = new SimpleStringProperty(valueBook);
        this.firstSerial = new SimpleStringProperty(firstSerial);
        this.lastSerial = new SimpleStringProperty(lastSerial);
        this.Quantity = new SimpleStringProperty(quantity);
        this.valAmount = new SimpleStringProperty(valAmount);
        this.cumuAmount = new SimpleStringProperty(cumuAmount);
        this.purAmount = new SimpleStringProperty(purAmount);
        this.remarks = new SimpleStringProperty(remarks);
    }
    public GetValueBooksEntries(String date, String valueBook, String firstSerial, String lastSerial, String quantity, String valAmount, String cumuAmount, String purAmount, String remarks) {
        this.Date = new SimpleStringProperty(date);
        this.valueBook = new SimpleStringProperty(valueBook);
        this.firstSerial = new SimpleStringProperty(firstSerial);
        this.lastSerial = new SimpleStringProperty(lastSerial);
        this.Quantity = new SimpleStringProperty(quantity);
        this.valAmount = new SimpleStringProperty(valAmount);
        this.cumuAmount = new SimpleStringProperty(cumuAmount);
        this.purAmount = new SimpleStringProperty(purAmount);
        this.remarks = new SimpleStringProperty(remarks);
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
        return Year.get();
    }

    public StringProperty yearProperty() {
        return Year;
    }

    public void setYear(String year) {
        this.Year.set(year);
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

    public String getDate() {
        return Date.get();
    }

    public StringProperty dateProperty() {
        return Date;
    }

    public void setDate(String date) {
        this.Date.set(date);
    }

    public String getValueBook() {
        return valueBook.get();
    }

    public StringProperty valueBookProperty() {
        return valueBook;
    }

    public void setValueBook(String valueBook) {
        this.valueBook.set(valueBook);
    }

    public String getFirstSerial() {
        return firstSerial.get();
    }

    public StringProperty firstSerialProperty() {
        return firstSerial;
    }

    public void setFirstSerial(String firstSerial) {
        this.firstSerial.set(firstSerial);
    }

    public String getLastSerial() {
        return lastSerial.get();
    }

    public StringProperty lastSerialProperty() {
        return lastSerial;
    }

    public void setLastSerial(String lastSerial) {
        this.lastSerial.set(lastSerial);
    }

    public String getQuantity() {
        return Quantity.get();
    }

    public StringProperty quantityProperty() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        this.Quantity.set(quantity);
    }

    public String getValAmount() {
        return valAmount.get();
    }

    public StringProperty valAmountProperty() {
        return valAmount;
    }

    public void setValAmount(String valAmount) {
        this.valAmount.set(valAmount);
    }

    public String getCumuAmount() {
        return cumuAmount.get();
    }

    public StringProperty cumuAmountProperty() {
        return cumuAmount;
    }

    public void setCumuAmount(String cumuAmount) {
        this.cumuAmount.set(cumuAmount);
    }

    public String getPurAmount() {
        return purAmount.get();
    }

    public StringProperty purAmountProperty() {
        return purAmount;
    }

    public void setPurAmount(String purAmount) {
        this.purAmount.set(purAmount);
    }
}
