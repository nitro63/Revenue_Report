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
 * @author HP
 */
public class GetReportgen {
    
    private StringProperty RevenueItem, Week1, Week2, Week3, Week4, Week5, Week6, totalAmount, type ;
    private String revenueItem, week1, week2, week3, week4, week5, week6, Total_Amount ;
    
    public GetReportgen(){
        
    }


    public GetReportgen(String RevenueItem, String week1, String week2, String week3, String week4, String week5, String week6, String Total_Amount, String Type){
        this.Week1 = new SimpleStringProperty(week1);
        this.Week2 = new SimpleStringProperty(week2);
        this.Week3 = new SimpleStringProperty(week3);
        this.Week4 = new SimpleStringProperty(week4);
        this.Week5 = new SimpleStringProperty(week5);
        this.Week6 = new SimpleStringProperty(week6);
        this.type = new SimpleStringProperty(Type);
        this.totalAmount = new SimpleStringProperty(Total_Amount);
        this.RevenueItem = new SimpleStringProperty(RevenueItem);
    }


    public GetReportgen(String RevenueItem, String week1, String week2, String week3, String week4, String week5, String week6, String Total_Amount){
       this.Week1 = new SimpleStringProperty(week1);
       this.Week2 = new SimpleStringProperty(week2);
       this.Week3 = new SimpleStringProperty(week3);
       this.Week4 = new SimpleStringProperty(week4);
       this.Week5 = new SimpleStringProperty(week5);
        this.Week6 = new SimpleStringProperty(week6);
       this.totalAmount = new SimpleStringProperty(Total_Amount);
       this.RevenueItem = new SimpleStringProperty(RevenueItem);
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public StringProperty RevenueItemProperty(){
       return RevenueItem;
   }
   
   public final String getRevenueItem(){
        revenueItem = RevenueItemProperty().get();
       return revenueItem;
   }
   
   public final void setRevenueItem(String RevenueItem){
       RevenueItemProperty().set(RevenueItem);
   }
   
   public StringProperty Total_AmountProperty(){
       return totalAmount;
   }
   
   public final String getTotal_Amount(){
        Total_Amount = Total_AmountProperty().get();
       return Total_Amount;
   }
   
   public final void setRevenueAmount(String Total_Amount){
       Total_AmountProperty().set(Total_Amount);
   }
   
   public StringProperty week1Property(){
       return Week1;
   }
   
   public final String getweek1(){
        week1 = week1Property().get();
       return week1;
   }
   
   public final void setweek1(String week1){
       week1Property().set(week1);
   }
   
   public StringProperty week2Property(){
       return Week2;
   }
   
   public final String getweek2(){
        week2 = week2Property().get();
       return week2;
   }
   
   public final void setweek2(String week2){
       week2Property().set(week2);
   }
   
   public StringProperty week3Property(){
       return Week3;
   }
   
   public final String getweek3(){
        week3 = week3Property().get();
       return week3;
   }
   
   public final void setweek3(String week3){
       week3Property().set(week3);
   }
   
   public StringProperty week4Property(){
       return Week4;
   }
   
   public final String getweek4(){
        week4 = week4Property().get();
       return week4;
   }
   
   public final void setweek4(String week4){
       week4Property().set(week4);
   }
   
   public StringProperty week5Property(){
       return Week5;
   }
   
   public final String getweek5(){
        week5 = week5Property().get();
       return week5;
   }
   
   public final void setDay5(String Day5){
       week5Property().set(Day5);
   }

    public StringProperty week6Property(){
        return Week6;
    }

    public final String getweek6(){
        week6 = week6Property().get();
        return week6;
    }

    public final void setDay6(String Day6){
        week6Property().set(Day6);
    }
}

   