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
public class GetCentersReport {
    private StringProperty RevenueCenter;
    private StringProperty jan, feb, mar, apr, may, jun, jul, aug, sep, oct, nov, dec, Total_Amount ;
    
    public GetCentersReport(){
        
    }
    
    public GetCentersReport(String RevenueCenter, String jan, String feb, String mar, String apr, String may, String jun, String jul, String aug, String sep, String oct, String nov, String dec, String Total_Amount){
     this.jan = new SimpleStringProperty(jan);
     this.feb = new SimpleStringProperty(feb);
     this.mar = new SimpleStringProperty(mar);
     this.apr = new SimpleStringProperty(apr);
     this.may = new SimpleStringProperty(may);
     this.jun = new SimpleStringProperty(jun);
     this.jul = new SimpleStringProperty(jul);
     this.aug = new SimpleStringProperty(aug);
     this.sep = new SimpleStringProperty(sep);
     this.oct = new SimpleStringProperty(oct);
     this.nov = new SimpleStringProperty(nov);
     this.dec = new SimpleStringProperty(dec);
     this.Total_Amount = new SimpleStringProperty(Total_Amount);       
     this.RevenueCenter = new SimpleStringProperty(RevenueCenter);   
    }
    
    
    public StringProperty RevenueCenterProperty() {
        return RevenueCenter;
    }

    public String getRevenueCenter() {
        return RevenueCenterProperty().get();
    }
    public void setRevenueCenter(String RevenueCenter) {
        RevenueCenterProperty().set(RevenueCenter);
    }

    public StringProperty JanProperty() {
        return jan;
    }

    public String getJan() {
        return JanProperty().get();
    }
    public void setJan(String jan) {
        JanProperty().set(jan);
    }

    public StringProperty FebProperty() {
        return feb;
    }

    public String getFeb() {
        return FebProperty().get();
    }
    public void setFeb(String feb) {
        FebProperty().set(feb);
    }

    public StringProperty MarProperty() {
        return mar;
    }

    public String getMar() {
        return MarProperty().get();
    }
    
    public void setMar (String mar) {
        MarProperty().set(mar);
    }

    public StringProperty AprProperty() {
        return apr;
    }

    public String getApr() {
        return AprProperty().get();
    }
    
    public void setApr (String apr) {
        AprProperty().set(apr);
    }

    public StringProperty MayProperty() {
        return may;
    }

    public String getMay() {
        return MayProperty().get();
    }
    
    public void setMay (String may) {
        MayProperty().set(may);
    }

    public StringProperty JunProperty() {
        return jun;
    }

    public String getJun() {
        return JunProperty().get();
    }
    
    public void setJun (String jun) {
        JunProperty().set(jun);
    }

   public StringProperty JulProperty() {
        return jul;
    }

    public String getJul() {
        return JulProperty().get();
    }
    
    public void setJul (String jul) {
        JulProperty().set(jul);
    }

    public StringProperty AugProperty() {
        return aug;
    }

    public String getAug() {
        return AugProperty().get();
    }
    
    public void setAug (String aug) {
        AugProperty().set(aug);
    }

    public StringProperty SepProperty() {
        return sep;
    }

    public String getSep() {
        return SepProperty().get();
    }
    
    public void setSep (String sep) {
        SepProperty().set(sep);
    }

    public StringProperty OctProperty() {
        return oct;
    }

    public String getOct() {
        return OctProperty().get();
    }
    
    public void setOct (String oct) {
        OctProperty().set(oct);
    }

    public StringProperty NovProperty() {
        return nov;
    }

    public String getNov() {
        return NovProperty().get();
    }
    
    public void setNov (String nov) {
        NovProperty().set(nov);
    }

    public StringProperty DecProperty() {
        return dec;
    }

    public String getDec() {
        return DecProperty().get();
    }
    
    public void setDec (String dec) {
        DecProperty().set(dec);
    }

    public StringProperty Total_AmountProperty() {
        return Total_Amount;
    }

    public String getTotal_Amount() {
        return Total_AmountProperty().get();
    }
    public void setTotal_Amount(String Total_Amount) {
        Total_AmountProperty().set(Total_Amount);
    }
}
