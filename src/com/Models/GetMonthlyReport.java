/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author NiTrO
 */
public class GetMonthlyReport {
    private String revenueItem, jan, feb, mar, apr, may, jun, jul, aug, sep, oct, nov, dec, total_Amount;
    private StringProperty RevenueItem, Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec, Total_Amount ;
    
    public GetMonthlyReport(){
        
    }
   
    public GetMonthlyReport(String RevenueItem, String jan, String feb, String mar, String apr, String may, String jun, String jul, String aug, String sep, String oct, String nov, String dec, String Total_Amount){
        this.Jan = new SimpleStringProperty(jan);
        this.Feb = new SimpleStringProperty(feb);
        this.Mar = new SimpleStringProperty(mar);
        this.Apr = new SimpleStringProperty(apr);
        this.May = new SimpleStringProperty(may);
        this.Jun = new SimpleStringProperty(jun);
        this.Jul = new SimpleStringProperty(jul);
        this.Aug = new SimpleStringProperty(aug);
        this.Sep = new SimpleStringProperty(sep);
        this.Oct = new SimpleStringProperty(oct);
        this.Nov = new SimpleStringProperty(nov);
        this.Dec = new SimpleStringProperty(dec);
        this.Total_Amount = new SimpleStringProperty(Total_Amount);
        this.RevenueItem = new SimpleStringProperty(RevenueItem);
    }


    public StringProperty RevenueItemProperty() {
        return RevenueItem;
    }

    public String getRevenueItem() {
        revenueItem = RevenueItemProperty().get();
        return revenueItem;
    }
    public void setRevenueItem(String RevenueItem) {
        RevenueItemProperty().set(RevenueItem);
    }

    public StringProperty JanProperty() {
        return Jan;
    }

    public String getJan() {
        jan = JanProperty().get();
        return jan;
    }
    public void setJan(String jan) {
        JanProperty().set(jan);
    }

    public StringProperty FebProperty() {
        return Feb;
    }

    public String getFeb() {
        feb = FebProperty().get();
        return feb;
    }
    public void setFeb(String feb) {
        FebProperty().set(feb);
    }

    public StringProperty MarProperty() {
        return Mar;
    }

    public String getMar() {
        mar = MarProperty().get();
        return mar;
    }

    public void setMar (String mar) {
        MarProperty().set(mar);
    }

    public StringProperty AprProperty() {
        return Apr;
    }

    public String getApr() {
        apr = AprProperty().get();
        return apr;
    }

    public void setApr (String apr) {
        AprProperty().set(apr);
    }

    public StringProperty MayProperty() {
        return May;
    }

    public String getMay() {
        may = MayProperty().get();
        return may;
    }

    public void setMay (String may) {
        MayProperty().set(may);
    }

    public StringProperty JunProperty() {
        return Jun;
    }

    public String getJun() {
        jun = JunProperty().get();
        return jun;
    }

    public void setJun (String jun) {
        JunProperty().set(jun);
    }

    public StringProperty JulProperty() {
        return Jul;
    }

    public String getJul() {
        jul = JulProperty().get();
        return jul;
    }

    public void setJul (String jul) {
        JulProperty().set(jul);
    }

    public StringProperty AugProperty() {
        return Aug;
    }

    public String getAug() {
        aug = AugProperty().get();
        return aug;
    }

    public void setAug (String aug) {
        AugProperty().set(aug);
    }

    public StringProperty SepProperty() {
        return Sep;
    }

    public String getSep() {
        sep = SepProperty().get();
        return sep;
    }

    public void setSep (String sep) {
        SepProperty().set(sep);
    }

    public StringProperty OctProperty() {
        return Oct;
    }

    public String getOct() {
        oct = OctProperty().get();
        return oct;
    }

    public void setOct (String oct) {
        OctProperty().set(oct);
    }

    public StringProperty NovProperty() {
        return Nov;
    }

    public String getNov() {
        nov = NovProperty().get();
        return nov;
    }

    public void setNov (String nov) {
        NovProperty().set(nov);
    }

    public StringProperty DecProperty() {
        return Dec;
    }

    public String getDec() {
        dec = DecProperty().get();
        return dec;
    }

    public void setDec (String dec) {
        DecProperty().set(dec);
    }

    public StringProperty Total_AmountProperty() {
        return Total_Amount;
    }

    public String getTotal_Amount() {
        total_Amount = Total_AmountProperty().get();
        return total_Amount;
    }
    public void setTotal_Amount(String Total_Amount) {
        Total_AmountProperty().set(Total_Amount);
    }
    
    
    
}
