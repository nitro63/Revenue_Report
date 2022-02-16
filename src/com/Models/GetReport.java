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
public class GetReport {
    
    private String revenueItem, day1, day2, day3, day4, day5, day6, day7, total_Amount, revenueCode ;
    private StringProperty RevenueItem, RevenueCode, Day1, Day2, Day3, Day4, Day5, Day6, Day7, Total_Amount, type;
    
    public GetReport(){
        
    }
   

   public GetReport(String RevenueCode, String RevenueItem, String Day1, String Day2, String Day3, String Day4, String Day5, String Day6, String Day7,  String Total_Amount, String type){
       this.Day1 = new SimpleStringProperty(Day1);
       this.Day2 = new SimpleStringProperty(Day2);
       this.Day3 = new SimpleStringProperty(Day3);
       this.Day4 = new SimpleStringProperty(Day4);
       this.Day5 = new SimpleStringProperty(Day5);
       this.Day6 = new SimpleStringProperty(Day6);
       this.Day7 = new SimpleStringProperty(Day7);
       this.type = new SimpleStringProperty(type);
       this.Total_Amount = new SimpleStringProperty(Total_Amount);
       this.RevenueItem = new SimpleStringProperty(RevenueItem);
       this.RevenueCode = new SimpleStringProperty(RevenueCode);
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

    public String getRevenueCode() {
        revenueCode = RevenueCodeProperty().get();
        return revenueCode;
    }

    public StringProperty RevenueCodeProperty() {
        return RevenueCode;
    }

    public void setRevenueCode(String revenueCode) {
        RevenueCodeProperty().set(revenueCode);
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
       return Total_Amount;
   }
   
   public final String getTotal_Amount(){
        total_Amount = Total_AmountProperty().get();
       return total_Amount;
   }
   
   public final void setRevenueAmount(String Total_Amount){
       Total_AmountProperty().set(Total_Amount);
   }
   
   public StringProperty DAY1Property(){

       return Day1;
   }
   
   public final String getDay1(){
        day1 = DAY1Property().get();
       return day1;
   }
   
   public final void setDay1(String Day1){
       DAY1Property().set(Day1);
   }
   
   public StringProperty DAY2Property(){
       return Day2;
   }
   
   public final String getDay2(){
        day2 = DAY2Property().get();
       return day2;
   }
   
   public final void setDay2(String Day2){
       DAY2Property().set(Day2);
   }
   
   public StringProperty DAY3Property(){
       return Day3;
   }
   
   public final String getDay3(){
        day3 = DAY3Property().get();
       return day3;
   }
   
   public final void setDay3(String Day3){
       DAY3Property().set(Day3);
   }
   
   public StringProperty DAY4Property(){
       return Day4;
   }
   
   public final String getDay4(){
        day4 = DAY4Property().get();
       return day4;
   }
   
   public final void setDay4(String Day4){
       DAY4Property().set(Day4);
   }
   
   public StringProperty DAY5Property(){
       return Day5;
   }
   
   public final String getDay5(){
        day5 = DAY5Property().get();
       return day5;
   }
   
   public final void setDay5(String Day5){
       DAY5Property().set(Day5);
   }
   
   public StringProperty DAY6Property(){
       return Day6;
   }
   
   public final String getDay6(){
        day6 = DAY6Property().get();
       return day6;
   }
   
   public final void setDay6(String Day6){
       DAY6Property().set(Day6);
   }
   
   public StringProperty DAY7Property(){
       return Day7;
   }
   
   public final String getDay7(){
        day7 = DAY7Property().get();
       return day7;
   }
   
   public final void setDay7(String Day7){
       DAY7Property().set(Day7);
   }
}
