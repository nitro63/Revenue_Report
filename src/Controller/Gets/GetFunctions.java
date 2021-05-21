package Controller.Gets;

import javafx.scene.control.DatePicker;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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

    public String getMonth(LocalDate date){
       int actMonth = date.getMonthValue() -1;//Converting Datepicker month value from 1-12 format to 0-11 format
    Calendar cal = Calendar.getInstance();
        cal.set(date.getYear(), actMonth, date.getDayOfMonth());//Initialising assigning datepicker value to Calendar item
        java.util.Date setDate = cal.getTime();//Variable for converting DatePicker value from Calendar to Date for further use
        cal.setTime(setDate);//Setting time to Calendar variable
        String Month = new SimpleDateFormat("MMMM").format(cal.getTime());
        return Month;
            }

    public String getWeek(LocalDate date){
       int actMonth = date.getMonthValue() -1;//Converting Datepicker month value from 1-12 format to 0-11 format
    Calendar cal = Calendar.getInstance();
        cal.set(date.getYear(), actMonth, date.getDayOfMonth());
        java.util.Date setDate = cal.getTime();//Variable for converting DatePicker value from Calendar to Date for further use
        cal.setTime(setDate);//Setting time to Calendar variable
        cal.setFirstDayOfWeek(1);
        cal.setMinimalDaysInFirstWeek(1);//Setting the minimal days to make a week;
        String Week = Integer.toString(cal.get(Calendar.WEEK_OF_MONTH));
        return Week;
    }

    public String getQuarter(LocalDate date){
//       int actMonth = date.getMonthValue()-1 ;//Converting Datepicker month value from 1-12 format to 0-11 format
//    Calendar cal = Calendar.getInstance();
//        cal.set(date.getYear(), actMonth, date.getDayOfMonth());
//        java.util.Date setDate = cal.getTime();//Variable for converting DatePicker value from Calendar to Date for further use
//        cal.setTime(setDate);//Setting time to Calendar variable
        String Quarter = Integer.toString(date.get(IsoFields.QUARTER_OF_YEAR));
        return Quarter;
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
}
