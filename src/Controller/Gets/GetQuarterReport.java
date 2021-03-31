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
 * @author HP
 */
public class GetQuarterReport {
    private StringProperty FirstMonth, SecondMonth, ThirdMonth, RevenueItem, TotalAmount;
    private String firstMonth, secondMonth, thirdMonth, revenueItem, totalAmount;
    
    public GetQuarterReport(){
        
    }
    public GetQuarterReport(String firstMonth, String secondMonth, String thirdMonth, String revenueItem, String totalAmount){
        this.FirstMonth = new SimpleStringProperty(firstMonth);
        this.SecondMonth = new SimpleStringProperty(secondMonth);
        this.ThirdMonth = new SimpleStringProperty(thirdMonth);
        this.RevenueItem = new SimpleStringProperty(revenueItem);
        this.TotalAmount = new SimpleStringProperty(totalAmount);
    }
    
    public StringProperty firstMonthProperty(){
        return FirstMonth;
    }
    
    public final String getfirstMonth(){
        firstMonth = firstMonthProperty().get();
        return firstMonth;
    }
    
    public final void setfirstMonth(String firstMonth){
        firstMonthProperty().set(firstMonth);
    }
    
    public StringProperty secondMonthProperty(){
        return SecondMonth;
    }
    
    public final String getsecondMonth(){
        secondMonth = secondMonthProperty().get();
        return secondMonth;
    }
    
    public final void setsecondMonth(String secondMonth){
        secondMonthProperty().set(secondMonth);
    }  
    
    public StringProperty thirdMonthProperty(){
        return ThirdMonth;
    }
    
    public final String getthirdMonth(){
        thirdMonth = thirdMonthProperty().get();
        return thirdMonth;
    }
    
    public final void setthirdMonth(String thirdMonth){
        thirdMonthProperty().set(thirdMonth);
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
    
}
