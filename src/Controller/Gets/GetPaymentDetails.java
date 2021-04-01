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
public class GetPaymentDetails {
    private StringProperty gcr, Date, PaymentType, Amount, CumuAmount;
    private String GCR, date, paymentType, amount, cumuAmount;
    
    public GetPaymentDetails(){
        
    }
    
    public GetPaymentDetails(String date, String GCR, String paymentType, String amount, String cumuAmount){
        this.gcr = new SimpleStringProperty(GCR);
        this.Amount = new SimpleStringProperty(amount);
        this.Date = new SimpleStringProperty(date);
        this.CumuAmount = new SimpleStringProperty(cumuAmount);
        this.PaymentType = new SimpleStringProperty(paymentType);
    }
    
    public StringProperty GCRProperty(){
        return gcr;
    }
    
    public final String getGCR(){
        GCR = GCRProperty().get();
        return GCR;
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
        paymentType = paymentTypeProperty().get();
        return paymentType;
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
