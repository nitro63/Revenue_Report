/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package revenue_report;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author NiTrO
 */
public class GetReport {
    
    private StringProperty RevenueItem;
    private StringProperty Day1, Day2, Day3, Day4, Day5, Day6, Day7, Total_Amount ;
    
    public GetReport(){
        
    }
   

   public GetReport(String RevenueItem, String Day1, String Day2, String Day3, String Day4, String Day5, String Day6, String Day7,  String Total_Amount){
       this.Day1 = new SimpleStringProperty(Day1);
       this.Day2 = new SimpleStringProperty(Day2);
       this.Day3 = new SimpleStringProperty(Day3);
       this.Day4 = new SimpleStringProperty(Day4);
       this.Day5 = new SimpleStringProperty(Day5);
       this.Day6 = new SimpleStringProperty(Day6);
       this.Day7 = new SimpleStringProperty(Day7);
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
   
   public StringProperty DAY1Property(){
       return Day1;
   }
   
   public final String getDay1(){
       return DAY1Property().get();
   }
   
   public final void setDay1(String Day1){
       DAY1Property().set(Day1);
   }
   
   public StringProperty DAY2Property(){
       return Day2;
   }
   
   public final String getDay2(){
       return DAY2Property().get();
   }
   
   public final void setDay2(String Day2){
       DAY2Property().set(Day2);
   }
   
   public StringProperty DAY3Property(){
       return Day3;
   }
   
   public final String getDay3(){
       return DAY3Property().get();
   }
   
   public final void setDay3(String Day3){
       DAY3Property().set(Day3);
   }
   
   public StringProperty DAY4Property(){
       return Day4;
   }
   
   public final String getDay4(){
       return DAY4Property().get();
   }
   
   public final void setDay4(String Day4){
       DAY4Property().set(Day4);
   }
   
   public StringProperty DAY5Property(){
       return Day5;
   }
   
   public final String getDay5(){
       return DAY5Property().get();
   }
   
   public final void setDay5(String Day5){
       DAY5Property().set(Day5);
   }
   
   public StringProperty DAY6Property(){
       return Day6;
   }
   
   public final String getDay6(){
       return DAY6Property().get();
   }
   
   public final void setDay6(String Day6){
       DAY6Property().set(Day6);
   }
   
   public StringProperty DAY7Property(){
       return Day7;
   }
   
   public final String getDay7(){
       return DAY7Property().get();
   }
   
   public final void setDay7(String Day7){
       DAY7Property().set(Day7);
   }
}
