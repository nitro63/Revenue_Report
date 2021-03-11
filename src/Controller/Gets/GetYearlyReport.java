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
public class GetYearlyReport {
    private StringProperty year1, year2, year3, year4, year5, revenueItem, totalAmount; 
    public GetYearlyReport(){
    }
    
    public GetYearlyReport(String year1, String year2, String year3, String year4, String year5, String revenueItem, String totalAmount){
        this.revenueItem = new SimpleStringProperty(revenueItem);
        this.totalAmount = new SimpleStringProperty(totalAmount);
        this.year1 = new SimpleStringProperty(year1);
        this.year2 = new SimpleStringProperty(year2);
        this.year3 = new SimpleStringProperty(year3);
        this.year4 = new SimpleStringProperty(year4);
        this.year5 = new SimpleStringProperty(year5);
    }
    
    public StringProperty revenueItemProperty(){
        return revenueItem;
    }
    
    public final String getrevenueItem(){
        return revenueItemProperty().get();
    }
    
    public final void setrevenueItem(String revenueItem){
        revenueItemProperty().set(revenueItem);
    }
    
    public StringProperty totalAmountProperty(){
        return totalAmount;
    }
    
    public final String gettotalAmount(){
        return totalAmountProperty().get();
    }
    
    public final void settotalAmount(String totalAmount){
        totalAmountProperty().set(totalAmount);
    }
    
    public StringProperty year1Property(){
        return year1;
    }
    
    public final String getyear1(){
        return year1Property().get();
    }
    
    public final void setyear1(String year1){
        year1Property().set(year1);
    }
    
    public StringProperty year2Property(){
        return year2;
    }
    
    public final String getyear2(){
        return year2Property().get();
    }
    
    public final void setyear2(String year2){
        year2Property().set(year2);
    }
    
    public StringProperty year3Property(){
        return year3;
    }
    
    public final String getyear3(){
        return year3Property().get();
    }
    
    public final void setyear3(String year3){
        year3Property().set(year3);
    }
    
    public StringProperty year4Property(){
        return year4;
    }
    
    public final String getyear4(){
        return year4Property().get();
    }
    
    public final void setyear4(String year4){
        year4Property().set(year4);
    }
    
    public StringProperty year5Property(){
        return year5;
    }
    
    public final String getyear5(){
        return year5Property().get();
    }
    
    public final void setyear5(String year5){
        year5Property().set(year5);
    }
}
