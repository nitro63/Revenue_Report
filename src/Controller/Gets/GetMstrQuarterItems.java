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
public class GetMstrQuarterItems {
    private StringProperty firstMonth, secondMonth, thirdMonth, revenueItem, totalAmount;
    
    public GetMstrQuarterItems(){
        
    }
    
    public GetMstrQuarterItems(String firstMonth, String secondMonth, String thirdMonth, String revenueItem, String totalAmount){
        this.firstMonth = new SimpleStringProperty(firstMonth);
        this.secondMonth = new SimpleStringProperty(secondMonth);
        this.thirdMonth = new SimpleStringProperty(thirdMonth);
        this.revenueItem = new SimpleStringProperty(revenueItem);
        this.totalAmount = new SimpleStringProperty(totalAmount);
        
    }
   
    public StringProperty firstMonthProperty(){
        return firstMonth;
    }
    
    public final String getfirstMonth(){
        return firstMonthProperty().get();
    }
    
    public final void setfirstMonth(String firstMonth){
        firstMonthProperty().set(firstMonth);
    }
    
    public StringProperty secondMonthProperty(){
        return secondMonth;
    }
    
    public final String getsecondMonth(){
        return secondMonthProperty().get();
    }
    
    public final void setsecondMonth(String secondMonth){
        secondMonthProperty().set(secondMonth);
    }  
    
    public StringProperty thirdMonthProperty(){
        return thirdMonth;
    }
    
    public final String getthirdMonth(){
        return thirdMonthProperty().get();
    }
    
    public final void setthirdMonth(String thirdMonth){
        thirdMonthProperty().set(thirdMonth);
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
     
}
