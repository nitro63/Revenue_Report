package Controller.Gets;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class GetPaymentDetailsRep {
        private StringProperty gcr, Date, PaymentType, Amount, CumuAmount, ID, Month, Year;
        private String GCR, date, amount, cumuAmount;
        /*

        public GetPaymentDetails(){

        }

        public GetPaymentDetails(String Amount, String GCR, String Month, String Date, String Year, String Type, String ID){
            this.Amount = new SimpleStringProperty(Amount);
            this.gcr = new SimpleStringProperty(GCR);
            this.Month = new SimpleStringProperty(Month);
            this.Date = new SimpleStringProperty(Date);
            this.Year = new SimpleStringProperty(Year);
            this.ID = new SimpleStringProperty(ID);
            this.PaymentType = new SimpleStringProperty(Type);
        }

        public GetPaymentDetails(String date, String GCR, String paymentType, String amount, String cumuAmount){
            this.gcr = new SimpleStringProperty(GCR);
            this.Amount = new SimpleStringProperty(amount);
            this.Date = new SimpleStringProperty(date);
            this.CumuAmount = new SimpleStringProperty(cumuAmount);
            this.PaymentType = new SimpleStringProperty(paymentType);
        }*/

        public StringProperty GCRProperty(){
            return gcr;
        }

        public final String getGCR(){
            GCR = GCRProperty().get();
            return GCR;
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

        public String getMonth() {
            return Month.get();
        }

        public StringProperty monthProperty() {
            return Month;
        }

        public void setMonth(String month) {
            this.Month.set(month);
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

        public final void setGCR(String GCR){
            GCRProperty().set(GCR);
        }

        public StringProperty dateProperty(){
            return Date;
        }

        public final String getdate(){
            date = dateProperty().get();
            return date;
        }

        public final void setdate(String date){
            dateProperty().set(date);
        }

        public StringProperty amountProperty(){
            return Amount;
        }

        public final String getamount(){
            amount = amountProperty().get();
            return amount;
        }

        public final void setamount(String amount){
            amountProperty().set(amount);
        }

        public StringProperty paymentTypeProperty(){
            return PaymentType;
        }

        public final String getpaymentType(){
            return paymentTypeProperty().get();
        }

        public final void setpaymentType(String paymentType){
            paymentTypeProperty().set(paymentType);
        }

        public StringProperty cumuAmountProperty(){
            return CumuAmount;
        }

        public final String getcumuAmount(){
            cumuAmount = cumuAmountProperty().get();
            return cumuAmount;
        }

        public final void setcumuAmount(String cumuAmount){
            cumuAmountProperty().set(cumuAmount);
        }
    }


