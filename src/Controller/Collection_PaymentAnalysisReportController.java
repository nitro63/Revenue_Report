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
    private Button btnShowReport;
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
    
    private GetColPay getReport;
    
    
     
    private final Connection con;
    private PreparedStatement stmnt;
    ObservableList<String> rowCent =FXCollections.observableArrayList();
    ObservableList<String> rowMonths =FXCollections.observableArrayList();
    ObservableList<String> rowYear =FXCollections.observableArrayList();
    ObservableList<String> rowItems =FXCollections.observableArrayList();
    int Year;
    
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
        } catch (SQLException ex) {
            Logger.getLogger(MonthlyReportController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MonthlyReportController.class.getName()).log(Level.SEVERE, null, ex);
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
         cmbReportCent.getItems().clear();
         cmbReportCent.setItems(rowCent);
         cmbReportCent.setVisibleRowCount(5);
    
         
    }
    
     
    private void getReportYear() throws SQLException{
        stmnt = con.prepareStatement(" SELECT `revenueYear` FROM `daily_entries` WHERE `daily_revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"'  GROUP BY `revenueYear`");
        ResultSet rs = stmnt.executeQuery();
        ResultSetMetaData meta = rs.getMetaData();
        int colum = meta.getColumnCount();
        rowYear.clear();
        while(rs.next()){
            for(int i= 1; i<=colum; i++)
             {
                 String value = rs.getObject(i).toString();
                 
                 rowYear.add(value);
                 
             }
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
        float repMonth, payMonth, diff;
        NumberFormat formatter = new DecimalFormat("#,##0.00");
        String acRepMonth, acPayMonth, acDiff, rmks="";
        for(String month : collectionMonth){
           repMonth = setReptMonthSum(cmbReportCent.getSelectionModel().getSelectedItem(), month, cmbReportYear.getSelectionModel().getSelectedItem());
           payMonth = setPayMonthSum(cmbReportCent.getSelectionModel().getSelectedItem(), month, cmbReportYear.getSelectionModel().getSelectedItem());
           diff = repMonth - payMonth ;
            acDiff = formatter.format(diff);
           if(diff < 0){
               rmks = "Overpayment";
               acDiff = "("+formatter.format(Math.abs(diff))+")";
           }else if(diff > 0){
               rmks = "Underpayment";
           }else if(diff == 0){
               rmks = "Balanced";
           }
           acRepMonth =formatter.format( repMonth);
           acPayMonth = formatter.format(payMonth);
           colMonth.setCellValueFactory(data -> data.getValue().MonthProperty());
           colAmtReptd.setCellValueFactory(data -> data.getValue().AmtReprtdProperty());
           colAmtPayed.setCellValueFactory(data -> data.getValue().AmtPayedProperty());
           colDiff.setCellValueFactory(data -> data.getValue().DiffProperty());
           colRemarks.setCellValueFactory(data -> data.getValue().RmksProperty());
           getReport = new GetColPay(month, acRepMonth, acPayMonth, acDiff, rmks);
           tblColPayAnalysis.getItems().add(getReport);
           repMonth = 0;
           payMonth = 0;
           diff = 0;
        }
    }
    
        
       public Float setReptMonthSum(String Center, String Month, String Year) throws SQLException{
        float totalAmunt;
       stmnt = con.prepareStatement(" SELECT `revenueAmount`   FROM `daily_entries` WHERE `revenueMonth` = '"+Month+"' AND `daily_revCenter` = '"+Center+"' AND `revenueYear` = '"+Year+"'  ");
       ResultSet rs = stmnt.executeQuery();
       ResultSetMetaData meta= rs.getMetaData();
       int row = 0 ;        
       int col = meta.getColumnCount();
       ObservableList<Float> Amount = FXCollections.observableArrayList();//List to Store revenue items which have entries for the specified week
       while(rs.next()){//looping through the retrieved revenueItems result set
           for(int j=1; j<=col; j++){
               if(j == 1){
           String revitem =rs.getObject(j).toString();
           BigDecimal rp= new BigDecimal(Float.parseFloat(revitem));
           rp.stripTrailingZeros().toPlainString();
//           int rev = Integer.parseInt(revitem);
           Amount.add(Float.parseFloat(revitem));//adding revenue items to list
           System.out.println(rp.stripTrailingZeros().toPlainString());
           }
           }     
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
       stmnt = con.prepareStatement(" SELECT `Amount`   FROM `collection_payment_entries` WHERE `Month` = '"+Month+"' AND `pay_revCenter` = '"+Center+"' AND `Year` = '"+Year+"'  ");
       ResultSet rs = stmnt.executeQuery();
       ResultSetMetaData meta= rs.getMetaData();
       int row = 0 ;        
       int col = meta.getColumnCount();
       ObservableList<Float> Amount = FXCollections.observableArrayList();//List to Store revenue items which have entries for the specified week
       while(rs.next()){//looping through the retrieved revenueItems result set
           for(int j=1; j<=col; j++){
               if(j == 1){
           String revitem =rs.getObject(j).toString();
           Amount.add(Float.parseFloat(revitem));//adding revenue items to list
           }
           }     
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
            List<GetColPay> items = new ArrayList<GetColPay>();
            for (int j = 0; j < tblColPayAnalysis.getItems().size(); j++) {
                GetColPay getdata = new GetColPay();
                getdata = tblColPayAnalysis.getItems().get(j);
                items.add(getdata);
            }
            URL url = this.getClass().getResource("/Assets/kmalogo.png"),
                    file = this.getClass().getResource("/Assets/revenueVSpayment.jrxml");

            System.out.println(items + "\n" + url);
            JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(items);
            String year = lblYear.getText(),
                   center = lblRevenueCenter.getText();

            /* Map to hold Jasper report Parameters */
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("CollectionBean", itemsJRBean);
            parameters.put("logo", url); parameters.put("center", center);
            parameters.put("year", year);
            parameters.put("timeStamp", date);

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
