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
public class GetYearlyReport {
    private String year1, year2, year3, year4, year5, revenueItem, totalAmount;
    private StringProperty Year1, Year2, Year3, Year4, Year5, RevenueItem, TotalAmount;
    public GetYearlyReport(){
    }
    
    public GetYearlyReport(String year1, String year2, String year3, String year4, String year5, String revenueItem, String totalAmount){
        this.RevenueItem = new SimpleStringProperty(revenueItem);
        this.TotalAmount = new SimpleStringProperty(totalAmount);
        this.Year1 = new SimpleStringProperty(year1);
        this.Year2 = new SimpleStringProperty(year2);
        this.Year3 = new SimpleStringProperty(year3);
        this.Year4 = new SimpleStringProperty(year4);
        this.Year5 = new SimpleStringProperty(year5);
    }
    
    public StringProperty revenueItemProperty(){
        return RevenueItem;
    }
    
    public final String getrevenueItem(){
        revenueItem = revenueItemProperty().get();
        return revenueItem;
    }
    
    public final void setrevenueItem(String revenueItem){
        revenueItemProperty().set(revenueItem);
    }
    
    public StringProperty totalAmountProperty(){
        return TotalAmount;
    }
    
    public final String gettotalAmount(){
        totalAmount = totalAmountProperty().get();
        return totalAmount;
    }
    
    public final void settotalAmount(String totalAmount){
        totalAmountProperty().set(totalAmount);
    }
    
    public StringProperty year1Property(){
        return Year1;
    }
    
    public final String getyear1(){
        year1 = year1Property().get();
        return year1;
    }
    
    public final void setyear1(String year1){
        year1Property().set(year1);
    }
    
    public StringProperty year2Property(){
        return Year2;
    }
    
    public final String getyear2(){
        year2 = year2Property().get();
        return year2;
    }
    
    public final void setyear2(String year2){
        year2Property().set(year2);
    }
    
    public StringProperty year3Property(){
        return Year3;
    }
    
    public final String getyear3(){
        year3 = year3Property().get();
        return year3;
    }
    
    public final void setyear3(String year3){
        year3Property().set(year3);
    }
    
    public StringProperty year4Property(){
        return Year4;
    }
    
    public final String getyear4(){
        year4 = year4Property().get();
        return year4;
    }
    
    public final void setyear4(String year4){
        year4Property().set(year4);
    }
    
    public StringProperty year5Property(){
        return Year5;
    }
    
    public final String getyear5(){
        year5 = year5Property().get();
        return year5;
    }
    
    public final void setyear5(String year5){
        year5Property().set(year5);
    }
}
