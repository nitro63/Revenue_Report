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
    private StringProperty GCR, date, paymentType, amount, cumuAmount;
    
    public GetPaymentDetails(){
        
    }
    
    public GetPaymentDetails(String date, String GCR, String paymentType, String amount, String cumuAmount){
        this.GCR = new SimpleStringProperty(GCR);
        this.amount = new SimpleStringProperty(amount);
        this.date = new SimpleStringProperty(date);
        this.cumuAmount = new SimpleStringProperty(cumuAmount);
        this.paymentType = new SimpleStringProperty(paymentType);
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
    
    public StringProperty dateProperty(){
        return date;
    }
    
    public final String getdate(){
        return dateProperty().get();
    }
    
    public final void setdate(String date){
        dateProperty().set(date);
    }
    
    public StringProperty amountProperty(){
        return amount;
    }
    
    public final String getamount(){
        return amountProperty().get();
    }
    
    public final void setamount(String amount){
        amountProperty().set(amount);
    }
    
    public StringProperty paymentTypeProperty(){
        return paymentType;
    }
    
    public final String getpaymentType(){
        return paymentTypeProperty().get();
    }
    
    public final void setpaymentType(String paymentType){
        paymentTypeProperty().set(paymentType);
    }
    
    public StringProperty cumuAmountProperty(){
        return cumuAmount;
    }
    
    public final String getcumuAmount(){
        return cumuAmountProperty().get();
    }
    
    public final void setcumuAmount(String cumuAmount){
        cumuAmountProperty().set(cumuAmount);
    }
}
