/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import Controller.Gets.GetItemsReport;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import revenue_report.DBConnection;
import Controller.Gets.GetReport;

/**
 * FXML Controller class
 *
 * @author NiTrO
 */
public class ReportController implements Initializable {

    @FXML
    private Label lblMonth;
    @FXML
    private Label lblYear;
    @FXML
    private Label lblRevenueCenter;
    @FXML
    private TableView<GetReport> WEEKLY_TABLE;
    @FXML
    private JFXButton btnPrint;
    @FXML
    private AnchorPane generalPane;
    @FXML
    private TableColumn<GetReport, String> REVENUE_ITEM;
    @FXML
    private TableColumn <GetReport, String> DAY1;
    @FXML
    private Label lblDay1;
    @FXML
    private TableColumn<GetReport, String> DAY2;
    @FXML
    private Label lblDay2;
    @FXML
    private TableColumn<GetReport, String> DAY3;
    @FXML
    private Label lblDay3;
    @FXML
    private TableColumn<GetReport, String> DAY4;
    @FXML
    private Label lblDay4;
    @FXML
    private TableColumn<GetReport, String> DAY5;
    @FXML
    private Label lblDay5;
    @FXML
    private TableColumn<GetReport, String> DAY6;
    @FXML
    private Label lblDay6;
    @FXML
    private TableColumn<GetReport, String> DAY7;
    @FXML
    private Label lblDay7;
    @FXML
    private VBox weekly_Template;
    @FXML
    private TableColumn<GetReport, String> Total_amt;
    @FXML
    private Label lblWeek;
    @FXML
    private ComboBox<String> cmbReportCent;
    @FXML
    private ComboBox<String> cmbReportYear;
    @FXML
    private ComboBox<String> cmbReportMonth;
    @FXML
    private ComboBox<String> cmbReportWeek;
    @FXML
    private AnchorPane subPane;
    @FXML
    private TableView<GetReport> WEEKLY_TABLESub;
    @FXML
    private TableColumn<GetReport, String> REVENUE_ITEMSub;
    @FXML
    private TableColumn<GetReport, String> DAY1Sub;
    @FXML
    private Label lblDay1Sub;
    @FXML
    private TableColumn<GetReport, String> DAY2Sub;
    @FXML
    private Label lblDay2Sub;
    @FXML
    private TableColumn<GetReport, String> DAY3Sub;
    @FXML
    private Label lblDay3Sub;
    @FXML
    private TableColumn<GetReport, String> DAY4Sub;
    @FXML
    private Label lblDay4Sub;
    @FXML
    private TableColumn<GetReport, String> DAY5Sub;
    @FXML
    private Label lblDay5Sub;
    @FXML
    private TableColumn<GetReport, String> DAY6Sub;
    @FXML
    private Label lblDay6Sub;
    @FXML
    private TableColumn<GetReport, String> DAY7Sub;
    @FXML
    private Label lblDay7Sub;
    @FXML
    private TableColumn<GetReport, String> Total_amtSub;
    @FXML
    private Label lblCumuAmount;
    @FXML
    private Label lblCCAmount;
    @FXML
    private Label lblCommission;
    @FXML
    private Label lblCostValueBooks;
    @FXML
    private Label lblDiff;
    @FXML
    private Label lblNetRevenue;
    @FXML
    private Label lblAmtDueSub;
    @FXML
    private Label lblAmtDueKMA;
    /**
     * initialises the controller class.
     * @param url
     * @param rb
     */
    
    private final Connection con;
    private PreparedStatement stmnt;
    ObservableList<String> rowDate =FXCollections.observableArrayList();
    ObservableList<String> rowCent =FXCollections.observableArrayList();
    ObservableList<String> rowMonth =FXCollections.observableArrayList();
    ObservableList<String> rowYear =FXCollections.observableArrayList();
    ObservableList<String> rowWeek =FXCollections.observableArrayList();
    @FXML
    private Button btnShowReport;
    
    GetReport getReport;

    public ReportController() throws SQLException, ClassNotFoundException {
        this.con = DBConnection.getConn();
    }
   

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            getRevCenters();
        } catch (SQLException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        }  
    
    private void getRevCenters() throws SQLException, ClassNotFoundException{
        
            stmnt = con.prepareStatement("SELECT `daily_revCenter` FROM `daily_entries` WHERE 1 GROUP BY `daily_revCenter` ");
         ResultSet rs = stmnt.executeQuery();
         ResultSetMetaData metadata = rs.getMetaData();
         int columns = metadata.getColumnCount();
         
         while(rs.next()){
             for(int i= 1; i<=columns; i++)
             {
                 String value = rs.getObject(i).toString();
                 rowCent.add(value);
                 
             }
         }
         cmbReportCent.setItems(rowCent);
         cmbReportCent.setVisibleRowCount(5);
    }
    private void getReportYear() throws SQLException{
        stmnt = con.prepareStatement(" SELECT `revenueYear` FROM `daily_entries` WHERE " +
                "`daily_revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"'  GROUP BY `revenueYear`");
        ResultSet rs = stmnt.executeQuery();
        ResultSetMetaData meta = rs.getMetaData();
        int colum = meta.getColumnCount();
        rowYear.clear();
        while(rs.next()){
            rowYear.add(rs.getString("revenueYear"));
        }
        cmbReportYear.getItems().clear();
        cmbReportYear.getItems().setAll(rowYear);
        cmbReportYear.setVisibleRowCount(5);
    }
    
    private void getReportMonth() throws SQLException{
        stmnt = con.prepareStatement(" SELECT `revenueMonth` FROM `daily_entries` WHERE " +
                "`daily_revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' AND " +
                "`revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' GROUP BY `revenueMonth`");
        ResultSet rs = stmnt.executeQuery();
        ResultSetMetaData meta = rs.getMetaData();
        int colum = meta.getColumnCount();
        rowMonth.clear();
        while(rs.next()){
            for(int i= 1; i<=colum; i++)
             {
                 String value = rs.getObject(i).toString();
                 
                 rowMonth.add(value);
                 
             }
        }
        cmbReportMonth.getItems().clear();
        cmbReportMonth.getItems().setAll(rowMonth);
        cmbReportMonth.setVisibleRowCount(5);
    }
    
    private void getReportWeek() throws SQLException{
        stmnt = con.prepareStatement(" SELECT `revenueWeek` FROM `daily_entries` WHERE " +
                "`daily_revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' AND " +
                "`revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND" +
                " `revenueMonth` = '"+cmbReportMonth.getSelectionModel().getSelectedItem()+"'  GROUP BY `revenueWeek`");
        ResultSet rs = stmnt.executeQuery();
        ResultSetMetaData meta = rs.getMetaData();
        int colum = meta.getColumnCount();
        boolean condition = true;
        rowWeek.clear();
        while(rs.next()){
            for(int i= 1; i<=colum; i++)
             {
                 
                 String value = rs.getObject(i).toString();
                 
                 rowWeek.add(value);
                 
             }
        }
        cmbReportWeek.getItems().clear();
        cmbReportWeek.getItems().setAll(rowWeek);
        cmbReportWeek.setVisibleRowCount(5);
    }
    
    private void changeNames() {
        lblRevenueCenter.setText(cmbReportCent.getSelectionModel().getSelectedItem());
        lblMonth.setText(cmbReportMonth.getSelectionModel().getSelectedItem());
        lblYear.setText(cmbReportYear.getSelectionModel().getSelectedItem());
        lblWeek.setText(cmbReportWeek.getSelectionModel().getSelectedItem());
    }

    public String getDate(int day, int month, int year, int week){
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.WEEK_OF_MONTH, week);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        calendar.set(Calendar.WEEK_OF_YEAR, weekOfYear);
        calendar.set(Calendar.DAY_OF_WEEK, day);

        return df.format(calendar.getTime());
    }

    public String getDay(int day, int month, int year, int week){
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.WEEK_OF_MONTH, week);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        SimpleDateFormat df = new SimpleDateFormat("EEEE");
        calendar.set(Calendar.WEEK_OF_YEAR, weekOfYear);
        calendar.set(Calendar.DAY_OF_WEEK, day);

        return df.format(calendar.getTime());
    }

    
    private void getWeekly() throws SQLException, ParseException {
        stmnt = con.prepareStatement(" SELECT `revenueDate` FROM `daily_entries` WHERE   `revenueWeek` = '"+
                cmbReportWeek.getSelectionModel().getSelectedItem()+"' AND `daily_revCenter` = '"+cmbReportCent.
                getSelectionModel().getSelectedItem()+"' AND `revenueMonth` = '"+cmbReportMonth.getSelectionModel().
                getSelectedItem()+"' GROUP BY `revenueDate`");
        ResultSet rs = stmnt.executeQuery();
        ResultSetMetaData meta = rs.getMetaData();
        int col = meta.getColumnCount();
        rowDate.clear();
        while(rs.next()){
            for(int i=1; i<=col; i++){
                if(i == 1){

                    rowDate.add(rs.getObject(i).toString());

                }
            }
        }
        Date mon = new SimpleDateFormat("MMMM").parse(cmbReportMonth.getSelectionModel().getSelectedItem());
        Calendar cal = Calendar.getInstance();
        cal.setTime(mon);
        int monthNum = cal.get(Calendar.MONTH);
        int yearNum = Integer.parseInt(cmbReportYear.getSelectionModel().getSelectedItem());
        int weekNum = Integer.parseInt(cmbReportWeek.getSelectionModel().getSelectedItem());
        DAY1.setText(getDate(2, monthNum, yearNum, weekNum));
        DAY2.setText(getDate(3, monthNum, yearNum, weekNum));
        DAY3.setText(getDate(4, monthNum, yearNum, weekNum));
        DAY4.setText(getDate(5, monthNum, yearNum, weekNum));
        DAY5.setText(getDate(6, monthNum, yearNum, weekNum));
        DAY6.setText(getDate(7, monthNum, yearNum, weekNum));
        DAY7.setText(getDate(1, monthNum, yearNum, weekNum));
        lblDay1.setText(getDay(2, monthNum, yearNum, weekNum));
        lblDay2.setText(getDay(3, monthNum, yearNum, weekNum));
        lblDay3.setText(getDay(4, monthNum, yearNum, weekNum));
        lblDay4.setText(getDay(5, monthNum, yearNum, weekNum));
        lblDay5.setText(getDay(6, monthNum, yearNum, weekNum));
        lblDay6.setText(getDay(7, monthNum, yearNum, weekNum));
        lblDay7.setText(getDay(1, monthNum, yearNum, weekNum));
//        System.out.println(rowDate.size());
//        int rowSize = rowDate.size();
//        switch(rowSize){
//            case 1:
//                DAY1.setText(rowDate.get(0));
//                break;
//            case 2:
//                DAY1.setText(rowDate.get(0));
//                DAY2.setText(rowDate.get(1));
//                break;
//            case 3:
//                DAY1.setText(rowDate.get(0));
//                DAY2.setText(rowDate.get(1));
//                DAY3.setText(rowDate.get(2));
//                break;
//            case 4:
//                DAY1.setText(rowDate.get(0));
//                DAY2.setText(rowDate.get(1));
//                DAY3.setText(rowDate.get(2));
//                DAY4.setText(rowDate.get(3));
//                break;
//            case 5:
//                DAY1.setText(rowDate.get(0));
//                DAY2.setText(rowDate.get(1));
//                DAY3.setText(rowDate.get(2));
//                DAY4.setText(rowDate.get(3));
//                DAY5.setText(rowDate.get(4));
//                break;
//            case 6:
//                DAY1.setText(rowDate.get(0));
//                DAY2.setText(rowDate.get(1));
//                DAY3.setText(rowDate.get(2));
//                DAY4.setText(rowDate.get(3));
//                DAY5.setText(rowDate.get(4));
//                DAY6.setText(rowDate.get(5));
//                break;
//            case 7:
//                DAY1.setText(rowDate.get(0));
//                DAY2.setText(rowDate.get(1));
//                DAY3.setText(rowDate.get(2));
//                DAY4.setText(rowDate.get(3));
//                DAY5.setText(rowDate.get(4));
//                DAY6.setText(rowDate.get(5));
//                DAY7.setText(rowDate.get(6));
//                break;
//
//        }
        
        
       
   }
    
    @SuppressWarnings("empty-statement")
     private void setItems() throws SQLException{
         /***
          * Retrieving Revenue Items from database per week selected
          * Put Revenue Items into a list for later use
         ***/
        stmnt = con.prepareStatement(" SELECT `revenueItem`   FROM `daily_entries` WHERE   `revenueWeek` = '"+
                cmbReportWeek.getSelectionModel().getSelectedItem()+"' AND `daily_revCenter` = '"+cmbReportCent.
                getSelectionModel().getSelectedItem()+"' AND `revenueMonth` = '"+cmbReportMonth.getSelectionModel().
                getSelectedItem()+"' GROUP BY `revenueItem` ");
       ResultSet rs = stmnt.executeQuery();
       ResultSetMetaData meta= rs.getMetaData();
       int row = 0 ;        
       int col = meta.getColumnCount();
       ObservableList<String> Item = FXCollections.observableArrayList();//List to Store revenue items which have entries for the specified week
       while(rs.next()){//looping through the retrieved revenueItems result set
           for(int j=1; j<=col; j++){
           String revitem =rs.getObject(j).toString();
           Item.add(revitem);//adding revenue items to list
           }
       }
       /***
        * Retrieving revenue items and their respective Amount and Dates in an ordered form by revenueItem 
        * 
       ***/
       stmnt =con.prepareStatement(" SELECT  `revenueItem`, `revenueAmount`, `revenueDate` FROM `daily_entries`" +
               " WHERE   `revenueWeek` = '"+cmbReportWeek.getSelectionModel().getSelectedItem()+"' AND `daily_revCenter` = '"+
               cmbReportCent.getSelectionModel().getSelectedItem()+"' AND `revenueMonth` = '"+cmbReportMonth.
               getSelectionModel().getSelectedItem()+"' ORDER BY `revenueItem` ");
       ResultSet rt = stmnt.executeQuery();
       ResultSetMetaData Meta = rt.getMetaData();
       int Col = Meta.getColumnCount();
       Map<String, ArrayList<Float>> AmountDate = new HashMap<>();//HashMap to store revenue Amounts on their respective dates
       Map<String, Map<String, ArrayList<Float>>> forEntry = new HashMap<>();//HashMap to store entries for tableview 

       
       rowDate.forEach((rowDates) -> {
           AmountDate.put(rowDates, new ArrayList());
          
        });
       System.out.println(AmountDate);
       
       Item.forEach((Items) -> {
           forEntry.put(Items,  new HashMap());
        });
       System.out.println(forEntry); 
       while(rt.next()){
           row =rt.getRow();
           float object2 = Float.parseFloat(rt.getObject(2).toString());
           for(Entry<String, ArrayList<Float>> Dates : AmountDate.entrySet()){
               for(Entry<String, Map<String, ArrayList<Float>>>Items : forEntry.entrySet()){
                   if (Items.getKey().equals(rt.getObject(1).toString())  && Dates.getKey().equals(rt.getObject(3).toString()) ){
                           if(forEntry.containsKey(rt.getObject(1).toString()) && !forEntry.get(rt.getObject(1).toString()).containsValue(rt.getObject(3).toString())){
                           forEntry.get(rt.getObject(1).toString()).put(rt.getObject(3).toString(), new ArrayList());
                           forEntry.get(rt.getObject(1).toString()).get(rt.getObject(3).toString()).add(object2);
                       }else if(forEntry.containsKey(rt.getObject(1).toString()) && forEntry.get(rt.getObject(1).toString()).containsValue(rt.getObject(3).toString())){
                           forEntry.get(rt.getObject(1).toString()).get(rt.getObject(3).toString()).add(object2);
                       }
                   }
               }
           }
       }

       System.out.println(Item+"  "+col+"  "+ row+" "+ forEntry + " ");
       //Items.getValue().entrySet()
         final String Day1= DAY1.getText();
         NumberFormat formatter = new DecimalFormat("#,##0.00");
         
       for(Entry<String, Map<String, ArrayList<Float>>>Items : forEntry.entrySet()){
           String da1 = "0.00", da2 = "0.00", da3 = "0.00", da4 = "0.00", da5 = "0.00", da6 = "0.00", da7 = "0.00", totalAmount = "0.00";
           float day1 = 0, day2 = 0, day3 = 0, day4 = 0, day5 = 0, day6 = 0, day7 = 0, total_amount;
           for(Entry<String, ArrayList<Float>> Dates :forEntry.get(Items.getKey()).entrySet() ){
               String reveItem = Items.getKey();
               System.out.println(reveItem+ "\n"+Items.getValue().get(Dates.getKey()));
               if(Dates.getKey() == null ? DAY1.getText() == null : Dates.getKey().equals(DAY1.getText())){
                   da1 = formatter.format(forEntry.get(Items.getKey()).get(DAY1.getText()).get(0));
                   day1 = forEntry.get(Items.getKey()).get(DAY1.getText()).get(0);
               }else if(Dates.getKey() == null ? DAY2.getText() == null : Dates.getKey().equals(DAY2.getText())){
                   da2 = formatter.format(forEntry.get(Items.getKey()).get(DAY2.getText()).get(0));
                   day2 = forEntry.get(Items.getKey()).get(DAY2.getText()).get(0);
               }else if(Dates.getKey() == null ? DAY3.getText() == null : Dates.getKey().equals(DAY3.getText())){
                   da3 = formatter.format(forEntry.get(Items.getKey()).get(DAY3.getText()).get(0));
                   day3 = forEntry.get(Items.getKey()).get(DAY3.getText()).get(0);
               }else if(Dates.getKey() == null ? DAY4.getText() == null : Dates.getKey().equals(DAY4.getText())){
                   da4 = formatter.format(forEntry.get(Items.getKey()).get(DAY4.getText()).get(0));
                   day4 = forEntry.get(Items.getKey()).get(DAY4.getText()).get(0);
               }else if(Dates.getKey() == null ? DAY5.getText() == null : Dates.getKey().equals(DAY5.getText())){
                   da5 = formatter.format(forEntry.get(Items.getKey()).get(DAY5.getText()).get(0));
                   day5 = forEntry.get(Items.getKey()).get(DAY5.getText()).get(0);
               }else if(Dates.getKey() == null ? DAY6.getText() == null : Dates.getKey().equals(DAY6.getText())){
                   da6 = formatter.format(forEntry.get(Items.getKey()).get(DAY6.getText()).get(0));
                   day6 = forEntry.get(Items.getKey()).get(DAY6.getText()).get(0);
               }else if(Dates.getKey() == null ? DAY7.getText() == null : Dates.getKey().equals(DAY7.getText())){
                   da7 = formatter.format(forEntry.get(Items.getKey()).get(DAY7.getText()).get(0));
                   day7 = forEntry.get(Items.getKey()).get(DAY7.getText()).get(0);
               }
           }
           total_amount = day1 + day2 + day3 +day4 + day5 + day6 + day7 ;
           totalAmount = formatter.format(total_amount);       
           REVENUE_ITEM.setCellValueFactory(data -> data.getValue().RevenueItemProperty());
           DAY1.setCellValueFactory(data -> data.getValue().DAY1Property());
           DAY2.setCellValueFactory(data -> data.getValue().DAY2Property());
           DAY3.setCellValueFactory(data -> data.getValue().DAY3Property());
           DAY4.setCellValueFactory(data -> data.getValue().DAY4Property());
           DAY5.setCellValueFactory(data -> data.getValue().DAY5Property());
           DAY6.setCellValueFactory(data -> data.getValue().DAY6Property());
           DAY7.setCellValueFactory(data -> data.getValue().DAY7Property());
           Total_amt.setCellValueFactory(data -> data.getValue().Total_AmountProperty());
           getReport = new GetReport(Items.getKey(), da1, da2, da3, da4, da5, da6, da7, totalAmount);
           WEEKLY_TABLE.getItems().add(getReport);                                           
       }
     }

    @FXML
    void SelectedYear(ActionEvent event) throws SQLException {
        getReportMonth();
    }
     

    @FXML
    private void SelectedCenter(ActionEvent event) throws SQLException {
        getReportYear();
    }

    @FXML
    private void SelectedMonth(ActionEvent event) throws SQLException {
        getReportWeek();
    }

    @FXML
    private void ShowReport(ActionEvent event) throws SQLException, ParseException {
        WEEKLY_TABLE.getItems().clear();
        changeNames();
        getWeekly();
        setItems();
    }

    @FXML
    void printReport(ActionEvent event) throws FileNotFoundException, JRException {
        if (WEEKLY_TABLE.getItems().isEmpty()){
            event.consume();
        }else {
            Date date = new Date();
            List<GetReport> items = new ArrayList<GetReport>();
            for (int j = 0; j < WEEKLY_TABLE.getItems().size(); j++) {
                GetReport getdata = new GetReport();
                getdata = WEEKLY_TABLE.getItems().get(j);
                items.add(getdata);
            }
            URL url = this.getClass().getResource("/Assets/kmalogo.png"),
                    file = this.getClass().getResource("/Assets/dailyPotrait.jrxml");

            System.out.println(items + "\n" + url);
            JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(items);
            String month = cmbReportMonth.getSelectionModel().getSelectedItem(),
            center = cmbReportCent.getSelectionModel().getSelectedItem(),
            year = cmbReportYear.getSelectionModel().getSelectedItem(),
            Day1 = DAY1.getText(), Day2 = DAY2.getText(), Day3 = DAY3.getText(), Day4 = DAY4.getText(),
            Day5 = DAY5.getText(), Day6 = DAY6.getText(), Day7 = DAY7.getText(), day1 = lblDay1.getText(), day2 = lblDay2.getText(),
            day3 = lblDay3.getText(), day4 = lblDay4.getText(), day5 = lblDay5.getText(), day6 = lblDay6.getText(),
            day7 = lblDay7.getText(), week = cmbReportWeek.getSelectionModel().getSelectedItem();
            System.out.println(day1 +"\n"+ day2 +"\n"+ day3 +"\n"+ day4 +"\n"+ day5);

            /* Map to hold Jasper report Parameters */
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("CollectionBean", itemsJRBean);
            parameters.put("logo", url);
            parameters.put("month", month);
            parameters.put("center", center);
            parameters.put("year", year);
            parameters.put("timeStamp", date);
            parameters.put("DAY1", Day1);parameters.put("DAY2", Day2);parameters.put("DAY3", Day3);
            parameters.put("DAY4", Day4);parameters.put("DAY5", Day5);parameters.put("DAY6", Day6);
            parameters.put("DAY7", Day7);parameters.put("day1", day1);parameters.put("day2", day2);
            parameters.put("day3", day3);parameters.put("day4", day4);parameters.put("day5", day5);
            parameters.put("day6", day6);parameters.put("day7", day7);parameters.put("week", week);

            //read jrxml file and creating jasperdesign object
            InputStream input = new FileInputStream(new File(file.getPath()));

            JasperDesign jasperDesign = JRXmlLoader.load(input);

            /*compiling jrxml with help of JasperReport class*/
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            /* Using jasperReport object to generate PDF */
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

            /*call jasper engine to display report in jasperviewer window*/
            JasperViewer.viewReport(jasperPrint, false);
        }

    }

    

   
    
}
