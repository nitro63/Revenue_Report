/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.Gets.GetCentersReport;
import Controller.Gets.GetColPay;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import revenue_report.DBConnection;

/**
 * FXML Controller class
 *
 * @author NiTrO
 */
public class Collection_PaymentAnalysisReportController implements Initializable {

    @FXML
    private Label lblYear;
    @FXML
    private JFXButton btnPrint;
    @FXML
    private Label lblRevenueCenter;
    @FXML
    private VBox monthlyTemplate;
    @FXML
    private ComboBox<String> cmbReportCent;
    @FXML
    private ComboBox<String> cmbReportYear;
    @FXML
    private JFXButton btnShowReport;
    @FXML
    private TableColumn<?, ?> revenueCenter;
    @FXML
    private TableColumn<?, ?> year;
    @FXML
    private TableColumn<GetColPay, String> colMonth;
    @FXML
    private TableColumn<GetColPay, String> colAmtReptd;
    @FXML
    private TableColumn<GetColPay, String> colAmtPayed;
    @FXML
    private TableColumn<GetColPay, String> colDiff;
    @FXML
    private TableColumn<GetColPay, String> colRemarks;
    @FXML
    private TableView<GetColPay> tblColPayAnalysis;
    @FXML
    private Label lblCenterWarn;
    @FXML
    private Label lblYearWarn;

    @FXML
    private Label lblReportedAmount;

    @FXML
    private Label lblPaidAmount;

    @FXML
    private Label lblDifference;

    @FXML
    private Label lblRemarks;


    private final Connection con;
    private PreparedStatement stmnt;
    ObservableList<String> rowCent =FXCollections.observableArrayList();
    ObservableList<String> rowMonths =FXCollections.observableArrayList();
    ObservableList<String> rowYear =FXCollections.observableArrayList();
    Map<String, String> centerID = new HashMap<>();
    private boolean subMetroPR, Condition;
    
    public Collection_PaymentAnalysisReportController() throws SQLException, ClassNotFoundException{
        this.con = DBConnection.getConn();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbReportCent.setOnMouseClicked(e -> {
            lblCenterWarn.setVisible(false);
        });
        cmbReportYear.setOnMouseClicked(e -> {
            lblYearWarn.setVisible(false);
        });
        try {
            getRevCenters();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(MonthlyReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    
    
    private void getRevCenters() throws SQLException, ClassNotFoundException{
        
            stmnt = con.prepareStatement("SELECT `revenue_center`, `daily_revCenter`, `revenue_category` FROM `revenue_centers`,`daily_entries` WHERE `CenterID` = `daily_revCenter` GROUP BY `daily_revCenter` ");
         ResultSet rs = stmnt.executeQuery();
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
         cmbReportCent.setItems(rowCent);
         cmbReportCent.setVisibleRowCount(5);
    
         
    }
    
     
    private void getReportYear() throws SQLException{
        if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE ALL")) {
            stmnt = con.prepareStatement(" SELECT `revenueYear` FROM `revenue_centers`,`daily_entries` WHERE `CenterID` = `daily_revCenter` AND `revenue_category` = 'PROPERTY RATE SECTION' GROUP BY `revenueYear`");
        } else if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE SUB-METROS")){
            stmnt = con.prepareStatement(" SELECT `revenueYear` FROM `daily_entries`,`revenue_centers` WHERE `CenterID` = `daily_revCenter` AND `daily_revCenter` = 'K0201' OR `daily_revCenter` = 'K0202' OR `daily_revCenter` = 'K0203' OR `daily_revCenter` = 'K0204' OR `daily_revCenter` = 'K0205' GROUP BY `revenueYear`");
        }
        else {
            stmnt = con.prepareStatement(" SELECT `revenueYear` FROM `daily_entries`,`revenue_centers` WHERE `CenterID` = `daily_revCenter` AND`revenue_center` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"'  GROUP BY `revenueYear`");
        }
//        stmnt = con.prepareStatement(" SELECT `revenueYear` FROM `daily_entries` WHERE `daily_revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"'  GROUP BY `revenueYear`");
        ResultSet rs = stmnt.executeQuery();
        rowYear.clear();
        while(rs.next()){
            rowYear.add(rs.getString("revenueYear"));
        }
        cmbReportYear.getItems().clear();
        cmbReportYear.getItems().setAll(rowYear);
        cmbReportYear.setVisibleRowCount(5);
    }
    
    private void changeNames() {
        lblRevenueCenter.setText(cmbReportCent.getSelectionModel().getSelectedItem());
        lblYear.setText(cmbReportYear.getSelectionModel().getSelectedItem());
    }
    
    private void setItems() throws SQLException{
        ObservableList<String> collectionMonth = FXCollections.observableArrayList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");  
        float repMonth, payMonth, diff, totRepMonth = 0, totPayMonth = 0, totDiff;
        NumberFormat formatter = new DecimalFormat("#,##0.00");
        String acRepMonth, acPayMonth, acDiff, acTotRepMonth, acTotPayMonth, acTotDiff, rmks="";
        for(String month : collectionMonth){
           repMonth = setReptMonthSum(cmbReportCent.getSelectionModel().getSelectedItem(), month, cmbReportYear.getSelectionModel().getSelectedItem());
           payMonth = setPayMonthSum(cmbReportCent.getSelectionModel().getSelectedItem(), month, cmbReportYear.getSelectionModel().getSelectedItem());
           diff = repMonth - payMonth ;
           if (diff < 0){
               acDiff = "("+formatter.format(Math.abs(diff))+")";
           }
            acDiff = formatter.format(diff);

            totRepMonth += repMonth;
            totPayMonth += payMonth;
            totDiff = totRepMonth - totPayMonth;
            acTotDiff = formatter.format(totDiff);
           if(totDiff < 0){
               rmks = "Overpayment";
               acTotDiff = "("+formatter.format(Math.abs(totDiff))+")";
           }else if(totDiff > 0){
               rmks = "Underpayment";
           }else if(totDiff == 0){
               rmks = "Balanced";
           }
            acRepMonth =formatter.format( repMonth);
            acPayMonth = formatter.format(payMonth);
            acTotRepMonth =formatter.format( totRepMonth);
            acTotPayMonth = formatter.format(totPayMonth);
           colMonth.setCellValueFactory(data -> data.getValue().MonthProperty());
           colAmtReptd.setCellValueFactory(data -> data.getValue().AmtReprtdProperty());
           colAmtPayed.setCellValueFactory(data -> data.getValue().AmtPayedProperty());
           colDiff.setCellValueFactory(data -> data.getValue().DiffProperty());
            GetColPay getReport = new GetColPay(month, acRepMonth, acPayMonth, acDiff);
           tblColPayAnalysis.getItems().add(getReport);
           lblDifference.setText(acTotDiff);
           lblPaidAmount.setText(acTotPayMonth);
           lblReportedAmount.setText(acTotRepMonth);
           lblRemarks.setText(rmks);
           repMonth = 0;
           payMonth = 0;
           diff = 0;
        }
    }
    
        
       public Float setReptMonthSum(String Center, String Month, String Year) throws SQLException{
        float totalAmunt;
           if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE ALL")) {
               stmnt = con.prepareStatement(" SELECT `revenueAmount` FROM `revenue_centers`,`daily_entries` WHERE `CenterID` = `daily_revCenter` AND `revenue_category` = 'PROPERTY RATE SECTION' AND `revenueYear` = '"+Year+"' AND `revenueMonth` = '"+Month+"' ");
           } else if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE SUB-METROS")){
               stmnt = con.prepareStatement(" SELECT `revenueAmount` FROM `daily_entries`,`revenue_centers` WHERE `revenueYear` = '"+Year+"' AND `revenueMonth` = '"+Month+"' AND `CenterID` = `daily_revCenter` AND `daily_revCenter` = 'K0201' OR `daily_revCenter` = 'K0202' OR `daily_revCenter` = 'K0203' OR `daily_revCenter` = 'K0204' OR `daily_revCenter` = 'K0205' ");
           }
           else {
               stmnt = con.prepareStatement(" SELECT `revenueAmount` FROM `daily_entries`,`revenue_centers` WHERE `CenterID` = `daily_revCenter` AND`revenue_center` = '"+Center+"' AND `revenueYear` = '"+Year+"' AND `revenueMonth` = '"+Month+"' ");
           }
//       stmnt = con.prepareStatement(" SELECT `revenueAmount`   FROM `daily_entries` WHERE `revenueMonth` = '"+Month+"' AND `daily_revCenter` = '"+Center+"' AND `revenueYear` = '"+Year+"'  ");
       ResultSet rs = stmnt.executeQuery();
       ObservableList<Float> Amount = FXCollections.observableArrayList();//List to Store revenue items which have entries for the specified week
       while(rs.next()){//looping through the retrieved revenueItems result set
           Amount.add(rs.getFloat("revenueAmount"));
       }
        totalAmunt = 0;
        if(Amount.isEmpty()){
            totalAmunt = 0;
        }else{
            for(int i = 0; i < Amount.size(); i++){
            totalAmunt += Amount.get(i);
            }
        }
        return totalAmunt;
    }
        
       public Float setPayMonthSum(String Center, String Month, String Year) throws SQLException{
        float totalAmunt;
           if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE ALL")) {
               stmnt = con.prepareStatement(" SELECT `Amount` FROM `revenue_centers`,`collection_payment_entries` WHERE `CenterID` = `pay_revCenter` AND `revenue_category` = 'PROPERTY RATE SECTION' AND `Year` = '"+Year+"' AND `Month` = '"+Month+"'  ");
           } else if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE SUB-METROS")){
               stmnt = con.prepareStatement(" SELECT `Amount` FROM `revenue_centers`,`collection_payment_entries` WHERE `Year` = '"+Year+"' AND `Month` = '"+Month+"' AND `pay_revCenter` = `CenterID` AND `pay_revCenter` = 'K0201' OR `pay_revCenter` = 'K0202' OR `pay_revCenter` = 'K0203' OR `pay_revCenter` = 'K0204' OR `pay_revCenter` = 'K0205' ");
           }
           else {
               stmnt = con.prepareStatement(" SELECT `Amount` FROM `revenue_centers`,`collection_payment_entries` WHERE `CenterID` = `pay_revCenter` AND`revenue_center` = '"+Center+"' AND `Year` = '"+Year+"' AND `Month` = '"+Month+"'  ");
           }
//       stmnt = con.prepareStatement(" SELECT `Amount`   FROM `collection_payment_entries` WHERE  `pay_revCenter` = '"+Center+"' AND `Year` = '"+Year+"' AND `Month` = '"+Month+"'  ");
       ResultSet rs = stmnt.executeQuery();
       ObservableList<Float> Amount = FXCollections.observableArrayList();//List to Store revenue items which have entries for the specified week
       while(rs.next()){//looping through the retrieved revenueItems result set
           Amount.add(rs.getFloat("Amount"));
       }
        totalAmunt = 0;
        if(Amount.isEmpty()){
            totalAmunt = 0;
        }else{
            for(int i = 0; i < Amount.size(); i++){
            totalAmunt += Amount.get(i);
            }
        }
        return totalAmunt;
    }

    @FXML
    private void SelectedCenter(ActionEvent event) throws SQLException {
        getReportYear();
    }

    @FXML
    private void SelectedYear(ActionEvent event) {
    }

    @FXML
    private void ShowReport(ActionEvent event) throws SQLException {
        if(cmbReportCent.getSelectionModel().isEmpty()){
            lblCenterWarn.setVisible(true);
        }else if(cmbReportYear.getSelectionModel().isEmpty()){
        lblYearWarn.setVisible(true);
        }else{
        tblColPayAnalysis.getItems().clear();
        changeNames();
        setItems();
        }
    }

    @FXML
    void printReport(ActionEvent event) throws FileNotFoundException, JRException {
        if (tblColPayAnalysis.getItems().isEmpty()){
            event.consume();
        }else {
            Date date = new Date();
            List<GetColPay> items = new ArrayList<>();
            for (int j = 0; j < tblColPayAnalysis.getItems().size(); j++) {
                GetColPay getdata = tblColPayAnalysis.getItems().get(j);
                items.add(getdata);
            }
            URL url = this.getClass().getResource("/Assets/kmalogo.png");

            JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(items);
            String yr = lblYear.getText(),
                   center = lblRevenueCenter.getText();

            /* Map to hold Jasper report Parameters */
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("CollectionBean", itemsJRBean);
            parameters.put("logo", url); parameters.put("center", center);
            parameters.put("year", yr);
            parameters.put("timeStamp", date);

            //read jrxml file and creating jasperdesign object
            InputStream input = this.getClass().getResourceAsStream("/Assets/revenueVSpayment.jrxml");

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
