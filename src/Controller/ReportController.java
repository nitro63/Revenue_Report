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
    private Label lblCenterWarn;
    @FXML
    private Label lblYearWarn;
    @FXML
    private Label lblMonthWarn;
    @FXML
    private Label lblWeekWarn;
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
    private Label lblSumDay1;

    @FXML
    private Label lblSumDay2;

    @FXML
    private Label lblSumDay3;

    @FXML
    private Label lblSumDay4;

    @FXML
    private Label lblSumDay5;

    @FXML
    private Label lblTotalAmount;

    @FXML
    private Label lblSumDay6;

    @FXML
    private Label lblSumDay7;
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
    Map<String, String> centerID = new HashMap<>();
    ObservableList<String> rowDate =FXCollections.observableArrayList();
    ObservableList<String> rowCent =FXCollections.observableArrayList();
    ObservableList<String> rowMonth =FXCollections.observableArrayList();
    ObservableList<String> rowYear =FXCollections.observableArrayList();
    ObservableList<String> rowWeek =FXCollections.observableArrayList();
    ObservableList<String> rowCategories =FXCollections.observableArrayList();
    @FXML
    private JFXButton btnShowReport;
    boolean Condition, subMetroPR;
    
    GetReport getReport;

    public ReportController() throws SQLException, ClassNotFoundException {
        this.con = DBConnection.getConn();
    }
   

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbReportCent.setOnMouseClicked(e -> {
            lblCenterWarn.setVisible(false);
        });
        cmbReportMonth.setOnMouseClicked(e -> {
            lblMonthWarn.setVisible(false);
        });
        cmbReportYear.setOnMouseClicked(e -> {
            lblYearWarn.setVisible(false);
        });
        cmbReportWeek.setOnMouseClicked(e -> {
            lblWeekWarn.setVisible(false);
        });
        try {
            getRevCenters();
        } catch (SQLException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        }  
    
    private void getRevCenters() throws SQLException, ClassNotFoundException{
            stmnt = con.prepareStatement("SELECT `daily_entries`.`daily_revCenter`, `revenue_centers`.`revenue_category`, `revenue_centers`.`revenue_center` FROM `revenue_centers`,`daily_entries` WHERE `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` GROUP BY `daily_revCenter` ");
         ResultSet rs = stmnt.executeQuery();
         rowCent.clear();
         cmbReportCent.getItems().clear();
         while(rs.next()){
             rowCent.add(rs.getString("revenue_center"));
             centerID.put(rs.getString("daily_revCenter"), rs.getString("revenue_center"));
             if (rs.getString("revenue_category").equals("PROPERTY RATE SECTION")){
                 Condition = true;
             }
             if (rs.getString("daily_revCenter").equals("K0201") || rs.getString("daily_revCenter").equals("K0202") || rs.getString("daily_revCenter").equals("K0203") || rs.getString("daily_revCenter").equals("K0204") || rs.getString("daily_revCenter").equals("K0205")){
                 subMetroPR = true;
             }
         }
         if (Condition){
             rowCent.add("PROPERTY RATE ALL");
         }
         if (subMetroPR){
             rowCent.add("PROPERTY RATE SUB-METROS");
         }
         Collections.sort(rowCent);
         cmbReportCent.getItems().addAll(rowCent);
         cmbReportCent.setVisibleRowCount(5);
    }
    private void getReportYear() throws SQLException{
        if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE ALL")) {
            stmnt = con.prepareStatement(" SELECT `daily_entries`.`revenueYear` FROM `revenue_centers`,`daily_entries` WHERE `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `revenue_centers`.`revenue_category` = 'PROPERTY RATE SECTION' GROUP BY `daily_entries`.`revenueYear`");
    } else if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE SUB-METROS")){
            stmnt = con.prepareStatement(" SELECT `daily_entries`.`revenueYear` FROM `daily_entries`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `daily_entries`.`daily_revCenter` = 'K0201' OR `daily_entries`.`daily_revCenter` = 'K0202' OR `daily_entries`.`daily_revCenter` = 'K0203' OR `daily_entries`.`daily_revCenter` = 'K0204' OR `daily_entries`.`daily_revCenter` = 'K0205' GROUP BY `daily_entries`.`revenueYear`");
        }
    else {
        stmnt = con.prepareStatement(" SELECT /*`daily_entries`.*/`revenueYear` FROM `daily_entries`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND`revenue_centers`.`revenue_center` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"'  GROUP BY `revenueYear`");
    }
        ResultSet rs = stmnt.executeQuery();
        rowYear.clear();
        cmbReportYear.getItems().clear();
        while(rs.next()){
            rowYear.add(rs.getString("revenueYear"));
        }
        cmbReportYear.getItems().clear();
        cmbReportYear.getItems().setAll(rowYear);
        cmbReportYear.setVisibleRowCount(5);
    }
    
    private void getReportMonth() throws SQLException{
        if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE ALL")) {
            stmnt = con.prepareStatement(" SELECT `daily_entries`.`revenueMonth` FROM `revenue_centers`,`daily_entries` WHERE `revenueYear` = '" + cmbReportYear.getSelectionModel().getSelectedItem() + "' AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `revenue_centers`.`revenue_category` = 'PROPERTY RATE SECTION' GROUP BY `daily_entries`.`revenueMonth`");
        } else if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE SUB-METROS")){
            stmnt = con.prepareStatement(" SELECT `daily_entries`.`revenueMonth` FROM `daily_entries` WHERE `revenueYear` = '" + cmbReportYear.getSelectionModel().getSelectedItem() + "' AND `daily_entries`.`daily_revCenter` = 'K0201' OR `daily_entries`.`daily_revCenter` = 'K0202' OR `daily_entries`.`daily_revCenter` = 'K0203' OR `daily_entries`.`daily_revCenter` = 'K0204' OR `daily_entries`.`daily_revCenter` = 'K0205' GROUP BY `daily_entries`.`revenueMonth`");
        }
        else {
            stmnt = con.prepareStatement(" SELECT `revenueMonth` FROM `daily_entries`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `revenue_centers`.`revenue_center` = '" + cmbReportCent.getSelectionModel().getSelectedItem() + "' AND `revenueYear` = '" + cmbReportYear.getSelectionModel().getSelectedItem() + "' GROUP BY `revenueMonth`");
        }
        ResultSet rs = stmnt.executeQuery();
        rowMonth.clear();
        cmbReportMonth.getItems().clear();
        while (rs.next()) {
            rowMonth.add(rs.getString("revenueMonth"));
        }
        Collections.sort(rowMonth);
        cmbReportMonth.getItems().clear();
        cmbReportMonth.getItems().setAll(rowMonth);
        cmbReportMonth.setVisibleRowCount(5);
    }
    
    private void getReportWeek() throws SQLException{
        if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE ALL")){
            stmnt = con.prepareStatement(" SELECT `daily_entries`.`revenueWeek` FROM `revenue_centers`,`daily_entries` WHERE `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter`  AND `revenue_centers`.`revenue_category` = 'PROPERTY RATE SECTION' AND `daily_entries`.`revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `daily_entries`.`revenueMonth` = '"+cmbReportMonth.getSelectionModel().getSelectedItem()+"'  GROUP BY `daily_entries`.`revenueWeek`");
        } else if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE SUB-METROS")){
            stmnt = con.prepareStatement(" SELECT `daily_entries`.`revenueWeek` FROM `revenue_centers`,`daily_entries` WHERE `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter`  AND `daily_entries`.`revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `daily_entries`.`revenueMonth` = '"+cmbReportMonth.getSelectionModel().getSelectedItem()+"' AND `daily_entries`.`daily_revCenter` = 'K0201' OR `daily_entries`.`daily_revCenter` = 'K0202' OR `daily_entries`.`daily_revCenter` = 'K0203' OR `daily_entries`.`daily_revCenter` = 'K0204' OR `daily_entries`.`daily_revCenter` = 'K0205'  GROUP BY `daily_entries`.`revenueWeek`");
        }else{
        stmnt = con.prepareStatement(" SELECT `revenueWeek` FROM `daily_entries`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `revenue_centers`.`revenue_center` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `revenueMonth` = '"+cmbReportMonth.getSelectionModel().getSelectedItem()+"'  GROUP BY `revenueWeek`");
    }
        ResultSet rs = stmnt.executeQuery();
        boolean condition = true;
        rowWeek.clear();
        while(rs.next()){
            rowWeek.add(rs.getString("revenueWeek"));
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
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.set(Calendar.WEEK_OF_MONTH, week);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        calendar.set(Calendar.WEEK_OF_YEAR, weekOfYear);
        calendar.setFirstDayOfWeek(1);
        calendar.setMinimalDaysInFirstWeek(1);
        calendar.set(Calendar.DAY_OF_WEEK, day);

        return df.format(calendar.getTime());
    }

    public String getDay(int day, int month, int year, int week){
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
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
        if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE ALL")){
            stmnt = con.prepareStatement(" SELECT `daily_entries`.`revenueDate` FROM `revenue_centers`,`daily_entries` WHERE `daily_entries`.`revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `daily_entries`.`revenueMonth` = '"+cmbReportMonth.getSelectionModel().getSelectedItem()+"' AND `daily_entries`.`revenueWeek` = '"+cmbReportWeek.getSelectionModel().getSelectedItem()+"' AND `daily_entries`.`daily_revCenter` = `revenue_centers`.`CenterID` AND `revenue_centers`.`revenue_category` = 'PROPERTY RATE SECTION'   GROUP BY `daily_entries`.`revenueDate`");
        }else if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE SUB-METROS")){
            stmnt = con.prepareStatement(" SELECT `daily_entries`.`revenueDate` FROM `revenue_centers`,`daily_entries` WHERE `daily_entries`.`revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `daily_entries`.`revenueMonth` = '"+cmbReportMonth.getSelectionModel().getSelectedItem()+"' AND  `daily_entries`.`revenueWeek` = '"+cmbReportWeek.getSelectionModel().getSelectedItem()+"' AND `daily_entries`.`daily_revCenter` = `revenue_centers`.`CenterID` AND `daily_entries`.`daily_revCenter` = 'K0201' OR `daily_entries`.`daily_revCenter` = 'K0202' OR `daily_entries`.`daily_revCenter` = 'K0203' OR `daily_entries`.`daily_revCenter` = 'K0204' OR `daily_entries`.`daily_revCenter` = 'K0205' AND `daily_entries`.`revenueMonth` = '"+cmbReportMonth.getSelectionModel().getSelectedItem()+"' GROUP BY `daily_entries`.`revenueDate`");
        }else {
            stmnt = con.prepareStatement(" SELECT `revenueDate` FROM `daily_entries`,`revenue_centers` WHERE   `revenueWeek` = '"+
                    cmbReportWeek.getSelectionModel().getSelectedItem()+"' AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `revenue_centers`.`revenue_center` = '"+cmbReportCent.
                    getSelectionModel().getSelectedItem()+"' AND `revenueMonth` = '"+cmbReportMonth.getSelectionModel().
                    getSelectedItem()+"' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' GROUP BY `revenueDate`");
        }
        ResultSet rs = stmnt.executeQuery();
        rowDate.clear();
        while(rs.next()){
            rowDate.add(rs.getString("revenueDate"));
        }
        Date mon = new SimpleDateFormat("MMMM").parse(cmbReportMonth.getSelectionModel().getSelectedItem());
        Calendar cal = Calendar.getInstance();
        cal.setTime(mon);
        int monthNum = cal.get(Calendar.MONTH);
        int yearNum = Integer.parseInt(cmbReportYear.getSelectionModel().getSelectedItem());
        int weekNum = Integer.parseInt(cmbReportWeek.getSelectionModel().getSelectedItem());
        DAY1.setText(getDate(1, monthNum, yearNum, weekNum));
        DAY2.setText(getDate(2, monthNum, yearNum, weekNum));
        DAY3.setText(getDate(3, monthNum, yearNum, weekNum));
        DAY4.setText(getDate(4, monthNum, yearNum, weekNum));
        DAY5.setText(getDate(5, monthNum, yearNum, weekNum));
        DAY6.setText(getDate(6, monthNum, yearNum, weekNum));
        DAY7.setText(getDate(7, monthNum, yearNum, weekNum));
        lblDay1.setText(getDay(1, monthNum, yearNum, weekNum));
        lblDay2.setText(getDay(2, monthNum, yearNum, weekNum));
        lblDay3.setText(getDay(3, monthNum, yearNum, weekNum));
        lblDay4.setText(getDay(4, monthNum, yearNum, weekNum));
        lblDay5.setText(getDay(5, monthNum, yearNum, weekNum));
        lblDay6.setText(getDay(6, monthNum, yearNum, weekNum));
        lblDay7.setText(getDay(7, monthNum, yearNum, weekNum));
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
        String totDay1 = "0.00", totDay2 = "0.00", totDay3 = "0.00", totDay4 = "0.00", totDay5 = "0.00", totDay6 = "0.00", totDay7 = "0.00", summation = "0.00", Day1 = "0.00", Day2 = "0.00", Day3 = "0.00", Day4 = "0.00", Day5 = "0.00", Day6 = "0.00", Day7 = "0.00",totalAmnt = "0.00";
        float  totday1 = 0, totday2 = 0,totday3 = 0,totday4 = 0, totday5 = 0, totday6 = 0, totday7 = 0,totdaysum = 0, days1 = 0, days2 = 0, days3 = 0, days4 = 0, days5 = 0, days6 = 0, days7 = 0, total_amount = 0;
        NumberFormat formatter = new DecimalFormat("#,##0.00");
         PreparedStatement stmnt_itemsCategories;
         ResultSet rs, rs_itemsCategories;
         if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE ALL")){
             stmnt = con.prepareStatement("SELECT  `revenueDate`, `item_Sub`, `item_category`, `revenueAmount` FROM `revenue_centers`,`daily_entries`, `revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `daily_entries`.`daily_revCenter` = `revenue_centers`.`CenterID` AND `revenue_centers`.`revenue_category` = 'PROPERTY RATE SECTION'  AND   `daily_entries`.`revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `daily_entries`.`revenueMonth` = '"+cmbReportMonth.getSelectionModel().getSelectedItem()+"' AND `daily_entries`.`revenueWeek` = '"+cmbReportWeek.getSelectionModel().getSelectedItem()+"' ORDER BY `item_Sub` ASC", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
             stmnt_itemsCategories = con.prepareStatement("SELECT `item_Sub`, `item_category` FROM `revenue_centers`,`daily_entries`, `revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `daily_entries`.`daily_revCenter` = `revenue_centers`.`CenterID` AND `revenue_centers`.`revenue_category` = 'PROPERTY RATE SECTION'  AND   `daily_entries`.`revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `daily_entries`.`revenueMonth` = '"+cmbReportMonth.getSelectionModel().getSelectedItem()+"' AND `daily_entries`.`revenueWeek` = '"+cmbReportWeek.getSelectionModel().getSelectedItem()+"' GROUP BY `item_Sub`");
         }else if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE SUB-METROS")){
             stmnt = con.prepareStatement("SELECT  `revenueDate`, `item_Sub`, `item_category`, `revenueAmount` FROM `revenue_centers`,`daily_entries`, `revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `daily_entries`.`daily_revCenter` = `revenue_centers`.`CenterID` AND `CenterID` = 'K0201' OR `CenterID` = 'K0202' OR `CenterID` = 'K0203' OR `CenterID` = 'K0204' OR `CenterID` = 'K0205' AND   `daily_entries`.`revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `daily_entries`.`revenueMonth` = '"+cmbReportMonth.getSelectionModel().getSelectedItem()+"' AND `daily_entries`.`revenueWeek` = '"+cmbReportWeek.getSelectionModel().getSelectedItem()+"' ORDER BY `item_Sub` ASC", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
             stmnt_itemsCategories = con.prepareStatement("SELECT `item_Sub`, `item_category`   FROM `revenue_centers`,`daily_entries`, `revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `daily_entries`.`daily_revCenter` = `revenue_centers`.`CenterID` AND `CenterID` = 'K0201' OR `CenterID` = 'K0202' OR `CenterID` = 'K0203' OR `CenterID` = 'K0204' OR `CenterID` = 'K0205' AND   `daily_entries`.`revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `daily_entries`.`revenueMonth` = '"+cmbReportMonth.getSelectionModel().getSelectedItem()+"' AND `daily_entries`.`revenueWeek` = '"+cmbReportWeek.getSelectionModel().getSelectedItem()+"' GROUP BY `item_Sub`");
         }else {
             stmnt = con.prepareStatement("SELECT  `revenueDate`, `item_Sub`, `item_category`, `revenueAmount` FROM `revenue_centers`,`daily_entries`, `revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `daily_entries`.`daily_revCenter` = `revenue_centers`.`CenterID` AND `revenue_centers`.`revenue_center` = '" + cmbReportCent.getSelectionModel().getSelectedItem() + "'  AND   `daily_entries`.`revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `daily_entries`.`revenueMonth` = '"+cmbReportMonth.getSelectionModel().getSelectedItem()+"' AND `daily_entries`.`revenueWeek` = '"+cmbReportWeek.getSelectionModel().getSelectedItem()+"' ORDER BY `item_Sub` ASC", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
             stmnt_itemsCategories = con.prepareStatement("SELECT `item_Sub`, `item_category`   FROM `revenue_centers`,`daily_entries`, `revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `daily_entries`.`daily_revCenter` = `revenue_centers`.`CenterID` AND `revenue_centers`.`revenue_center` = '" + cmbReportCent.getSelectionModel().getSelectedItem() + "'  AND   `daily_entries`.`revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `daily_entries`.`revenueMonth` = '"+cmbReportMonth.getSelectionModel().getSelectedItem()+"' AND `daily_entries`.`revenueWeek` = '"+cmbReportWeek.getSelectionModel().getSelectedItem()+"' GROUP BY `item_Sub`");
         }
       rs = stmnt.executeQuery();
         rs_itemsCategories = stmnt_itemsCategories.executeQuery();
         rowCategories.clear();
        Map<String, ArrayList<String>> categoriesItem = new HashMap<>();
        categoriesItem.put("", new ArrayList<>());
         while (rs_itemsCategories.next()){
             if (!rowCategories.contains(rs_itemsCategories.getString("item_category"))){
                 rowCategories.add(rs_itemsCategories.getString("item_category"));
             }
             if (!categoriesItem.containsKey(rs_itemsCategories.getString("item_category"))){
                 categoriesItem.put(rs_itemsCategories.getString("item_category"), new ArrayList<>());
                 categoriesItem.get(rs_itemsCategories.getString("item_category")).add(rs_itemsCategories.getString("item_Sub"));
             }else if (categoriesItem.containsKey(rs_itemsCategories.getString("item_category")) && !categoriesItem.get(rs_itemsCategories.getString("item_category")).contains(rs_itemsCategories.getString("item_Sub"))){
                 categoriesItem.get(rs_itemsCategories.getString("item_category")).add(rs_itemsCategories.getString("item_Sub"));
             }
         }
        REVENUE_ITEM.setCellValueFactory(data -> data.getValue().RevenueItemProperty());
        DAY1.setCellValueFactory(data -> data.getValue().DAY1Property());
        DAY2.setCellValueFactory(data -> data.getValue().DAY2Property());
        DAY3.setCellValueFactory(data -> data.getValue().DAY3Property());
        DAY4.setCellValueFactory(data -> data.getValue().DAY4Property());
        DAY5.setCellValueFactory(data -> data.getValue().DAY5Property());
        DAY6.setCellValueFactory(data -> data.getValue().DAY6Property());
        DAY7.setCellValueFactory(data -> data.getValue().DAY7Property());
        Total_amt.setCellValueFactory(data -> data.getValue().Total_AmountProperty());
         for (String category : rowCategories){
             REVENUE_ITEM.setStyle("-fx-alignment: CENTER; -fx-text-fill: #5a5959;");
             getReport = new GetReport(category, "", "", "", "", "", "", "", "");
             WEEKLY_TABLE.getItems().add(getReport);
             float subDay1 = 0, subDay2 = 0, subDay3 = 0, subDay4 = 0, subDay5 = 0, subDay6 = 0, subDay7 = 0, subTotalAmount;
             String subDays1 = "0.00", subDays2 = "0.00", subDays3 = "0.00", subDays4 = "0.00", subDays5 = "0.00", subDays6 = "0.00", subDays7 = "0.00",subTotalAmnt = "0.00";
             for (String items : categoriesItem.get(category)){
                 Map<String, Map<String, Float>> itemWeekSum = new HashMap<>();
                 Map<String, Float> weekSum = new HashMap<>();
                 weekSum.put(DAY1.getText(), days1); weekSum.put(DAY2.getText(), days2); weekSum.put(DAY3.getText(), days3);
                 weekSum.put(DAY4.getText(), days4); weekSum.put(DAY5.getText(), days5); weekSum.put(DAY6.getText(), days6);
                 weekSum.put(DAY7.getText(), days7);
                 itemWeekSum.put(items, weekSum);
                 boolean resultSetState = true;
                 while (resultSetState){
                     rs.next();
                     if (items.equals(rs.getString("item_Sub"))){
                         float amot= itemWeekSum.get(items).get(rs.getString("revenueDate"));
                         amot += rs.getFloat("revenueAmount");
                         itemWeekSum.get(items).put(rs.getString("revenueDate"), amot);
                     }
                     if (rs.isLast()){
                         resultSetState = false;
                     }
                 }
                 if (rs.isLast()){
                     rs.beforeFirst();
                 }
                 subDay1 += itemWeekSum.get(items).get(DAY1.getText()); subDay2 += itemWeekSum.get(items).get(DAY2.getText()); subDay3 += itemWeekSum.get(items).get(DAY3.getText()); subDay4 += itemWeekSum.get(items).get(DAY4.getText()); subDay5 += itemWeekSum.get(items).get(DAY5.getText()); subDay6 += itemWeekSum.get(items).get(DAY6.getText()); subDay7 += itemWeekSum.get(items).get(DAY7.getText());
                 totday1 += itemWeekSum.get(items).get(DAY1.getText()); totday2 += itemWeekSum.get(items).get(DAY2.getText()); totday3 += itemWeekSum.get(items).get(DAY3.getText()); totday4 += itemWeekSum.get(items).get(DAY4.getText()); totday5 += itemWeekSum.get(items).get(DAY5.getText()); totday6 += itemWeekSum.get(items).get(DAY6.getText()); totday7 += itemWeekSum.get(items).get(DAY7.getText());
                 Day1 = formatter.format(itemWeekSum.get(items).get(DAY1.getText())); Day2 = formatter.format(itemWeekSum.get(items).get(DAY2.getText())); Day3 = formatter.format(itemWeekSum.get(items).get(DAY3.getText())); Day4 = formatter.format(itemWeekSum.get(items).get(DAY4.getText()));
                 Day5 = formatter.format(itemWeekSum.get(items).get(DAY5.getText())); Day6 = formatter.format(itemWeekSum.get(items).get(DAY6.getText())); Day7 = formatter.format(itemWeekSum.get(items).get(DAY7.getText()));
                 total_amount = itemWeekSum.get(items).get(DAY1.getText()) + itemWeekSum.get(items).get(DAY2.getText()) + itemWeekSum.get(items).get(DAY3.getText()) + itemWeekSum.get(items).get(DAY4.getText()) + itemWeekSum.get(items).get(DAY5.getText()) + itemWeekSum.get(items).get(DAY6.getText()) + itemWeekSum.get(items).get(DAY7.getText()); totdaysum += total_amount;
                 totalAmnt = formatter.format(total_amount);
                 getReport = new GetReport(items, Day1, Day2, Day3, Day4, Day5, Day6, Day7, totalAmnt);
                 WEEKLY_TABLE.getItems().add(getReport);
                 days1 = 0; days2 = 0; days3 = 0; days4 = 0; days5 = 0; days6 = 0; days7 = 0;
             }
             subTotalAmount = subDay1 + subDay2 + subDay3 + subDay4 + subDay5 + subDay6 + subDay7;
             subDays1 = formatter.format(subDay1); subDays2 = formatter.format(subDay2); subDays3 = formatter.format(subDay3); subDays4 = formatter.format(subDay4);
             subDays5 = formatter.format(subDay5); subDays6 = formatter.format(subDay6); subDays7 = formatter.format(subDay7); subTotalAmnt = formatter.format(subTotalAmount);
             getReport = new GetReport("SUB-TOTAL", subDays1, subDays2, subDays3, subDays4, subDays5, subDays6, subDays7, subTotalAmnt);
             WEEKLY_TABLE.getItems().add(getReport);
         }
        totDay1 = formatter.format(totday1); totDay2 = formatter.format(totday2); totDay3 = formatter.format(totday3); totDay4 = formatter.format(totday4);
        totDay5 = formatter.format(totday5); totDay6 = formatter.format(totday6); totDay7 = formatter.format(totday7); summation = formatter.format(totdaysum);
        getReport = new GetReport("TOTAL",totDay1, totDay2, totDay3, totDay4, totDay5, totDay6, totDay7, summation);
        WEEKLY_TABLE.getItems().add(getReport);
        lblSumDay1.setText(totDay1); lblSumDay2.setText(totDay2); lblSumDay3.setText(totDay3); lblSumDay4.setText(totDay4); lblSumDay5.setText(totDay5); lblSumDay6.setText(totDay6);
        lblSumDay7.setText(totDay7); lblTotalAmount.setText(summation);
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
        if (cmbReportCent.getSelectionModel().isEmpty()){
            lblCenterWarn.setVisible(true);
        }else if (cmbReportYear.getSelectionModel().isEmpty()){
            lblYearWarn.setVisible(true);
        }else if (cmbReportMonth.getSelectionModel().isEmpty()){
            lblMonthWarn.setVisible(true);
        }else if (cmbReportWeek.getSelectionModel().isEmpty()){
            lblWeekWarn.setVisible(true);
        }else{
        changeNames();
        getWeekly();
        setItems();
        }
    }

    @FXML
    void printReport(ActionEvent event) throws FileNotFoundException, JRException {
        if (WEEKLY_TABLE.getItems().isEmpty()){
            event.consume();
        }else {
            Date date = new Date();
            List<GetReport> items = new ArrayList<>();
            for (int j = 0; j < WEEKLY_TABLE.getItems().size(); j++) {
                GetReport getdata;
                getdata = WEEKLY_TABLE.getItems().get(j);
                items.add(getdata);
            }
            URL url = this.getClass().getResource("/Assets/kmalogo.png");
            JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(items);
            String month = cmbReportMonth.getSelectionModel().getSelectedItem(),
            center = cmbReportCent.getSelectionModel().getSelectedItem(),
            year = cmbReportYear.getSelectionModel().getSelectedItem(),
            Day1 = DAY1.getText(), Day2 = DAY2.getText(), Day3 = DAY3.getText(), Day4 = DAY4.getText(),
            Day5 = DAY5.getText(), Day6 = DAY6.getText(), Day7 = DAY7.getText(), day1 = lblDay1.getText(), day2 = lblDay2.getText(),
            day3 = lblDay3.getText(), day4 = lblDay4.getText(), day5 = lblDay5.getText(), day6 = lblDay6.getText(),
            day7 = lblDay7.getText(), week = cmbReportWeek.getSelectionModel().getSelectedItem();

            /* Map to hold Jasper report Parameters */
            Map<String, Object> parameters = new HashMap<>();
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
            InputStream input = this.getClass().getResourceAsStream("/Assets/dailyPotrait.jrxml");

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
