package com.Controller.Gets;

import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.Calendar;

public class GetFunctions {
    public GetFunctions(){
    }
    public String getDate(LocalDate date){
        int actMonth = date.getMonthValue() -1;//Converting Datepicker month value from 1-12 format to 0-11 format
    Calendar cal = Calendar.getInstance();
        cal.set(date.getYear(), actMonth, date.getDayOfMonth());//Initialising assigning datepicker value to Calendar item
        java.util.Date setDate = cal.getTime();//Variable for converting DatePicker value from Calendar to Date for further use
        SimpleDateFormat Dateformat = new SimpleDateFormat("dd-MM-yyyy");// Date format for Date
        String Date = Dateformat.format(setDate);// Assigning converted date with "MM/dd/YY" format to "Date" variable
        return Date;
    }
    public String convertSqlDate(String acDate){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(acDate, dtf2);
        return date.format(dtf);
        /*
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(acDate, dtf);*/
    }
    public String setSqlDate(String acDate){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(acDate, dtf);
        return date.format(dtf2);
        /*
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(acDate, dtf);*/
    }

    public String getMonth(LocalDate date){
       int actMonth = date.getMonthValue() -1;//Converting Datepicker month value from 1-12 format to 0-11 format
    Calendar cal = Calendar.getInstance();
        cal.set(date.getYear(), actMonth, date.getDayOfMonth());//Initialising assigning datepicker value to Calendar item
        java.util.Date setDate = cal.getTime();//Variable for converting DatePicker value from Calendar to Date for further use
        cal.setTime(setDate);//Setting time to Calendar variable
        return new SimpleDateFormat("MMMM").format(cal.getTime());
            }

    public String getWeek(LocalDate date){
       int actMonth = date.getMonthValue() -1;//Converting Datepicker month value from 1-12 format to 0-11 format
    Calendar cal = Calendar.getInstance();
        cal.set(date.getYear(), actMonth, date.getDayOfMonth());
        java.util.Date setDate = cal.getTime();//Variable for converting DatePicker value from Calendar to Date for further use
        cal.setTime(setDate);//Setting time to Calendar variable
        cal.setFirstDayOfWeek(1);
        cal.setMinimalDaysInFirstWeek(1);//Setting the minimal days to make a week;
        return Integer.toString(cal.get(Calendar.WEEK_OF_MONTH));
    }

    public String getQuarter(LocalDate date){
//       int actMonth = date.getMonthValue()-1 ;//Converting Datepicker month value from 1-12 format to 0-11 format
//    Calendar cal = Calendar.getInstance();
//        cal.set(date.getYear(), actMonth, date.getDayOfMonth());
//        java.util.Date setDate = cal.getTime();//Variable for converting DatePicker value from Calendar to Date for further use
//        cal.setTime(setDate);//Setting time to Calendar variable
        return Integer.toString(date.get(IsoFields.QUARTER_OF_YEAR));
    }

    public String getYear(LocalDate date){
       int actMonth = date.getMonthValue() -1;//Converting Datepicker month value from 1-12 format to 0-11 format
    Calendar cal = Calendar.getInstance();
        cal.set(date.getYear(), actMonth, date.getDayOfMonth());
        java.util.Date setDate = cal.getTime();//Variable for converting DatePicker value from Calendar to Date for further use
        cal.setTime(setDate);//Setting time to Calendar variable
        return Integer.toString(cal.get(Calendar.YEAR));
    }
    public String getAmount(String amount){
        double initeAmount = Double.parseDouble(amount);
        NumberFormat formatter = new DecimalFormat("#,##0.00");
        String initAmount = formatter.format(initeAmount);
        formatter.setGroupingUsed(true);
        return initAmount;
    }
    public DatePicker datePicker(DatePicker dtp)
    {
        dtp.setConverter(new StringConverter<LocalDate>() {
        String pattern = "dd/MM/yyyy";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
            /*{
                entDatePck.setPromptText(pattern.toLowerCase());
            }*/

        @Override public String toString(LocalDate date) {
            if (date != null) {
                return dateFormatter.format(date);
            } else {
                return "";
            }
        }

        @Override public LocalDate fromString(String string) {
            if (string != null && !string.isEmpty()) {
                return LocalDate.parse(string, dateFormatter);
            } else {
                return null;
            }
        }
    });
        return dtp;
    }
}
