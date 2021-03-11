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
public class GetReportgen {
    
    private StringProperty RevenueItem;
    private StringProperty week1, week2, week3, week4, week5, Total_Amount ;
    
    public GetReportgen(){
        
    }
    
    
    public GetReportgen(String RevenueItem, String week1, String week2, String week3, String week4, String week5, String Total_Amount){
       this.week1 = new SimpleStringProperty(week1);
       this.week2 = new SimpleStringProperty(week2);
       this.week3 = new SimpleStringProperty(week3);
       this.week4 = new SimpleStringProperty(week4);
       this.week5 = new SimpleStringProperty(week5);
       this.Total_Amount = new SimpleStringProperty(Total_Amount);       
       this.RevenueItem = new SimpleStringProperty(RevenueItem);
    }   
    public StringProperty RevenueItemProperty(){
       return RevenueItem;
   }
   
   public final String getRevenueItem(){
       return RevenueItemProperty().get();
   }
   
   public final void setRevenueItem(String RevenueItem){
       RevenueItemProperty().set(RevenueItem);
   }
   
   public StringProperty Total_AmountProperty(){
       return Total_Amount;
   }
   
   public final String getTotal_Amount(){
       return Total_AmountProperty().get();
   }
   
   public final void setRevenueAmount(String Total_Amount){
       Total_AmountProperty().set(Total_Amount);
   }
   
   public StringProperty week1Property(){
       return week1;
   }
   
   public final String getweek1(){
       return week1Property().get();
   }
   
   public final void setweek1(String week1){
       week1Property().set(week1);
   }
   
   public StringProperty week2Property(){
       return week2;
   }
   
   public final String getweek2(){
       return week2Property().get();
   }
   
   public final void setweek2(String week2){
       week2Property().set(week2);
   }
   
   public StringProperty week3Property(){
       return week3;
   }
   
   public final String getweek3(){
       return week3Property().get();
   }
   
   public final void setweek3(String week3){
       week3Property().set(week3);
   }
   
   public StringProperty week4Property(){
       return week4;
   }
   
   public final String getweek4(){
       return week4Property().get();
   }
   
   public final void setweek4(String week4){
       week4Property().set(week4);
   }
   
   public StringProperty week5Property(){
       return week5;
   }
   
   public final String getDay5(){
       return week5Property().get();
   }
   
   public final void setDay5(String Day5){
       week5Property().set(Day5);
   }
}

   