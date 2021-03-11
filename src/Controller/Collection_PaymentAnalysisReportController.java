/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.Gets.GetColPay;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import revenue_report.DBConnection;

/**
 * FXML Controller class
 *
 * @author NiTrO
 */
public class Collection_PaymentAnalysisReportController implements Initializable {

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
        
            stmnt = con.prepareStatement("SELECT `revCenter` FROM `daily_entries` WHERE 1 GROUP BY `revCenter` ");
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
        stmnt = con.prepareStatement(" SELECT `revenueYear` FROM `daily_entries` WHERE `revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"'  GROUP BY `revenueYear`");
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
        revenueCenter.setText(cmbReportCent.getSelectionModel().getSelectedItem());
        year.setText("Year: "+ cmbReportYear.getSelectionModel().getSelectedItem());
    }
    
    private void setItems() throws SQLException{
        ObservableList<String> collectionMonth = FXCollections.observableArrayList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");  
        float repMonth, payMonth, diff;
        NumberFormat formatter = new DecimalFormat("#,##0.00");
        String acRepMonth, acPayMonth, acDiff, rmks="";
        for(String month : collectionMonth){
           repMonth = setReptMonthSum(cmbReportCent.getSelectionModel().getSelectedItem(), month, cmbReportYear.getSelectionModel().getSelectedItem());
           payMonth = setPayMonthSum(cmbReportCent.getSelectionModel().getSelectedItem(), month, cmbReportYear.getSelectionModel().getSelectedItem());
           diff = repMonth - payMonth;
           if(diff > 0){
               rmks = "Excess";
           }else if(diff < 0){
               rmks = "Outstanding";
           }else if(diff == 0){
               rmks = "Balanced";
           }
           acRepMonth =formatter.format( repMonth);
           acPayMonth = formatter.format(payMonth);
           acDiff = formatter.format(diff);
           colMonth.setCellValueFactory(data -> data.getValue().MonthProperty());
           colAmtReptd.setCellValueFactory(data -> data.getValue().AmtReprtdProperty());
           colAmtPayed.setCellValueFactory(data -> data.getValue().AmtPayedProperty());
           colDiff.setCellValueFactory(data -> data.getValue().DiffProperty());
           colRemarks.setCellValueFactory(data -> data.getValue().RmksProperty());
           getReport = new GetColPay(month, acRepMonth, acPayMonth, acDiff, rmks);
           tblColPayAnalysis.getItems().add(getReport);
           System.out.println(repMonth +" "+ payMonth+" "+ diff);
           repMonth = 0;
           payMonth = 0;
           diff = 0;
        }
    }
    
        
       public Float setReptMonthSum(String Center, String Month, String Year) throws SQLException{
        float totalAmunt;
       stmnt = con.prepareStatement(" SELECT `revenueAmount`   FROM `daily_entries` WHERE `revenueMonth` = '"+Month+"' AND `revCenter` = '"+Center+"' AND `revenueYear` = '"+Year+"'  ");
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
       stmnt = con.prepareStatement(" SELECT `Amount`   FROM `collection_payment_entries` WHERE `Month` = '"+Month+"' AND `revCenter` = '"+Center+"' AND `Year` = '"+Year+"'  ");
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
    
}
